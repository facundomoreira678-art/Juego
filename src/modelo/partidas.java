package modelo;

import java.time.LocalDateTime;

public class partidas {
    private int id;
    private jugadores jugador;
    private int puntaje;
    private int preguntas_respondidas;
    private LocalDateTime fecha;
   

    public partidas(int id, jugadores jugador, int puntaje, int preguntas_respondidas, LocalDateTime fecha) {
        this.id = id;
        this.jugador = jugador;
        this.puntaje = puntaje;
        this.preguntas_respondidas = preguntas_respondidas;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public jugadores getJugador() { return jugador; }
    public void setJugador(jugadores jugador) { this.jugador = jugador; }
    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
    public int getPreguntas_respondidas() { return preguntas_respondidas; }
    public void setPreguntas_respondidas(int preguntas_respondidas) { this.preguntas_respondidas = preguntas_respondidas; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}