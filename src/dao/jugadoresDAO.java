package dao;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.time.LocalDateTime;
import conexion.Conexion;
import modelo.jugadores;

public class jugadoresDAO {
    public jugadores obtenerOCrearPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id, nombre, fecha_registro FROM jugadores WHERE nombre = ? ORDER BY id LIMIT 1";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
                    return new jugadores(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            fechaRegistro != null ? fechaRegistro.toLocalDateTime() : null
                    );
                }
            }
        }

        LocalDateTime fechaRegistro = LocalDateTime.now();
        int id = guardar(new jugadores(0, nombre, fechaRegistro));
        if (id < 1) {
            throw new SQLException("No se pudo crear el registro del jugador.");
        }
        return new jugadores(id, nombre, fechaRegistro);
    }

     public int guardar(jugadores jugador) throws SQLException {
        String sql = "INSERT INTO jugadores (nombre, fecha_registro) VALUES (?, ?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, jugador.getNombre());
            ps.setTimestamp(2, Timestamp.valueOf(jugador.getFecha_registro()));

            int filas = ps.executeUpdate();
            if (filas == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }
 public ArrayList<jugadores> listar() throws SQLException {
        String sql = "SELECT id, nombre, fecha_registro FROM jugadores ORDER BY nombre";
        ArrayList<jugadores> lista = new ArrayList<>();

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new jugadores(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getTimestamp("fecha_registro").toLocalDateTime()
                ));
            }
        }
        return lista;
    }
    public boolean actualizar(jugadores jugador) throws SQLException {
        String sql = "UPDATE jugadores SET nombre = ?, fecha_registro = ? WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, jugador.getNombre());
            ps.setTimestamp(2, Timestamp.valueOf(jugador.getFecha_registro()));
            ps.setInt(3, jugador.getId());

            return ps.executeUpdate() > 0;
        }
    }
  public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM jugadores WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
