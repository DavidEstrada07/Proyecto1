package vampirewargame;

import java.util.Calendar;

public class Jugador {

    private final String usuario;
    private String contrasena;
    private int puntos;
    private final Calendar fechaIngreso;
    private boolean activo;

    public Jugador(String usuario, String contrasena) throws JuegoException {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new JuegoException("El nombre de usuario no puede quedar vacío.");
        }
        validarContrasena(contrasena);
        this.usuario = usuario.trim();
        this.contrasena = contrasena;
        puntos = 0;
        fechaIngreso = Calendar.getInstance();
        activo = true;
    }

    private void validarContrasena(String contrasena) throws JuegoException {
        if (contrasena == null || contrasena.length() != 5) {
            throw new JuegoException("La contraseña debe tener exactamente 5 caracteres.");
        }
    }

    public boolean validarAcceso(String contrasena) {
        return activo && this.contrasena.equals(contrasena);
    }

    public void cambiarContrasena(String nuevaContrasena) throws JuegoException {
        validarContrasena(nuevaContrasena);
        contrasena = nuevaContrasena;
    }

    public void sumarPuntos(int cantidad) {
        if (cantidad > 0) {
            puntos += cantidad;
        }
    }

    public void cerrarCuenta() {
        activo = false;
    }

    public String getFechaIngresoTexto() {
        int dia = fechaIngreso.get(Calendar.DAY_OF_MONTH);
        int mes = fechaIngreso.get(Calendar.MONTH) + 1;
        int anio = fechaIngreso.get(Calendar.YEAR);
        int hora = fechaIngreso.get(Calendar.HOUR_OF_DAY);
        int minuto = fechaIngreso.get(Calendar.MINUTE);
        return String.format("%02d/%02d/%04d %02d:%02d", dia, mes, anio, hora, minuto);
    }

    public String getUsuario() {
        return usuario;
    }

    public int getPuntos() {
        return puntos;
    }

    public Calendar getFechaIngreso() {
        return (Calendar) fechaIngreso.clone();
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    public String toString() {
        return usuario + " | puntos: " + puntos + " | ingreso: " + getFechaIngresoTexto() + " | activo: " + activo;
    }
}
