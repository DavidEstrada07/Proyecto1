package vampirewargame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaReportes extends VentanaBase {

    public VentanaReportes(SistemaJuego sistema) {
        super(sistema, "REPORTES");
        setSubtitulo("Consulta el ranking y las partidas guardadas durante esta ejecución");
        construirMenu();
    }

    private void construirMenu() {
        panelBotones.setLayout(new GridLayout(3, 1, 0, 18));

        BotonMenu botonRanking = crearBoton("RANKING DE JUGADORES");
        BotonMenu botonHistorial = crearBoton("HISTORIAL DE MIS ÚLTIMOS JUEGOS");
        BotonMenu botonVolver = crearBoton("VOLVER AL MENÚ PRINCIPAL");

        botonRanking.addActionListener(e -> mostrarRanking());
        botonHistorial.addActionListener(e -> mostrarHistorial());
        botonVolver.addActionListener(e -> volver());

        panelBotones.add(botonRanking);
        panelBotones.add(botonHistorial);
        panelBotones.add(botonVolver);
    }

    private void mostrarRanking() {
        try {
            Jugador[] ranking = sistema.obtenerRanking();
            String texto = "POSICIÓN    USUARIO                 PUNTOS\n";
            texto += "----------------------------------------------\n";
            for (int i = 0; i < ranking.length; i++) {
                texto += String.format("%-11d %-23s %d%n", i + 1, ranking[i].getUsuario(), ranking[i].getPuntos());
            }
            if (ranking.length == 0) {
                texto += "No hay jugadores activos.";
            }
            mostrarTexto("Ranking de jugadores", texto);
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarHistorial() {
        try {
            RegistroPartida[] registros = sistema.obtenerMiHistorial();
            String texto = "";
            for (int i = 0; i < registros.length; i++) {
                texto += registros[i].toString() + "\n\n";
            }
            if (registros.length == 0) {
                texto = "Todavía no hay partidas finalizadas para este jugador.";
            }
            mostrarTexto("Historial de mis últimos juegos", texto);
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto, 15, 48);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(new Color(24, 24, 28));
        area.setForeground(Tema.TEXTO);
        area.setCaretColor(Tema.TEXTO);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE, 1));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void volver() {
        new VentanaPrincipal(sistema).setVisible(true);
        dispose();
    }
}
