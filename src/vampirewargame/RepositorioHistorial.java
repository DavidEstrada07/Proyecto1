package vampirewargame;

public class RepositorioHistorial implements InterfazHistorial {

    private RegistroPartida[] registros;
    private int cantidad;

    public RepositorioHistorial() {
        registros = new RegistroPartida[10];
        cantidad = 0;
    }

    @Override
    public void agregarRegistro(RegistroPartida registro) throws JuegoException {
        if (registro == null) {
            throw new JuegoException("El registro de partida no puede ser nulo.");
        }
        ampliarArregloSiEsNecesario();
        registros[cantidad] = registro;
        cantidad++;
    }

    private void ampliarArregloSiEsNecesario() {
        if (cantidad < registros.length) {
            return;
        }

        RegistroPartida[] nuevo = new RegistroPartida[registros.length * 2];
        for (int i = 0; i < registros.length; i++) {
            nuevo[i] = registros[i];
        }
        registros = nuevo;
    }

    @Override
    public RegistroPartida[] obtenerHistorialJugador(String usuario) {
        int total = contarRegistrosJugador(usuario, cantidad - 1);
        RegistroPartida[] resultado = new RegistroPartida[total];
        llenarHistorialReciente(usuario, cantidad - 1, resultado, 0);
        return resultado;
    }

    private int contarRegistrosJugador(String usuario, int indice) {
        if (indice < 0) {
            return 0;
        }
        int suma = registros[indice].participo(usuario) ? 1 : 0;
        return suma + contarRegistrosJugador(usuario, indice - 1);
    }

    private int llenarHistorialReciente(String usuario, int indice, RegistroPartida[] resultado, int posicion) {
        if (indice < 0) {
            return posicion;
        }
        if (registros[indice].participo(usuario)) {
            resultado[posicion] = registros[indice];
            return llenarHistorialReciente(usuario, indice - 1, resultado, posicion + 1);
        }
        return llenarHistorialReciente(usuario, indice - 1, resultado, posicion);
    }

    @Override
    public int cantidad() {
        return cantidad;
    }
}
