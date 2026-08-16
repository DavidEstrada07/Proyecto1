package vampirewargame;

public class Tablero {

    public static final int TAMANO = 6;
    private final Pieza[][] casillas;

    public Tablero() {
        casillas = new Pieza[TAMANO][TAMANO];
        inicializarPiezas();
    }

    private void inicializarPiezas() {
        colocarFilaInicial(0, ColorBando.NEGRO);
        colocarFilaInicial(TAMANO - 1, ColorBando.BLANCO);
    }

    private void colocarFilaInicial(int fila, ColorBando color) {
        casillas[fila][0] = new HombreLobo(color);
        casillas[fila][1] = new Vampiro(color);
        casillas[fila][2] = new Necromante(color);
        casillas[fila][3] = new Necromante(color);
        casillas[fila][4] = new Vampiro(color);
        casillas[fila][5] = new HombreLobo(color);
    }

    public Pieza getPieza(int fila, int columna) throws JuegoException {
        validarPosicion(fila, columna);
        return casillas[fila][columna];
    }

    public boolean estaVacia(int fila, int columna) throws JuegoException {
        validarPosicion(fila, columna);
        return casillas[fila][columna] == null;
    }

    public void moverPieza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPosicion(filaOrigen, columnaOrigen);
        validarPosicion(filaDestino, columnaDestino);

        if (casillas[filaOrigen][columnaOrigen] == null) {
            throw new JuegoException("La casilla de origen está vacía.");
        }
        if (casillas[filaDestino][columnaDestino] != null) {
            throw new JuegoException("La casilla de destino debe estar vacía para mover una pieza.");
        }

        casillas[filaDestino][columnaDestino] = casillas[filaOrigen][columnaOrigen];
        casillas[filaOrigen][columnaOrigen] = null;
    }

    public void colocarPieza(Pieza pieza, int fila, int columna) throws JuegoException {
        validarPosicion(fila, columna);
        if (pieza == null) {
            throw new JuegoException("La pieza no puede ser nula.");
        }
        if (casillas[fila][columna] != null) {
            throw new JuegoException("La casilla seleccionada ya está ocupada.");
        }
        casillas[fila][columna] = pieza;
    }

    public void eliminarPieza(int fila, int columna) throws JuegoException {
        validarPosicion(fila, columna);
        casillas[fila][columna] = null;
    }

    public boolean hayPiezaDelTipo(ColorBando color, TipoPieza tipo) {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                Pieza pieza = casillas[fila][columna];
                if (pieza != null && pieza.getColor() == color && pieza.getTipo() == tipo) {
                    return true;
                }
            }
        }
        return false;
    }

    public int contarPiezas(ColorBando color) {
        int cantidad = 0;
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                Pieza pieza = casillas[fila][columna];
                if (pieza != null && pieza.getColor() == color) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    public int contarPiezasPrincipales(ColorBando color) {
        int cantidad = 0;
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                Pieza pieza = casillas[fila][columna];
                if (pieza != null && pieza.getColor() == color && pieza.getTipo() != TipoPieza.ZOMBIE) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    public boolean caminoLibreParaDosCasillas(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPosicion(filaOrigen, columnaOrigen);
        validarPosicion(filaDestino, columnaDestino);

        int diferenciaFila = filaDestino - filaOrigen;
        int diferenciaColumna = columnaDestino - columnaOrigen;

        if (Math.max(Math.abs(diferenciaFila), Math.abs(diferenciaColumna)) != 2) {
            return true;
        }

        int pasoFila = Integer.compare(diferenciaFila, 0);
        int pasoColumna = Integer.compare(diferenciaColumna, 0);
        int filaIntermedia = filaOrigen + pasoFila;
        int columnaIntermedia = columnaOrigen + pasoColumna;

        return casillas[filaIntermedia][columnaIntermedia] == null;
    }

    public boolean sinObstruccionLanza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws JuegoException {
        validarPosicion(filaOrigen, columnaOrigen);
        validarPosicion(filaDestino, columnaDestino);

        int diferenciaFila = filaDestino - filaOrigen;
        int diferenciaColumna = columnaDestino - columnaOrigen;
        boolean horizontalDos = diferenciaFila == 0 && Math.abs(diferenciaColumna) == 2;
        boolean verticalDos = diferenciaColumna == 0 && Math.abs(diferenciaFila) == 2;

        if (!horizontalDos && !verticalDos) {
            return false;
        }

        int filaIntermedia = filaOrigen + Integer.compare(diferenciaFila, 0);
        int columnaIntermedia = columnaOrigen + Integer.compare(diferenciaColumna, 0);
        return casillas[filaIntermedia][columnaIntermedia] == null;
    }

    public void validarPosicion(int fila, int columna) throws JuegoException {
        if (fila < 0 || fila >= TAMANO || columna < 0 || columna >= TAMANO) {
            throw new JuegoException("La posición debe estar dentro del tablero 6x6.");
        }
    }
}
