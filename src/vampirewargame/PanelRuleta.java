package vampirewargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PanelRuleta extends JPanel {

    private final TipoPieza[] sectores;
    private double rotacion;
    private boolean animando;
    private TipoPieza ultimoResultado;

    public PanelRuleta() {
        sectores = new TipoPieza[]{
            TipoPieza.HOMBRE_LOBO,
            TipoPieza.VAMPIRO,
            TipoPieza.NECROMANTE,
            TipoPieza.HOMBRE_LOBO,
            TipoPieza.VAMPIRO,
            TipoPieza.NECROMANTE
        };
        rotacion = 0;
        animando = false;
        ultimoResultado = null;
        setOpaque(false);
        setPreferredSize(new Dimension(315, 315));
        setMinimumSize(new Dimension(315, 315));
    }

    public boolean isAnimando() {
        return animando;
    }

    public void animarHasta(TipoPieza resultado, Runnable alFinalizar) {
        if (animando || resultado == null) {
            return;
        }

        int[] coincidencias = new int[2];
        int cantidad = 0;
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i] == resultado) {
                coincidencias[cantidad] = i;
                cantidad++;
            }
        }

        int indiceObjetivo = coincidencias[(int) (Math.random() * cantidad)];
        double objetivoModulo = (360.0 - indiceObjetivo * 60.0) % 360.0;
        double actualModulo = ((rotacion % 360.0) + 360.0) % 360.0;
        double diferencia = objetivoModulo - actualModulo;
        if (diferencia < 0) {
            diferencia += 360.0;
        }

        final double inicio = rotacion;
        final double recorrido = 4 * 360.0 + diferencia;
        final long tiempoInicio = System.currentTimeMillis();
        final int duracion = 1850;

        animando = true;
        ultimoResultado = null;

        Timer temporizador = new Timer(16, null);
        temporizador.addActionListener(e -> {
            long transcurrido = System.currentTimeMillis() - tiempoInicio;
            double progreso = Math.min(1.0, transcurrido / (double) duracion);
            double suavizado = 1.0 - Math.pow(1.0 - progreso, 3.0);
            rotacion = inicio + recorrido * suavizado;
            repaint();

            if (progreso >= 1.0) {
                temporizador.stop();
                rotacion = inicio + recorrido;
                ultimoResultado = resultado;
                animando = false;
                repaint();
                if (alFinalizar != null) {
                    alFinalizar.run();
                }
            }
        });
        temporizador.start();
    }

    private String getNombreSector(TipoPieza tipo) {
        if (tipo == TipoPieza.HOMBRE_LOBO) {
            return "HOMBRE LOBO";
        }
        if (tipo == TipoPieza.VAMPIRO) {
            return "VAMPIRO";
        }
        if (tipo == TipoPieza.NECROMANTE) {
            return "NECROMANTE";
        }
        return "ZOMBIE";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margen = 24;
        int diametro = Math.min(getWidth(), getHeight()) - margen * 2;
        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + 5;
        int x = cx - diametro / 2;
        int y = cy - diametro / 2;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillOval(x + 8, y + 10, diametro, diametro);

        Graphics2D rueda = (Graphics2D) g2.create();
        rueda.rotate(Math.toRadians(rotacion), cx, cy);

        Color[] colores = {
            new Color(88, 14, 28),
            new Color(42, 42, 48),
            new Color(118, 22, 37),
            new Color(37, 37, 43),
            new Color(100, 16, 31),
            new Color(48, 48, 54)
        };

        for (int i = 0; i < sectores.length; i++) {
            int inicio = 120 - i * 60;
            rueda.setColor(colores[i]);
            rueda.fillArc(x, y, diametro, diametro, inicio, -60);
            rueda.setColor(new Color(230, 216, 190, 90));
            rueda.drawArc(x, y, diametro, diametro, inicio, -60);

            double angulo = Math.toRadians(-90 + i * 60);

            int radioImagen = diametro / 2 - 52;
            int tamImagen = 54;
            int ix = cx + (int) (Math.cos(angulo) * radioImagen) - tamImagen / 2;
            int iy = cy + (int) (Math.sin(angulo) * radioImagen) - tamImagen / 2;

            rueda.setColor(new Color(14, 14, 17, 155));
            rueda.fillOval(ix - 2, iy - 2, tamImagen + 4, tamImagen + 4);

            BufferedImage imagen = RecursosImagenes.getImagenPieza(sectores[i]);
            if (imagen != null) {
                Shape clipAnterior = rueda.getClip();
                Shape clip = new Ellipse2D.Double(ix, iy, tamImagen, tamImagen);
                rueda.setClip(clip);
                rueda.drawImage(imagen, ix, iy, tamImagen, tamImagen, null);
                rueda.setClip(clipAnterior);
            }

            rueda.setStroke(new BasicStroke(2f));
            rueda.setColor(Tema.DORADO);
            rueda.drawOval(ix, iy, tamImagen, tamImagen);

            int radioTexto = diametro / 2 - 112;
            int tx = cx + (int) (Math.cos(angulo) * radioTexto);
            int ty = cy + (int) (Math.sin(angulo) * radioTexto);

            String texto = getNombreSector(sectores[i]);
            int tamFuente = sectores[i] == TipoPieza.NECROMANTE ? 8 : 10;
            rueda.setFont(new Font("SansSerif", Font.BOLD, tamFuente));
            FontMetrics fm = rueda.getFontMetrics();
            rueda.setColor(Tema.TEXTO);
            rueda.drawString(texto, tx - fm.stringWidth(texto) / 2, ty + fm.getAscent() / 2);
        }

        rueda.setStroke(new BasicStroke(4f));
        rueda.setColor(Tema.DORADO);
        rueda.drawOval(x, y, diametro, diametro);
        rueda.dispose();

        int centro = 94;
        g2.setColor(new Color(12, 12, 15));
        g2.fillOval(cx - centro / 2, cy - centro / 2, centro, centro);
        g2.setColor(Tema.BORDE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(cx - centro / 2, cy - centro / 2, centro, centro);

        String centroTexto;
        if (animando) {
            centroTexto = "GIRANDO";
        } else if (ultimoResultado == null) {
            centroTexto = "RULETA";
        } else {
            centroTexto = getNombreSector(ultimoResultado);
        }

        int tamCentro = ultimoResultado == TipoPieza.NECROMANTE ? 10 : 14;
        g2.setFont(new Font("Serif", Font.BOLD, tamCentro));
        FontMetrics fmCentro = g2.getFontMetrics();
        g2.setColor(Tema.TEXTO);
        g2.drawString(centroTexto, cx - fmCentro.stringWidth(centroTexto) / 2, cy + fmCentro.getAscent() / 3);

        Polygon puntero = new Polygon();
        puntero.addPoint(cx, 6);
        puntero.addPoint(cx - 13, 31);
        puntero.addPoint(cx + 13, 31);
        g2.setColor(Tema.DORADO);
        g2.fillPolygon(puntero);
        g2.setColor(new Color(70, 45, 20));
        g2.drawPolygon(puntero);

        g2.dispose();
    }
}
