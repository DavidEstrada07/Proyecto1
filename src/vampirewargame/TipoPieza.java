package vampirewargame;

public enum TipoPieza {
    HOMBRE_LOBO("Hombre Lobo"),
    VAMPIRO("Vampiro"),
    NECROMANTE("Necrómante"),
    ZOMBIE("Zombie");

    private final String nombre;

    TipoPieza(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
