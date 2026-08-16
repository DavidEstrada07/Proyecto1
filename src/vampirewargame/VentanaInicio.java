package vampirewargame;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaInicio extends VentanaBase {

    public VentanaInicio(SistemaJuego sistema) {
        super(sistema, "VAMPIRE WARGAME");
        setSubtitulo("Elije una opcion:");
        construirMenu();
    }

    private void construirMenu() {
        panelBotones.setLayout(new GridLayout(3, 1, 0, 18));

        BotonMenu botonIniciar = crearBoton("INICIAR SESIÓN");
        BotonMenu botonCrear = crearBoton("CREAR JUGADOR");
        BotonMenu botonSalir = crearBoton("SALIR");

        botonIniciar.addActionListener(e -> iniciarSesion());
        botonCrear.addActionListener(e -> crearJugador());
        botonSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(botonIniciar);
        panelBotones.add(botonCrear);
        panelBotones.add(botonSalir);
    }

    private void iniciarSesion() {
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContrasena = new JPasswordField();

        JPanel panel = crearPanelFormulario();
        panel.add(crearEtiqueta("Usuario"));
        panel.add(campoUsuario);
        panel.add(crearEtiqueta("Contraseña"));
        panel.add(campoContrasena);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Iniciar sesión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            sistema.iniciarSesion(campoUsuario.getText(), new String(campoContrasena.getPassword()));
            abrirMenuPrincipal();
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo iniciar sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearJugador() {
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContrasena = new JPasswordField();

        JPanel panel = crearPanelFormulario();
        panel.add(crearEtiqueta("Nuevo usuario"));
        panel.add(campoUsuario);
        panel.add(crearEtiqueta("Contraseña de 5 caracteres"));
        panel.add(campoContrasena);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Crear jugador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            sistema.crearJugador(campoUsuario.getText(), new String(campoContrasena.getPassword()));
            JOptionPane.showMessageDialog(this, "Jugador creado correctamente. La sesión se inició automáticamente.");
            abrirMenuPrincipal();
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo crear el jugador", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelFormulario() {
        return new JPanel(new GridLayout(4, 1, 6, 6));

    }

    private JLabel crearEtiqueta(String texto) {
        return new JLabel(texto);

    }

    private void abrirMenuPrincipal() {
        new VentanaPrincipal(sistema).setVisible(true);
        dispose();
    }
}
