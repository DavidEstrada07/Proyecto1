package vampirewargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class VentanaPartida extends JFrame {

    private final SistemaJuego sistema;
    private final Partida partida;
    private final BotonCasilla[][] botonesTablero;
    private final PanelRuleta panelRuleta;

    private final JLabel etiquetaTurno;
    private final JLabel etiquetaTipo;
    private final JLabel etiquetaGiros;
    private final JLabel etiquetaInstruccion;
    private final JTextArea areaEventos;
    private final BotonMenu botonGirar;
    private final PanelDescripcionPieza panelDescripcionPieza;

    private int filaOrigen;
    private int columnaOrigen;
    private boolean animandoRuleta;

    public VentanaPartida(SistemaJuego sistema, Partida partida) {
        this.sistema = sistema;
        this.partida = partida;
        botonesTablero = new BotonCasilla[Tablero.TAMANO][Tablero.TAMANO];
        panelRuleta = new PanelRuleta();
        filaOrigen = -1;
        columnaOrigen = -1;
        animandoRuleta = false;

        setTitle("Vampire Wargame - Partida");
        setSize(1320, 1300);
        setMinimumSize(new Dimension(1200, 820));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        PanelFondo fondo = new PanelFondo();
        fondo.setLayout(new BorderLayout(18, 18));
        fondo.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        setContentPane(fondo);

        JPanel cabecera = crearCabecera();
        fondo.add(cabecera, BorderLayout.NORTH);

        JPanel zonaCentral = new JPanel(new BorderLayout(20, 0));
        zonaCentral.setOpaque(false);
        zonaCentral.add(crearPanelTablero(), BorderLayout.CENTER);

        JPanel lateral = new JPanel();
        lateral.setOpaque(false);
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setPreferredSize(new Dimension(360, 0));

        JPanel tarjetaRuleta = Tema.crearTarjeta();
        tarjetaRuleta.setLayout(new BoxLayout(tarjetaRuleta, BoxLayout.Y_AXIS));
        tarjetaRuleta.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaRuleta.add(panelRuleta);
        tarjetaRuleta.add(Box.createRigidArea(new Dimension(0, 8)));

        botonGirar = new BotonMenu("GIRAR RULETA");
        botonGirar.setMaximumSize(new Dimension(290, 52));
        botonGirar.setAlignmentX(CENTER_ALIGNMENT);
        botonGirar.addActionListener(e -> girarRuleta());
        tarjetaRuleta.add(botonGirar);

        etiquetaTipo = Tema.crearSubtitulo("Tipo habilitado: ninguno", 14);
        etiquetaTipo.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaRuleta.add(Box.createRigidArea(new Dimension(0, 10)));
        tarjetaRuleta.add(etiquetaTipo);

        etiquetaGiros = Tema.crearSubtitulo("Giros: 0/1", 13);
        etiquetaGiros.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaRuleta.add(Box.createRigidArea(new Dimension(0, 4)));
        tarjetaRuleta.add(etiquetaGiros);

        lateral.add(tarjetaRuleta);
        lateral.add(Box.createRigidArea(new Dimension(0, 12)));

        panelDescripcionPieza = new PanelDescripcionPieza();
        panelDescripcionPieza.setAlignmentX(CENTER_ALIGNMENT);
        lateral.add(panelDescripcionPieza);
        lateral.add(Box.createRigidArea(new Dimension(0, 12)));

        BotonMenu botonRetirarse = new BotonMenu("RETIRARSE DE LA PARTIDA");
        botonRetirarse.setMaximumSize(new Dimension(350, 50));
        botonRetirarse.setAlignmentX(CENTER_ALIGNMENT);
        botonRetirarse.addActionListener(e -> retirarse());
        lateral.add(botonRetirarse);

        zonaCentral.add(lateral, BorderLayout.EAST);
        fondo.add(zonaCentral, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout(12, 8));
        pie.setOpaque(false);

        etiquetaInstruccion = Tema.crearSubtitulo("Gira la ruleta para comenzar el turno.", 14);
        etiquetaInstruccion.setHorizontalAlignment(SwingConstants.LEFT);
        pie.add(etiquetaInstruccion, BorderLayout.NORTH);

        areaEventos = new JTextArea(5, 20);
        areaEventos.setEditable(false);
        areaEventos.setLineWrap(true);
        areaEventos.setWrapStyleWord(true);
        areaEventos.setBackground(new Color(17, 17, 20));
        areaEventos.setForeground(Tema.TEXTO_SECUNDARIO);
        areaEventos.setCaretColor(Tema.TEXTO);
        areaEventos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        areaEventos.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane scroll = new JScrollPane(areaEventos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(67, 47, 52), 1));
        scroll.setPreferredSize(new Dimension(0, 92));
        pie.add(scroll, BorderLayout.CENTER);

        fondo.add(pie, BorderLayout.SOUTH);

        etiquetaTurno = (JLabel) cabecera.getClientProperty("etiquetaTurno");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                retirarse();
            }
        });

        registrarEvento("Partida iniciada. Las piezas BLANCAS comienzan.");
        actualizarVista();
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(18, 0));
        cabecera.setOpaque(false);

        JLabel titulo = Tema.crearTitulo("VAMPIRE WARGAME", 32);
        titulo.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel jugadores = Tema.crearSubtitulo(
                "BLANCO: " + partida.getJugador1().getUsuario()
                + "    VS    NEGRO: " + partida.getJugador2().getUsuario(),
                14
        );
        jugadores.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel izquierdo = new JPanel();
        izquierdo.setOpaque(false);
        izquierdo.setLayout(new BoxLayout(izquierdo, BoxLayout.Y_AXIS));
        izquierdo.add(titulo);
        izquierdo.add(Box.createRigidArea(new Dimension(0, 3)));
        izquierdo.add(jugadores);

        JLabel turno = Tema.crearTitulo("", 20);
        turno.setHorizontalAlignment(SwingConstants.RIGHT);
        cabecera.add(izquierdo, BorderLayout.WEST);
        cabecera.add(turno, BorderLayout.EAST);
        cabecera.putClientProperty("etiquetaTurno", turno);

        return cabecera;
    }

    private JPanel crearPanelTablero() {
        JPanel tarjeta = Tema.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(0, 7));

        JLabel jugadorNegro = Tema.crearSubtitulo("▲  NEGRO — " + partida.getJugador2().getUsuario(), 13);
        jugadorNegro.setForeground(new Color(105, 31, 40));
        tarjeta.add(jugadorNegro, BorderLayout.NORTH);

        JPanel tablero = new JPanel(new GridLayout(Tablero.TAMANO, Tablero.TAMANO, 4, 4));
        tablero.setOpaque(false);

        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                final int f = fila;
                final int c = columna;
                BotonCasilla boton = new BotonCasilla(fila, columna);
                boton.addActionListener(e -> seleccionarCasilla(f, c));
                botonesTablero[fila][columna] = boton;
                tablero.add(boton);
            }
        }

        tarjeta.add(tablero, BorderLayout.CENTER);

        JLabel jugadorBlanco = Tema.crearSubtitulo("▼  BLANCO — " + partida.getJugador1().getUsuario(), 13);
        jugadorBlanco.setForeground(new Color(225, 215, 189));
        tarjeta.add(jugadorBlanco, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void girarRuleta() {
        if (animandoRuleta || panelRuleta.isAnimando()) {
            return;
        }

        try {
            final ResultadoRuleta resultado = partida.girarRuleta();
            animandoRuleta = true;
            botonGirar.setEnabled(false);
            limpiarSeleccion();
            etiquetaInstruccion.setText("La ruleta está girando...");

            panelRuleta.animarHasta(resultado.getTipoSeleccionado(), () -> {
                animandoRuleta = false;
                registrarEvento(resultado.getMensaje());

                if (resultado.isPuedeJugar()) {
                    etiquetaInstruccion.setText("Selecciona una pieza " + resultado.getTipoSeleccionado().getNombre() + " de tu bando y luego el destino.");
                } else if (resultado.isPuedeVolverAGirar()) {
                    etiquetaInstruccion.setText("Ese tipo ya no está disponible. Puedes volver a girar la ruleta.");
                } else {
                    etiquetaInstruccion.setText("El turno cambió. El nuevo jugador debe girar la ruleta.");
                }

                actualizarVista();
            });

        } catch (JuegoException ex) {
            mostrarError(ex.getMessage());
            actualizarVista();
        }
    }

    private void seleccionarCasilla(int fila, int columna) {
        if (animandoRuleta || !partida.isActiva()) {
            return;
        }

        if (partida.getTipoHabilitado() == null) {
            mostrarError("Primero debes girar la ruleta y obtener un tipo de pieza válido.");
            return;
        }

        try {
            Pieza pieza = partida.getTablero().getPieza(fila, columna);

            if (filaOrigen < 0) {
                seleccionarOrigen(fila, columna, pieza);
                return;
            }

            if (fila == filaOrigen && columna == columnaOrigen) {
                limpiarSeleccion();
                etiquetaInstruccion.setText("Selección cancelada. Elige nuevamente la pieza de origen.");
                actualizarTablero();
                return;
            }

            Pieza atacante = partida.getTablero().getPieza(filaOrigen, columnaOrigen);

            if (pieza == null) {
                ejecutarAccionCasillaVacia(atacante, fila, columna);
            } else if (pieza.getColor() == partida.getTurnoActual()) {
                throw new JuegoException("La casilla destino contiene una pieza propia.");
            } else {
                ejecutarAtaque(atacante, fila, columna);
            }

        } catch (JuegoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void seleccionarOrigen(int fila, int columna, Pieza pieza) throws JuegoException {
        if (pieza == null) {
            throw new JuegoException("La casilla seleccionada está vacía.");
        }
        if (pieza.getColor() != partida.getTurnoActual()) {
            throw new JuegoException("Debes seleccionar una pieza del jugador que tiene el turno.");
        }
        if (pieza.getTipo() != partida.getTipoHabilitado()) {
            throw new JuegoException("La ruleta solo permite seleccionar una pieza de tipo " + partida.getTipoHabilitado().getNombre() + ".");
        }

        filaOrigen = fila;
        columnaOrigen = columna;
        panelDescripcionPieza.mostrarPieza(pieza);
        etiquetaInstruccion.setText("Origen seleccionado: " + pieza.getTipo().getNombre() + ". Ahora selecciona la casilla destino.");
        actualizarTablero();
    }

    private void ejecutarAccionCasillaVacia(Pieza atacante, int filaDestino, int columnaDestino) throws JuegoException {
        String mensaje;

        if (atacante.getTipo() == TipoPieza.NECROMANTE) {
            String[] opciones = {"Mover", "Invocar Zombie", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(
                    this,
                    "La casilla está vacía. ¿Qué deseas hacer con el Necrómante?",
                    "Acción del Necrómante",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (opcion == 0) {
                mensaje = partida.moverPieza(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
            } else if (opcion == 1) {
                mensaje = partida.invocarZombie(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
            } else {
                return;
            }
        } else {
            mensaje = partida.moverPieza(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
        }

        finalizarAccionVisual(mensaje);
    }

    private void ejecutarAtaque(Pieza atacante, int filaDestino, int columnaDestino) throws JuegoException {
        if (atacante.getTipo() == TipoPieza.VAMPIRO) {
            String[] opciones = {"Ataque normal", "Absorber sangre", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, "Elige el tipo de ataque:", "Vampiro", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (opcion == 0) {
                finalizarAccionVisual(partida.atacarNormal(filaOrigen, columnaOrigen, filaDestino, columnaDestino));
            } else if (opcion == 1) {
                finalizarAccionVisual(partida.absorberSangre(filaOrigen, columnaOrigen, filaDestino, columnaDestino));
            }
            return;
        }

        if (atacante.getTipo() == TipoPieza.NECROMANTE) {
            String[] opciones = {"Ataque normal", "Lanzar lanza", "Ataque por Zombie", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, "Elige el ataque del Necrómante:", "Necrómante", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

            if (opcion == 0) {
                finalizarAccionVisual(partida.atacarNormal(filaOrigen, columnaOrigen, filaDestino, columnaDestino));
            } else if (opcion == 1) {
                finalizarAccionVisual(partida.lanzarLanza(filaOrigen, columnaOrigen, filaDestino, columnaDestino));
            } else if (opcion == 2) {
                atacarPorZombie(filaDestino, columnaDestino);
            }
            return;
        }

        finalizarAccionVisual(partida.atacarNormal(filaOrigen, columnaOrigen, filaDestino, columnaDestino));
    }

    private void atacarPorZombie(int filaObjetivo, int columnaObjetivo) throws JuegoException {
        int[][] coordenadas = new int[Tablero.TAMANO * Tablero.TAMANO][2];
        String[] textosTemporales = new String[Tablero.TAMANO * Tablero.TAMANO];
        int cantidad = 0;

        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                Pieza pieza = partida.getTablero().getPieza(fila, columna);
                if (pieza != null && pieza.getTipo() == TipoPieza.ZOMBIE && pieza.getColor() == partida.getTurnoActual()) {
                    if (pieza.esAdyacente(fila, columna, filaObjetivo, columnaObjetivo)) {
                        coordenadas[cantidad][0] = fila;
                        coordenadas[cantidad][1] = columna;
                        textosTemporales[cantidad] = "Zombie en [" + fila + ", " + columna + "]";
                        cantidad++;
                    }
                }
            }
        }

        if (cantidad == 0) {
            throw new JuegoException("No tienes un Zombie propio adyacente a ese enemigo.");
        }

        String[] opciones = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            opciones[i] = textosTemporales[i];
        }

        String seleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el Zombie que ejecutará el ataque:",
                "Ataque por Zombie",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return;
        }

        int indice = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                indice = i;
                break;
            }
        }

        String mensaje = partida.atacarPorZombie(
                filaOrigen,
                columnaOrigen,
                coordenadas[indice][0],
                coordenadas[indice][1],
                filaObjetivo,
                columnaObjetivo
        );

        finalizarAccionVisual(mensaje);
    }

    private void finalizarAccionVisual(String mensaje) {
        registrarEvento(mensaje);
        limpiarSeleccion();
        actualizarVista();

        if (!partida.isActiva()) {
            registrarEvento(partida.getMensajeFinal());
            JOptionPane.showMessageDialog(this, partida.getMensajeFinal(), "Partida finalizada", JOptionPane.INFORMATION_MESSAGE);
            volverAlMenu();
        } else {
            etiquetaInstruccion.setText("Turno completado. El siguiente jugador debe girar la ruleta.");
        }
    }

    private void retirarse() {
        if (!partida.isActiva()) {
            volverAlMenu();
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas retirarte? El jugador contrario será declarado ganador.",
                "Retirarse de la partida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String mensaje = partida.retirarse(partida.getJugadorTurnoActual());
            registrarEvento(mensaje);
            JOptionPane.showMessageDialog(this, mensaje, "Partida finalizada", JOptionPane.INFORMATION_MESSAGE);
            volverAlMenu();
        } catch (JuegoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void volverAlMenu() {
        new VentanaPrincipal(sistema).setVisible(true);
        dispose();
    }

    private void limpiarSeleccion() {
        filaOrigen = -1;
        columnaOrigen = -1;
        panelDescripcionPieza.ocultar();
    }

    private void actualizarVista() {
        actualizarTablero();

        Jugador turno = partida.getJugadorTurnoActual();
        etiquetaTurno.setText("Turno: " + turno.getUsuario() + " · " + partida.getTurnoActual());

        TipoPieza habilitado = partida.getTipoHabilitado();
        etiquetaTipo.setText("Tipo habilitado: " + (habilitado == null ? "ninguno" : habilitado.getNombre()));
        etiquetaGiros.setText("Giros: " + partida.getGirosUsados() + "/" + partida.getGirosPermitidos());

        boolean puedeGirar = partida.isActiva()
                && !animandoRuleta
                && habilitado == null
                && partida.getGirosUsados() < partida.getGirosPermitidos();
        botonGirar.setEnabled(puedeGirar);
    }

    private void actualizarTablero() {
        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                try {
                    Pieza pieza = partida.getTablero().getPieza(fila, columna);
                    boolean seleccionada = fila == filaOrigen && columna == columnaOrigen;
                    botonesTablero[fila][columna].actualizar(pieza, seleccionada);
                } catch (JuegoException ex) {
                    botonesTablero[fila][columna].actualizar(null, false);
                }
            }
        }
    }

    private void registrarEvento(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return;
        }
        areaEventos.append("• " + texto + "\n");
        areaEventos.setCaretPosition(areaEventos.getDocument().getLength());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Acción no válida", JOptionPane.WARNING_MESSAGE);
    }
}
