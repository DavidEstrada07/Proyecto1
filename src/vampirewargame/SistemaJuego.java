package vampirewargame;

public class SistemaJuego {

    private final RepositorioJugadores jugadores;
    private final RepositorioHistorial historial;
    private Jugador sesionActual;
    private Partida partidaActual;

    public SistemaJuego() {
        jugadores = new RepositorioJugadores();
        historial = new RepositorioHistorial();
        sesionActual = null;
        partidaActual = null;
    }

    public Jugador crearJugador(String usuario, String contrasena) throws JuegoException {
        Jugador nuevo = new Jugador(usuario, contrasena);
        jugadores.agregarJugador(nuevo);
        sesionActual = nuevo;
        return nuevo;
    }

    public Jugador iniciarSesion(String usuario, String contrasena) throws JuegoException {
        Jugador jugador = jugadores.buscarJugador(usuario);
        if (jugador == null || !jugador.validarAcceso(contrasena)) {
            throw new JuegoException("Usuario o contraseña incorrectos, o la cuenta está cerrada.");
        }
        sesionActual = jugador;
        return jugador;
    }

    public void cerrarSesion() {
        sesionActual = null;
        partidaActual = null;
    }

    public void cambiarContrasena(String nuevaContrasena) throws JuegoException {
        validarSesion();
        sesionActual.cambiarContrasena(nuevaContrasena);
    }

    public void cerrarCuenta() throws JuegoException {
        validarSesion();
        sesionActual.cerrarCuenta();
        cerrarSesion();
    }

    public Jugador[] obtenerOponentesDisponibles() throws JuegoException {
        validarSesion();
        Jugador[] activos = jugadores.obtenerJugadoresActivos();
        int cantidad = 0;

        for (int i = 0; i < activos.length; i++) {
            if (activos[i] != sesionActual) {
                cantidad++;
            }
        }

        Jugador[] oponentes = new Jugador[cantidad];
        int posicion = 0;
        for (int i = 0; i < activos.length; i++) {
            if (activos[i] != sesionActual) {
                oponentes[posicion] = activos[i];
                posicion++;
            }
        }
        return oponentes;
    }

    public Partida nuevaPartida(String usuarioOponente) throws JuegoException {
        validarSesion();
        Jugador oponente = jugadores.buscarJugador(usuarioOponente);
        if (oponente == null || !oponente.isActivo()) {
            throw new JuegoException("El oponente seleccionado no está disponible.");
        }
        if (oponente == sesionActual) {
            throw new JuegoException("No puedes jugar contra tu propia cuenta.");
        }

        partidaActual = new Partida(sesionActual, oponente, historial);
        return partidaActual;
    }

    public Jugador[] obtenerRanking() throws JuegoException {
        validarSesion();
        return jugadores.obtenerRanking();
    }

    public RegistroPartida[] obtenerMiHistorial() throws JuegoException {
        validarSesion();
        return historial.obtenerHistorialJugador(sesionActual.getUsuario());
    }

    private void validarSesion() throws JuegoException {
        if (sesionActual == null) {
            throw new JuegoException("No hay una sesión activa.");
        }
    }

    public Jugador getSesionActual() {
        return sesionActual;
    }

    public Partida getPartidaActual() {
        return partidaActual;
    }
}
