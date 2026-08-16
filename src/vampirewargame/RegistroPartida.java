package vampirewargame;

import java.util.Calendar;

public class RegistroPartida {

    private final String jugador1;
    private final String jugador2;
    private final String ganador;
    private final String perdedor;
    private final String mensaje;
    private final Calendar fecha;

    public RegistroPartida(String jugador1, String jugador2, String ganador, String perdedor, String mensaje) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ganador = ganador;
        this.perdedor = perdedor;
        this.mensaje = mensaje;
        fecha = Calendar.getInstance();
    }

    public boolean participo(String usuario) {
        return jugador1.equalsIgnoreCase(usuario) || jugador2.equalsIgnoreCase(usuario);
    }

    public String getFechaTexto() {
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int anio = fecha.get(Calendar.YEAR);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        return String.format("%02d/%02d/%04d %02d:%02d", dia, mes, anio, hora, minuto);
    }

    public String getJugador1() {
        return jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public String getGanador() {
        return ganador;
    }

    public String getPerdedor() {
        return perdedor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Calendar getFecha() {
        return (Calendar) fecha.clone();
    }

    @Override
    public String toString() {
        return getFechaTexto() + " - " + mensaje;
    }
}
