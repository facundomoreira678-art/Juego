package modelo;
import java.time.*;
public class jugadores {
private int id;
    private String nombre;
    private LocalDateTime fecha_registro;
   

    public jugadores(int id, String nombre, LocalDateTime fecha_registro) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_registro = fecha_registro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDateTime getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(LocalDateTime fecha_registro) { this.fecha_registro = fecha_registro; }
}
