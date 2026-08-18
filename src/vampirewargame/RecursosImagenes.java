package vampirewargame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public final class RecursosImagenes {

    private static final BufferedImage IMAGEN_LOBO = cargar("lobo.png");
    private static final BufferedImage IMAGEN_VAMPIRO = cargar("vampiro.png");
    private static final BufferedImage IMAGEN_NECROMANTE = cargar("necromante.png");
    private static final BufferedImage IMAGEN_ZOMBIE = cargar("zombie.png");

    private RecursosImagenes() {
    }

    private static BufferedImage cargar(String nombreArchivo) {
        String rutaRecurso = "/vampirewargame/recursos/" + nombreArchivo;
        InputStream entrada = RecursosImagenes.class.getResourceAsStream(rutaRecurso);
        if (entrada != null) {
            try {
                return ImageIO.read(entrada);
            } catch (IOException ex) {
                
            } finally {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    
                }
            }
        }

        String[] rutasPosibles = new String[]{
            "src/vampirewargame/recursos/" + nombreArchivo,
            "VampireWargameAvance/src/vampirewargame/recursos/" + nombreArchivo,
            "VampireWargame_ImagenesRuleta/src/vampirewargame/recursos/" + nombreArchivo
        };

        for (int i = 0; i < rutasPosibles.length; i++) {
            File archivo = new File(rutasPosibles[i]);
            if (archivo.exists()) {
                try {
                    return ImageIO.read(archivo);
                } catch (IOException ex) {
                    return null;
                }
            }
        }

        return null;
    }

    public static BufferedImage getImagenPieza(TipoPieza tipo) {
        if (tipo == null) {
            return null;
        }

        if (tipo == TipoPieza.HOMBRE_LOBO) {
            return IMAGEN_LOBO;
        }
        if (tipo == TipoPieza.VAMPIRO) {
            return IMAGEN_VAMPIRO;
        }
        if (tipo == TipoPieza.NECROMANTE) {
            return IMAGEN_NECROMANTE;
        }
        return IMAGEN_ZOMBIE;
    }
}
