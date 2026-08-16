package vampirewargame;

public class Partida {

    private final Jugador jugador1;
    private final Jugador jugador2;
    private final Tablero tablero;
    private final Ruleta ruleta;
    private final InterfazHistorial historial;
    private ColorBando turnoActual;
    private TipoPieza tipoHabilitado;
    private int girosUsados;
    private boolean activa;
    private String mensajeFinal;

    public Partida(Jugador jugador1, Jugador jugador2, InterfazHistorial historial) throws JuegoException {
        if (jugador1 == null || jugador2 == null) {
            throw new JuegoException("La partida necesita dos jugadores.");
        }
        if (jugador1 == jugador2 || jugador1.getUsuario().equalsIgnoreCase(jugador2.getUsuario())) {
            throw new JuegoException("El oponente debe ser un jugador diferente.");
        }
        if (!jugador1.isActivo() || !jugador2.isActivo()) {
            throw new JuegoException("Ambos jugadores deben tener sus cuentas activas.");
        }
        if (historial == null) {
            throw new JuegoException("No se encontró el almacenamiento del historial.");
        }

        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.historial = historial;
        tablero = new Tablero();
        ruleta = new Ruleta();
        turnoActual = ColorBando.BLANCO;
        tipoHabilitado = null;
        girosUsados = 0;
        activa = true;
        mensajeFinal = "";
    }

    public ResultadoRuleta girarRuleta() throws JuegoException {
        validarPartidaActiva();
        if (tipoHabilitado != null) {
            throw new JuegoException("Ya tienes una pieza habilitada para este turno.");
        }

        int maximoGiros = getGirosPermitidosTurnoActual();
        if (girosUsados >= maximoGiros) {
            throw new JuegoException("Ya utilizaste todos los giros permitidos en este turno.");
        }

        girosUsados++;
        TipoPieza resultado = ruleta.girar();
        boolean disponible = tablero.hayPiezaDelTipo(turnoActual, resultado);

        if (disponible) {
            tipoHabilitado = resultado;
            return new ResultadoRuleta(resultado, true, false, "La ruleta seleccionó: " + resultado.getNombre() + ".");
        }

        boolean puedeVolver = girosUsados < maximoGiros;
        if (puedeVolver) {
            return new ResultadoRuleta(resultado, false, true, "La ruleta seleccionó " + resultado.getNombre() + ", pero ya no tienes piezas de ese tipo. Puedes volver a girar.");
        }

        String mensaje = "La ruleta seleccionó " + resultado.getNombre() + " y no tienes piezas disponibles de ese tipo. Pierdes el turno.";
        cambiarTurno();
        return new ResultadoRuleta(resultado, false, false, mensaje);
    }

    public String moverPieza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza pieza = validarPiezaOrigen(filaOrigen, columnaOrigen);

        if (!tablero.estaVacia(filaDestino, columnaDestino)) {
            throw new JuegoException("La casilla destino está ocupada. Debes usar una acción de ataque.");
        }
        if (!pieza.movimientoValido(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new JuegoException("Ese movimiento no es válido para " + pieza.getTipo().getNombre() + ".");
        }
        if (pieza.getTipo() == TipoPieza.HOMBRE_LOBO && !tablero.caminoLibreParaDosCasillas(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new JuegoException("El Hombre Lobo no puede atravesar una pieza para moverse dos casillas.");
        }

        tablero.moverPieza(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
        String mensaje = "Se movió " + pieza.getTipo().getNombre() + " a la casilla [" + filaDestino + ", " + columnaDestino + "].";
        terminarAccion();
        return mensaje;
    }

    public String atacarNormal(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza atacante = validarPiezaOrigen(filaOrigen, columnaOrigen);
        Pieza objetivo = validarObjetivoEnemigo(filaDestino, columnaDestino);

        if (!atacante.esAdyacente(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new JuegoException("El ataque normal solo puede dirigirse a una pieza enemiga adyacente.");
        }

        ResultadoDanio resultado = atacante.atacarNormal(objetivo);
        String mensaje = construirMensajeAtaque(objetivo, resultado, filaDestino, columnaDestino);
        resolverDespuesDelAtaque(filaDestino, columnaDestino, objetivo, resultado);
        return mensaje;
    }

    public String absorberSangre(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza pieza = validarPiezaOrigen(filaOrigen, columnaOrigen);
        if (pieza.getTipo() != TipoPieza.VAMPIRO) {
            throw new JuegoException("La absorción de sangre solo pertenece al Vampiro.");
        }
        if (!pieza.esAdyacente(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new JuegoException("El Vampiro solo puede absorber sangre de una pieza enemiga adyacente.");
        }

        Pieza objetivo = validarObjetivoEnemigo(filaDestino, columnaDestino);
        Vampiro vampiro = (Vampiro) pieza;
        ResultadoDanio resultado = vampiro.absorberSangre(objetivo);
        String mensaje = "El Vampiro absorbió sangre. " + construirMensajeAtaque(objetivo, resultado, filaDestino, columnaDestino);
        resolverDespuesDelAtaque(filaDestino, columnaDestino, objetivo, resultado);
        return mensaje;
    }

    public String lanzarLanza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza pieza = validarPiezaOrigen(filaOrigen, columnaOrigen);
        if (pieza.getTipo() != TipoPieza.NECROMANTE) {
            throw new JuegoException("El ataque de lanza solo pertenece al Necrómante.");
        }
        if (!tablero.sinObstruccionLanza(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new JuegoException("La lanza requiere un enemigo a 2 casillas en línea horizontal o vertical y sin obstrucciones.");
        }

        Pieza objetivo = validarObjetivoEnemigo(filaDestino, columnaDestino);
        Necromante necromante = (Necromante) pieza;
        ResultadoDanio resultado = necromante.lanzarLanza(objetivo);
        String mensaje = "El Necrómante lanzó su lanza ignorando el escudo. " + construirMensajeAtaque(objetivo, resultado, filaDestino, columnaDestino);
        resolverDespuesDelAtaque(filaDestino, columnaDestino, objetivo, resultado);
        return mensaje;
    }

    public String invocarZombie(int filaNecromante, int columnaNecromante, int filaDestino, int columnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza pieza = validarPiezaOrigen(filaNecromante, columnaNecromante);
        if (pieza.getTipo() != TipoPieza.NECROMANTE) {
            throw new JuegoException("Solo el Necrómante puede invocar Zombies.");
        }
        if (!tablero.estaVacia(filaDestino, columnaDestino)) {
            throw new JuegoException("El Zombie solo puede invocarse en una casilla vacía.");
        }

        Necromante necromante = (Necromante) pieza;
        tablero.colocarPieza(necromante.invocarZombie(), filaDestino, columnaDestino);
        String mensaje = "El Necrómante invocó un Zombie en la casilla [" + filaDestino + ", " + columnaDestino + "].";
        terminarAccion();
        return mensaje;
    }

    public String atacarPorZombie(int filaNecromante, int columnaNecromante, int filaZombie, int columnaZombie, int filaObjetivo, int columnaObjetivo) throws JuegoException {
        validarPartidaActiva();
        Pieza piezaNecromante = validarPiezaOrigen(filaNecromante, columnaNecromante);
        if (piezaNecromante.getTipo() != TipoPieza.NECROMANTE) {
            throw new JuegoException("Debes seleccionar un Necrómante habilitado por la ruleta.");
        }

        Pieza piezaZombie = tablero.getPieza(filaZombie, columnaZombie);
        if (piezaZombie == null || piezaZombie.getTipo() != TipoPieza.ZOMBIE || piezaZombie.getColor() != turnoActual) {
            throw new JuegoException("Debes seleccionar un Zombie propio para ejecutar este ataque.");
        }

        Pieza objetivo = validarObjetivoEnemigo(filaObjetivo, columnaObjetivo);
        if (!piezaZombie.esAdyacente(filaZombie, columnaZombie, filaObjetivo, columnaObjetivo)) {
            throw new JuegoException("El enemigo debe estar adyacente al Zombie propio.");
        }

        int distanciaFila = Math.abs(filaObjetivo - filaNecromante);
        int distanciaColumna = Math.abs(columnaObjetivo - columnaNecromante);
        if (Math.max(distanciaFila, distanciaColumna) <= 2) {
            throw new JuegoException("El ataque a través de Zombie se usa contra enemigos que no estén a 1 ni a 2 casillas del Necrómante.");
        }

        Necromante necromante = (Necromante) piezaNecromante;
        Zombie zombie = (Zombie) piezaZombie;
        ResultadoDanio resultado = necromante.ordenarAtaqueZombie(zombie, objetivo);
        String mensaje = "El Necrómante ordenó un ataque a través de su Zombie. " + construirMensajeAtaque(objetivo, resultado, filaObjetivo, columnaObjetivo);
        resolverDespuesDelAtaque(filaObjetivo, columnaObjetivo, objetivo, resultado);
        return mensaje;
    }

    public String retirarse(Jugador jugadorRetirado) throws JuegoException {
        validarPartidaActiva();
        if (jugadorRetirado == null || (jugadorRetirado != jugador1 && jugadorRetirado != jugador2)) {
            throw new JuegoException("El jugador indicado no pertenece a esta partida.");
        }

        Jugador ganador = jugadorRetirado == jugador1 ? jugador2 : jugador1;
        String mensaje = jugadorRetirado.getUsuario() + " se ha retirado. ¡Felicidades, " + ganador.getUsuario() + ", has ganado 3 puntos!";
        finalizarPartida(ganador, jugadorRetirado, mensaje);
        return mensaje;
    }

    private Pieza validarPiezaOrigen(int fila, int columna) throws JuegoException {
        if (tipoHabilitado == null) {
            throw new JuegoException("Primero debes girar la ruleta y obtener un tipo de pieza válido.");
        }

        Pieza pieza = tablero.getPieza(fila, columna);
        if (pieza == null) {
            throw new JuegoException("La casilla de origen está vacía.");
        }
        if (pieza.getColor() != turnoActual) {
            throw new JuegoException("La pieza seleccionada no pertenece al jugador del turno actual.");
        }
        if (pieza.getTipo() != tipoHabilitado) {
            throw new JuegoException("La ruleta solo permite usar una pieza de tipo " + tipoHabilitado.getNombre() + ".");
        }
        return pieza;
    }

    private Pieza validarObjetivoEnemigo(int fila, int columna) throws JuegoException {
        Pieza objetivo = tablero.getPieza(fila, columna);
        if (objetivo == null) {
            throw new JuegoException("La casilla destino está vacía.");
        }
        if (objetivo.getColor() == turnoActual) {
            throw new JuegoException("No puedes atacar una pieza propia.");
        }
        return objetivo;
    }

    private String construirMensajeAtaque(Pieza objetivo, ResultadoDanio resultado, int filaDestino, int columnaDestino) {
        if (resultado.isDestruida()) {
            return "Se destruyó la pieza " + objetivo.getTipo().getNombre() + " del jugador " + getJugadorPorColor(objetivo.getColor()).getUsuario() + ".";
        }
        return "Se atacó la pieza " + objetivo.getTipo().getNombre() + " y se le quitaron " + resultado.getDanoTotal() + " puntos; le quedan " + resultado.getEscudoRestante() + " puntos de escudo y " + resultado.getVidaRestante() + " de vida.";
    }

    private void resolverDespuesDelAtaque(int filaDestino, int columnaDestino, Pieza objetivo, ResultadoDanio resultado) throws JuegoException {
        if (resultado.isDestruida()) {
            tablero.eliminarPieza(filaDestino, columnaDestino);
        }

        ColorBando colorEnemigo = turnoActual == ColorBando.BLANCO ? ColorBando.NEGRO : ColorBando.BLANCO;
        if (tablero.contarPiezas(colorEnemigo) == 0) {
            Jugador ganador = getJugadorPorColor(turnoActual);
            Jugador perdedor = getJugadorPorColor(colorEnemigo);
            String mensaje = ganador.getUsuario() + " venció a " + perdedor.getUsuario() + ". ¡Felicidades, has ganado 3 puntos!";
            finalizarPartida(ganador, perdedor, mensaje);
        } else {
            terminarAccion();
        }
    }

    private void terminarAccion() {
        cambiarTurno();
    }

    private void cambiarTurno() {
        turnoActual = turnoActual == ColorBando.BLANCO ? ColorBando.NEGRO : ColorBando.BLANCO;
        tipoHabilitado = null;
        girosUsados = 0;
    }

    private int getGirosPermitidosTurnoActual() {
        int piezasRestantes = tablero.contarPiezasPrincipales(turnoActual);
        int perdidas = 6 - piezasRestantes;
        return ruleta.calcularGirosPermitidos(perdidas);
    }

    private void finalizarPartida(Jugador ganador, Jugador perdedor, String mensaje) throws JuegoException {
        activa = false;
        mensajeFinal = mensaje;
        ganador.sumarPuntos(3);
        historial.agregarRegistro(new RegistroPartida(jugador1.getUsuario(), jugador2.getUsuario(), ganador.getUsuario(), perdedor.getUsuario(), mensaje));
    }

    private void validarPartidaActiva() throws JuegoException {
        if (!activa) {
            throw new JuegoException("La partida ya finalizó.");
        }
    }

    public Jugador getJugadorPorColor(ColorBando color) {
        return color == ColorBando.BLANCO ? jugador1 : jugador2;
    }

    public Jugador getJugadorTurnoActual() {
        return getJugadorPorColor(turnoActual);
    }

    public Tablero getTablero() {
        return tablero;
    }

    public ColorBando getTurnoActual() {
        return turnoActual;
    }

    public TipoPieza getTipoHabilitado() {
        return tipoHabilitado;
    }

    public int getGirosUsados() {
        return girosUsados;
    }

    public int getGirosPermitidos() {
        return getGirosPermitidosTurnoActual();
    }

    public boolean isActiva() {
        return activa;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }
}
