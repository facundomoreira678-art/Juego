package modelo;

public class PuntajeJugador {
    private final String nombre;
    private final int puntaje;

    public PuntajeJugador(String nombre, int puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
