package vampirewargame;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

public class BotonCasilla extends JButton {

    private final int fila;
    private final int columna;
    private Pieza pieza;
    private boolean seleccionada;

    public BotonCasilla(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        setPreferredSize(new Dimension(90, 92));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setRolloverEnabled(true);
    }

    public void actualizar(Pieza pieza, boolean seleccionada) {
        this.pieza = pieza;
        this.seleccionada = seleccionada;
        setToolTipText(null);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colorCasilla = (fila + columna) % 2 == 0
                ? new Color(45, 35, 38)
                : new Color(24, 24, 28);

        if (getModel().isRollover()) {
            colorCasilla = (fila + columna) % 2 == 0
                    ? new Color(62, 45, 49)
                    : new Color(37, 37, 43);
        }

        g2.setColor(colorCasilla);
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);

        g2.setColor(new Color(90, 66, 72));
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);

        if (seleccionada) {
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(Tema.DORADO);
            g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 12, 12);
        }

        if (pieza != null) {
            int diametro = Math.min(getWidth(), getHeight()) - 26;
            int x = (getWidth() - diametro) / 2;
            int y = 7;

            Color aroExterior = pieza.getColor() == ColorBando.BLANCO
                    ? new Color(221, 201, 141)
                    : new Color(103, 35, 35);

            Color aroInterior = pieza.getColor() == ColorBando.BLANCO
                    ? new Color(240, 232, 216)
                    : new Color(56, 12, 19);

            g2.setColor(new Color(0, 0, 0, 85));
            g2.fillOval(x + 3, y + 4, diametro, diametro);

            g2.setColor(aroInterior);
            g2.fillOval(x, y, diametro, diametro);

            BufferedImage imagen = RecursosImagenes.getImagenPieza(pieza.getTipo());

            if (imagen != null) {
                Shape clipAnterior = g2.getClip();
                Shape clip = new Ellipse2D.Double(x + 3, y + 3, diametro - 6, diametro - 6);
                g2.setClip(clip);
                g2.drawImage(imagen, x + 3, y + 3, diametro - 6, diametro - 6, null);

                if (pieza.getColor() == ColorBando.NEGRO) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
                    g2.setColor(new Color(65, 0, 8));
                    g2.fillOval(x + 3, y + 3, diametro - 6, diametro - 6);
                    g2.setComposite(AlphaComposite.SrcOver);
                }

                g2.setClip(clipAnterior);
            } else {
                g2.setColor(new Color(18, 18, 21));
                g2.fillOval(x + 3, y + 3, diametro - 6, diametro - 6);
            }

            g2.setStroke(new BasicStroke(2.3f));
            g2.setColor(aroExterior);
            g2.drawOval(x, y, diametro, diametro);

            String datos = "A" + pieza.getAtaque() + "  V" + pieza.getVida() + "  E" + pieza.getEscudo();
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fmDatos = g2.getFontMetrics();
            g2.setColor(Tema.TEXTO_SECUNDARIO);
            g2.drawString(datos, (getWidth() - fmDatos.stringWidth(datos)) / 2, getHeight() - 8);
        }

        g2.dispose();
    }
}
