package vampirewargame;

import java.awt.GridLayout;
import javax.swing.JOptionPane;

public class VentanaJugar extends VentanaBase {

    public VentanaJugar(SistemaJuego sistema) {
        super(sistema, "JUGAR");
        setSubtitulo("Selecciona un rival y entra al tablero");
        construirMenu();
    }

    private void construirMenu() {
        panelBotones.setLayout(new GridLayout(2, 1, 0, 18));

        BotonMenu botonNuevaPartida = crearBoton("NUEVA PARTIDA");
        BotonMenu botonVolver = crearBoton("VOLVER AL MENÚ PRINCIPAL");

        botonNuevaPartida.addActionListener(e -> nuevaPartida());
        botonVolver.addActionListener(e -> volver());

        panelBotones.add(botonNuevaPartida);
        panelBotones.add(botonVolver);
    }

    private void nuevaPartida() {
        try {
            Jugador[] oponentes = sistema.obtenerOponentesDisponibles();
            if (oponentes.length == 0) {
                JOptionPane.showMessageDialog(this, "No existe otro jugador activo disponible para iniciar una partida.");
                return;
            }

            String[] nombres = new String[oponentes.length];
            for (int i = 0; i < oponentes.length; i++) {
                nombres[i] = oponentes[i].getUsuario();
            }

            String seleccionado = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona al oponente:",
                    "Nueva partida",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nombres,
                    nombres[0]
            );

            if (seleccionado == null) {
                return;
            }

            Partida partida = sistema.nuevaPartida(seleccionado);
            new VentanaPartida(sistema, partida).setVisible(true);
            dispose();

        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volver() {
        new VentanaPrincipal(sistema).setVisible(true);
        dispose();
    }
}
