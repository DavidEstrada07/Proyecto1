package vampirewargame;

public final class Zombie extends Pieza {

    public Zombie(ColorBando color) {
        super(TipoPieza.ZOMBIE, color, 1, 1, 0);
    }

    @Override
    public boolean movimientoValido(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        return false;
    }

    @Override
    public String getHabilidadEspecial() {
        return "No se mueve por sí mismo. Solo ataca cuando el Necrómante lo ordena.";
    }
}
