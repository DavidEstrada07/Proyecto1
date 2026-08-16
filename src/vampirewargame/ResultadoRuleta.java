package vampirewargame;

public final class ResultadoRuleta {

    private final TipoPieza tipoSeleccionado;
    private final boolean puedeJugar;
    private final boolean puedeVolverAGirar;
    private final String mensaje;

    public ResultadoRuleta(TipoPieza tipoSeleccionado, boolean puedeJugar, boolean puedeVolverAGirar, String mensaje) {
        this.tipoSeleccionado = tipoSeleccionado;
        this.puedeJugar = puedeJugar;
        this.puedeVolverAGirar = puedeVolverAGirar;
        this.mensaje = mensaje;
    }

    public TipoPieza getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public boolean isPuedeJugar() {
        return puedeJugar;
    }

    public boolean isPuedeVolverAGirar() {
        return puedeVolverAGirar;
    }

    public String getMensaje() {
        return mensaje;
    }
}
