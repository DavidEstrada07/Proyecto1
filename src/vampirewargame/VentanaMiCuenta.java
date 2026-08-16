package vampirewargame;

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class VentanaMiCuenta extends VentanaBase {

    public VentanaMiCuenta(SistemaJuego sistema) {
        super(sistema, "MI CUENTA");
        setSubtitulo("Consulta o administra la cuenta del jugador activo");
        construirMenu();
    }

    private void construirMenu() {
        panelBotones.setLayout(new GridLayout(4, 1, 0, 14));

        BotonMenu botonInformacion = crearBoton("VER MI INFORMACIÓN");
        BotonMenu botonContrasena = crearBoton("CAMBIAR CONTRASEÑA");
        BotonMenu botonCerrarCuenta = crearBoton("CERRAR MI CUENTA");
        BotonMenu botonVolver = crearBoton("VOLVER AL MENÚ PRINCIPAL");

        botonInformacion.addActionListener(e -> verInformacion());
        botonContrasena.addActionListener(e -> cambiarContrasena());
        botonCerrarCuenta.addActionListener(e -> cerrarCuenta());
        botonVolver.addActionListener(e -> volver());

        panelBotones.add(botonInformacion);
        panelBotones.add(botonContrasena);
        panelBotones.add(botonCerrarCuenta);
        panelBotones.add(botonVolver);
    }

    private void verInformacion() {
        Jugador jugador = sistema.getSesionActual();
        if (jugador == null) {
            JOptionPane.showMessageDialog(this, "No hay sesión activa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String texto = "Usuario: " + jugador.getUsuario()
                + "\nPuntos: " + jugador.getPuntos()
                + "\nFecha de ingreso: " + jugador.getFechaIngresoTexto()
                + "\nCuenta activa: " + (jugador.isActivo() ? "Sí" : "No");

        JOptionPane.showMessageDialog(this, texto, "Mi información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarContrasena() {
        JPasswordField campo = new JPasswordField();
        Tema.estilizarCampo(campo);
        int opcion = JOptionPane.showConfirmDialog(this, campo, "Nueva contraseña de 5 caracteres", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            sistema.cambiarContrasena(new String(campo.getPassword()));
            JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.");
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarCuenta() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas cerrar tu cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            sistema.cerrarCuenta();
            JOptionPane.showMessageDialog(this, "La cuenta fue cerrada.");
            new VentanaInicio(sistema).setVisible(true);
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
