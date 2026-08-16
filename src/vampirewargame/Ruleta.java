package vampirewargame;

import java.util.Random;

public class Ruleta {

    private final TipoPieza[] opciones;
    private final Random random;

    public Ruleta() {
        opciones = new TipoPieza[6];
        opciones[0] = TipoPieza.HOMBRE_LOBO;
        opciones[1] = TipoPieza.HOMBRE_LOBO;
        opciones[2] = TipoPieza.VAMPIRO;
        opciones[3] = TipoPieza.VAMPIRO;
        opciones[4] = TipoPieza.NECROMANTE;
        opciones[5] = TipoPieza.NECROMANTE;
        random = new Random();
    }

    public TipoPieza girar() {
        return opciones[random.nextInt(opciones.length)];
    }

    public int calcularGirosPermitidos(int piezasPrincipalesPerdidas) {
        if (piezasPrincipalesPerdidas >= 4) {
            return 3;
        }
        if (piezasPrincipalesPerdidas >= 2) {
            return 2;
        }
        return 1;
    }
}
