package vampirewargame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class VentanaBase extends JFrame {

    protected final SistemaJuego sistema;
    protected JPanel panelBotones;
    protected JLabel subtitulo;

    public VentanaBase(SistemaJuego sistema, String titulo) {
        this.sistema = sistema;

        setTitle("Vampire Wargame - " + titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(860, 620));
        setLocationRelativeTo(null);
        setResizable(false);

        PanelFondo fondo = new PanelFondo();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(48, 95, 48, 95));

        JLabel encabezado = Tema.crearTitulo(titulo, titulo.equalsIgnoreCase("VAMPIRE WARGAME") ? 48 : 38);
        encabezado.setAlignmentX(CENTER_ALIGNMENT);

        subtitulo = Tema.crearSubtitulo("", 17);
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);

        JPanel tarjeta = Tema.crearTarjeta();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setMaximumSize(new Dimension(560, 370));
        tarjeta.setAlignmentX(CENTER_ALIGNMENT);

        panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(8, 40, 8, 40));
        tarjeta.add(panelBotones, BorderLayout.CENTER);

        contenido.add(Box.createVerticalGlue());
        contenido.add(encabezado);
        contenido.add(Box.createRigidArea(new Dimension(0, 8)));
        contenido.add(subtitulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 32)));
        contenido.add(tarjeta);
        contenido.add(Box.createVerticalGlue());

        fondo.add(contenido, BorderLayout.CENTER);
    }

    protected void setSubtitulo(String texto) {
        subtitulo.setText(texto);
    }

    protected BotonMenu crearBoton(String texto) {
        return new BotonMenu(texto);
    }
}
