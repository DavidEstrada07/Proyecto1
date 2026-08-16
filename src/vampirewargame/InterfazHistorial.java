package vampirewargame;

public interface InterfazHistorial {

    void agregarRegistro(RegistroPartida registro) throws JuegoException;

    RegistroPartida[] obtenerHistorialJugador(String usuario);

    int cantidad();
}
