package vampirewargame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public final class Tema {

    public static final Color FONDO = new Color(7, 7, 9);
    public static final Color FONDO_SECUNDARIO = new Color(20, 10, 13);
    public static final Color PANEL = new Color(20, 20, 23);
    public static final Color PANEL_CLARO = new Color(31, 31, 36);
    public static final Color ROJO = new Color(112, 12, 27);
    public static final Color ROJO_HOVER = new Color(154, 23, 42);
    public static final Color ROJO_PRESIONADO = new Color(82, 7, 18);
    public static final Color BORDE = new Color(190, 43, 60);
    public static final Color DORADO = new Color(212, 174, 93);
    public static final Color TEXTO = new Color(242, 239, 232);
    public static final Color TEXTO_SECUNDARIO = new Color(174, 170, 166);

    private Tema() {
    }

    public static JLabel crearTitulo(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("Serif", Font.BOLD, tamano));
        etiqueta.setForeground(TEXTO);
        return etiqueta;
    }

    public static JLabel crearSubtitulo(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, tamano));
        etiqueta.setForeground(TEXTO_SECUNDARIO);
        return etiqueta;
    }

    public static JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(61, 42, 46), 1, true),
                BorderFactory.createEmptyBorder(24, 30, 24, 30)
        ));
        return panel;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setBackground(PANEL_CLARO);
        campo.setForeground(TEXTO);
        campo.setCaretColor(TEXTO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(260, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(82, 72, 74), 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
    }

    public static void prepararBoton(BotonMenu boton) {
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
