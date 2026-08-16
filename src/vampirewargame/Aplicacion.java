package vampirewargame;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Aplicacion {

    private Aplicacion() {
    }

    public static void iniciar() {
        configurarApariencia();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SistemaJuego sistema = new SistemaJuego();
                new VentanaInicio(sistema).setVisible(true);
            }
        });
    }

    private static void configurarApariencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            // Si no se puede cambiar la apariencia, Swing utiliza la disponible.
        }

        UIManager.put("OptionPane.background", new Color(235, 235, 235));
        UIManager.put("Panel.background", new Color(235, 235, 235));
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 13));
    }
}
