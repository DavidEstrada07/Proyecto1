package vampirewargame;

public abstract class Pieza {

    protected TipoPieza tipo;
    protected ColorBando color;
    protected int ataque;
    protected int vida;
    protected int vidaMaxima;
    protected int escudo;

    public Pieza(TipoPieza tipo, ColorBando color, int ataque, int vida, int escudo) {
        this.tipo = tipo;
        this.color = color;
        this.ataque = ataque;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.escudo = escudo;
    }

    public abstract boolean movimientoValido(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino);

    public abstract String getHabilidadEspecial();

    public final ResultadoDanio atacarNormal(Pieza objetivo) throws JuegoException {
        if (objetivo == null) {
            throw new JuegoException("No hay una pieza para atacar.");
        }
        if (objetivo.getColor() == color) {
            throw new JuegoException("No puedes atacar una pieza de tu mismo bando.");
        }
        return objetivo.recibirDanio(ataque);
    }

    public final ResultadoDanio recibirDanio(int cantidad) throws JuegoException {
        if (cantidad <= 0) {
            throw new JuegoException("El daño debe ser mayor que cero.");
        }

        int escudoAntes = escudo;
        int vidaAntes = vida;
        int restante = cantidad;

        if (escudo > 0) {
            int absorbido = Math.min(escudo, restante);
            escudo -= absorbido;
            restante -= absorbido;
        }

        if (restante > 0) {
            vida -= restante;
            if (vida < 0) {
                vida = 0;
            }
        }

        return new ResultadoDanio(escudoAntes - escudo, vidaAntes - vida, escudo, vida, estaDestruida());
    }

    public final ResultadoDanio recibirDanioDirectoVida(int cantidad) throws JuegoException {
        if (cantidad <= 0) {
            throw new JuegoException("El daño debe ser mayor que cero.");
        }

        int vidaAntes = vida;
        vida -= cantidad;
        if (vida < 0) {
            vida = 0;
        }

        return new ResultadoDanio(0, vidaAntes - vida, escudo, vida, estaDestruida());
    }

    public final boolean estaDestruida() {
        return vida <= 0;
    }

    public final boolean esAdyacente(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        int diferenciaFila = Math.abs(filaDestino - filaOrigen);
        int diferenciaColumna = Math.abs(columnaDestino - columnaOrigen);
        return diferenciaFila <= 1 && diferenciaColumna <= 1 && (diferenciaFila != 0 || diferenciaColumna != 0);
    }

    public TipoPieza getTipo() {
        return tipo;
    }

    public ColorBando getColor() {
        return color;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getEscudo() {
        return escudo;
    }

    @Override
    public String toString() {
        return tipo.getNombre() + " " + color + " | ataque=" + ataque + ", vida=" + vida + ", escudo=" + escudo;
    }
}
