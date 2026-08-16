package vampirewargame;

public class RepositorioJugadores implements InterfazJugadores {

    private Jugador[] jugadores;
    private int cantidad;

    public RepositorioJugadores() {
        jugadores = new Jugador[10];
        cantidad = 0;
    }

    @Override
    public void agregarJugador(Jugador jugador) throws JuegoException {
        if (jugador == null) {
            throw new JuegoException("El jugador no puede ser nulo.");
        }
        if (buscarJugador(jugador.getUsuario()) != null) {
            throw new JuegoException("Ese nombre de usuario ya existe.");
        }

        ampliarArregloSiEsNecesario();
        jugadores[cantidad] = jugador;
        cantidad++;
    }

    private void ampliarArregloSiEsNecesario() {
        if (cantidad < jugadores.length) {
            return;
        }

        Jugador[] nuevo = new Jugador[jugadores.length * 2];
        for (int i = 0; i < jugadores.length; i++) {
            nuevo[i] = jugadores[i];
        }
        jugadores = nuevo;
    }

    @Override
    public Jugador buscarJugador(String usuario) {
        if (usuario == null) {
            return null;
        }
        return buscarJugadorRecursivo(usuario.trim(), 0);
    }

    // Función recursiva 1: búsqueda de jugador.
    private Jugador buscarJugadorRecursivo(String usuario, int indice) {
        if (indice >= cantidad) {
            return null;
        }
        if (jugadores[indice].getUsuario().equalsIgnoreCase(usuario)) {
            return jugadores[indice];
        }
        return buscarJugadorRecursivo(usuario, indice + 1);
    }

    @Override
    public Jugador[] obtenerJugadoresActivos() {
        int activos = 0;
        for (int i = 0; i < cantidad; i++) {
            if (jugadores[i].isActivo()) {
                activos++;
            }
        }

        Jugador[] resultado = new Jugador[activos];
        int posicion = 0;
        for (int i = 0; i < cantidad; i++) {
            if (jugadores[i].isActivo()) {
                resultado[posicion] = jugadores[i];
                posicion++;
            }
        }
        return resultado;
    }

    @Override
    public Jugador[] obtenerRanking() {
        Jugador[] ranking = obtenerJugadoresActivos();
        ordenarRankingRecursivo(ranking, 0);
        return ranking;
    }

    // Función recursiva 2: ordena el ranking de mayor a menor cantidad de puntos.
    private void ordenarRankingRecursivo(Jugador[] ranking, int inicio) {
        if (inicio >= ranking.length - 1) {
            return;
        }

        int posicionMayor = inicio;
        for (int i = inicio + 1; i < ranking.length; i++) {
            if (ranking[i].getPuntos() > ranking[posicionMayor].getPuntos()) {
                posicionMayor = i;
            }
        }

        Jugador temporal = ranking[inicio];
        ranking[inicio] = ranking[posicionMayor];
        ranking[posicionMayor] = temporal;

        ordenarRankingRecursivo(ranking, inicio + 1);
    }

    @Override
    public int cantidad() {
        return cantidad;
    }
}
