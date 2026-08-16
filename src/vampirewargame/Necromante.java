package vampirewargame;

public class Necromante extends Pieza {

    public Necromante(ColorBando color) {
        super(TipoPieza.NECROMANTE, color, 4, 3, 1);
    }

    @Override
    public boolean movimientoValido(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        return esAdyacente(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
    }

    public ResultadoDanio lanzarLanza(Pieza objetivo) throws JuegoException {
        if (objetivo == null) {
            throw new JuegoException("No hay una pieza enemiga en el destino.");
        }
        if (objetivo.getColor() == color) {
            throw new JuegoException("No puedes lanzar la lanza contra una pieza de tu mismo bando.");
        }
        return objetivo.recibirDanioDirectoVida(2);
    }

    public Zombie invocarZombie() {
        return new Zombie(color);
    }

    public ResultadoDanio ordenarAtaqueZombie(Zombie zombie, Pieza objetivo) throws JuegoException {
        if (zombie == null || zombie.getColor() != color) {
            throw new JuegoException("Debes seleccionar un Zombie propio.");
        }
        return zombie.atacarNormal(objetivo);
    }

    @Override
    public String getHabilidadEspecial() {
        return "Lanza a distancia, invocación de Zombie y ataque a través de un Zombie propio.";
    }
}
