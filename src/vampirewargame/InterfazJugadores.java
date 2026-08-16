package vampirewargame;

public interface InterfazJugadores {

    void agregarJugador(Jugador jugador) throws JuegoException;

    Jugador buscarJugador(String usuario);

    Jugador[] obtenerJugadoresActivos();

    Jugador[] obtenerRanking();

    int cantidad();
}
