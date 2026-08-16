package vampirewargame;

public class HombreLobo extends Pieza {

    public HombreLobo(ColorBando color) {
        super(TipoPieza.HOMBRE_LOBO, color, 5, 5, 2);
    }

    @Override
    public boolean movimientoValido(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        int diferenciaFila = Math.abs(filaDestino - filaOrigen);
        int diferenciaColumna = Math.abs(columnaDestino - columnaOrigen);

        if (diferenciaFila == 0 && diferenciaColumna == 0) {
            return false;
        }

        boolean horizontal = diferenciaFila == 0 && diferenciaColumna <= 2;
        boolean vertical = diferenciaColumna == 0 && diferenciaFila <= 2;
        boolean diagonal = diferenciaFila == diferenciaColumna && diferenciaFila <= 2;

        return horizontal || vertical || diagonal;
    }

    @Override
    public String getHabilidadEspecial() {
        return "Desplazamiento extendido: puede moverse hasta 2 casillas vacías en cualquier dirección.";
    }
}
