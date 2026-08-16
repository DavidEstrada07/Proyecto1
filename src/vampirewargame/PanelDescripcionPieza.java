package vampirewargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PanelDescripcionPieza extends JPanel {

    private final JLabel titulo;
    private final JLabel estado;
    private final JTextArea descripcion;

    public PanelDescripcionPieza() {
        setLayout(new BorderLayout(0, 8));
        setBackground(Tema.PANEL);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(86, 58, 64), 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        setMaximumSize(new Dimension(350, 210));
        setPreferredSize(new Dimension(350, 190));
        setVisible(false);

        titulo = new JLabel("", SwingConstants.LEFT);
        titulo.setFont(new Font("Serif", Font.BOLD, 20));
        titulo.setForeground(Tema.DORADO);

        estado = new JLabel("", SwingConstants.LEFT);
        estado.setFont(new Font("SansSerif", Font.BOLD, 12));
        estado.setForeground(Tema.TEXTO_SECUNDARIO);

        JPanel encabezado = new JPanel(new BorderLayout(0, 4));
        encabezado.setOpaque(false);
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(estado, BorderLayout.SOUTH);

        descripcion = new JTextArea();
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descripcion.setForeground(Tema.TEXTO);
        descripcion.setBorder(null);
        descripcion.setFocusable(false);

        add(encabezado, BorderLayout.NORTH);
        add(descripcion, BorderLayout.CENTER);
    }

    public void mostrarPieza(Pieza pieza) {
        if (pieza == null) {
            ocultar();
            return;
        }

        titulo.setText(pieza.getTipo().getNombre().toUpperCase());
        estado.setText(
                "Ataque: " + pieza.getAtaque()
                + "   |   Vida: " + pieza.getVida() + "/" + pieza.getVidaMaxima()
                + "   |   Escudo: " + pieza.getEscudo()
        );
        descripcion.setText(crearDescripcion(pieza));
        descripcion.setCaretPosition(0);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void ocultar() {
        setVisible(false);
        titulo.setText("");
        estado.setText("");
        descripcion.setText("");
        revalidate();
        repaint();
    }

    private String crearDescripcion(Pieza pieza) {
        if (pieza.getTipo() == TipoPieza.VAMPIRO) {
            return "Movimiento: 1 casilla en horizontal, vertical o diagonal.\n"
                    + "Ataque normal: quita 3 puntos. El escudo enemigo recibe el daño primero.\n"
                    + "Especial: Absorción de sangre. Quita 1 punto al enemigo y el Vampiro recupera 1 punto de vida.";
        }

        if (pieza.getTipo() == TipoPieza.HOMBRE_LOBO) {
            return "Movimiento: hasta 2 casillas vacías en horizontal, vertical o diagonal.\n"
                    + "Ataque normal: quita 5 puntos. El escudo enemigo recibe el daño primero.\n"
                    + "Especial: su ventaja es el desplazamiento extendido de hasta 2 casillas.";
        }

        if (pieza.getTipo() == TipoPieza.NECROMANTE) {
            return "Movimiento: 1 casilla en horizontal, vertical o diagonal.\n"
                    + "Ataque normal: quita 4 puntos.\n"
                    + "Lanza: quita 2 puntos directamente a la vida, ignora el escudo y alcanza exactamente 2 casillas en horizontal o vertical sin obstrucciones.\n"
                    + "También puede invocar Zombies en casillas vacías y ordenar a un Zombie propio atacar por 1 punto.";
        }

        return "Movimiento: no puede desplazarse por sí mismo.\n"
                + "Ataque: quita 1 punto, pero solo puede atacar cuando un Necrómante propio se lo ordena.\n"
                + "No aparece como resultado de la ruleta.";
    }
}
