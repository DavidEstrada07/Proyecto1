package vampirewargame;

public final class ResultadoDanio {

    private final int danoEscudo;
    private final int danoVida;
    private final int escudoRestante;
    private final int vidaRestante;
    private final boolean destruida;

    public ResultadoDanio(int danoEscudo, int danoVida, int escudoRestante, int vidaRestante, boolean destruida) {
        this.danoEscudo = danoEscudo;
        this.danoVida = danoVida;
        this.escudoRestante = escudoRestante;
        this.vidaRestante = vidaRestante;
        this.destruida = destruida;
    }

    public int getDanoEscudo() {
        return danoEscudo;
    }

    public int getDanoVida() {
        return danoVida;
    }

    public int getDanoTotal() {
        return danoEscudo + danoVida;
    }

    public int getEscudoRestante() {
        return escudoRestante;
    }

    public int getVidaRestante() {
        return vidaRestante;
    }

    public boolean isDestruida() {
        return destruida;
    }
}
