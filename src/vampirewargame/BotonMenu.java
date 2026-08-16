package vampirewargame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class BotonMenu extends JButton {

    private int arco;

    public BotonMenu(String texto) {
        super(texto);
        arco = 18;
        setFont(new Font("SansSerif", Font.BOLD, 15));
        setForeground(Tema.TEXTO);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        setPreferredSize(new Dimension(330, 56));
        setRolloverEnabled(true);
        Tema.prepararBoton(this);
    }

    public void setArco(int arco) {
        this.arco = arco;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(Tema.ROJO_PRESIONADO);
        } else if (getModel().isRollover()) {
            g2.setColor(Tema.ROJO_HOVER);
        } else {
            g2.setColor(Tema.ROJO);
        }

        g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arco, arco);
        g2.setColor(getModel().isRollover() ? Tema.DORADO : Tema.BORDE);
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arco, arco);
        g2.dispose();

        super.paintComponent(g);
    }
}
