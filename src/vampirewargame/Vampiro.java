package vampirewargame;

public class Vampiro extends Pieza {

    public Vampiro(ColorBando color) {
        super(TipoPieza.VAMPIRO, color, 3, 4, 5);
    }

    @Override
    public boolean movimientoValido(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        return esAdyacente(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
    }

    public ResultadoDanio absorberSangre(Pieza objetivo) throws JuegoException {
        if (objetivo == null) {
            throw new JuegoException("No hay una pieza enemiga para absorber sangre.");
        }
        if (objetivo.getColor() == color) {
            throw new JuegoException("No puedes absorber sangre de una pieza de tu mismo bando.");
        }

        ResultadoDanio resultado = objetivo.recibirDanio(1);
        if (resultado.getDanoTotal() > 0) {
            vida++;
        }
        return resultado;
    }

    @Override
    public String getHabilidadEspecial() {
        return "Absorción de sangre: resta 1 punto al enemigo y recupera 1 punto de vida.";
    }
}
