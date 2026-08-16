package vampirewargame;

import java.awt.GridLayout;

public class VentanaPrincipal extends VentanaBase {

    public VentanaPrincipal(SistemaJuego sistema) {
        super(sistema, "VAMPIRE WARGAME");
        Jugador jugador = sistema.getSesionActual();
        setSubtitulo("Jugador: " + jugador.getUsuario() + "   •   Puntos: " + jugador.getPuntos());
        construirMenu();
    }

    private void construirMenu() {
        panelBotones.setLayout(new GridLayout(4, 1, 0, 16));

        BotonMenu botonJugar = crearBoton("JUGAR VAMPIRE WARGAME");
        BotonMenu botonCuenta = crearBoton("MI CUENTA");
        BotonMenu botonReportes = crearBoton("REPORTES");
        BotonMenu botonCerrarSesion = crearBoton("CERRAR SESIÓN");

        botonJugar.addActionListener(e -> abrirVentanaJugar());
        botonCuenta.addActionListener(e -> abrirMiCuenta());
        botonReportes.addActionListener(e -> abrirReportes());
        botonCerrarSesion.addActionListener(e -> cerrarSesion());

        panelBotones.add(botonJugar);
        panelBotones.add(botonCuenta);
        panelBotones.add(botonReportes);
        panelBotones.add(botonCerrarSesion);
    }

    private void abrirVentanaJugar() {
        new VentanaJugar(sistema).setVisible(true);
        dispose();
    }

    private void abrirMiCuenta() {
        new VentanaMiCuenta(sistema).setVisible(true);
        dispose();
    }

    private void abrirReportes() {
        new VentanaReportes(sistema).setVisible(true);
        dispose();
    }

    private void cerrarSesion() {
        sistema.cerrarSesion();
        new VentanaInicio(sistema).setVisible(true);
        dispose();
    }
}
