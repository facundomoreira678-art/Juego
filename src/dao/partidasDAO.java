package dao;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import conexion.Conexion;
import modelo.jugadores;
import modelo.partidas;
import modelo.PuntajeJugador;

public class partidasDAO {
    public ArrayList<PuntajeJugador> listarMejoresPuntajes() throws SQLException {
        String sql = "SELECT j.nombre, COALESCE(MAX(p.puntaje), 0) AS puntaje "
                + "FROM jugadores j LEFT JOIN partidas p ON p.jugador_id = j.id "
                + "GROUP BY j.id, j.nombre "
                + "ORDER BY puntaje DESC, j.nombre";
        ArrayList<PuntajeJugador> puntajes = new ArrayList<>();

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                puntajes.add(new PuntajeJugador(
                        rs.getString("nombre"),
                        rs.getInt("puntaje")
                ));
            }
        }
        return puntajes;
    }

    public int guardar(partidas partida) throws SQLException {
        String sql = "INSERT INTO partidas (jugador_id, puntaje, preguntas_respondidas, fecha) VALUES (?, ?, ?, ?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, partida.getJugador() != null ? partida.getJugador().getId() : 0);
            ps.setInt(2, partida.getPuntaje());
            ps.setInt(3, partida.getPreguntas_respondidas());
            ps.setTimestamp(4, Timestamp.valueOf(partida.getFecha()));

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

    public ArrayList<partidas> listar() throws SQLException {
        String sql = "SELECT id, jugador_id, puntaje, preguntas_respondidas, fecha FROM partidas ORDER BY jugador_id";
        ArrayList<partidas> lista = new ArrayList<>();

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int jugadorId = rs.getInt("jugador_id");
                jugadores jugador = new jugadores(jugadorId, "", null);

                lista.add(new partidas(
                        rs.getInt("id"),
                        jugador,
                        rs.getInt("puntaje"),
                        rs.getInt("preguntas_respondidas"),
                        rs.getTimestamp("fecha").toLocalDateTime()
                ));
            }
        }
        return lista;
    }

    public boolean actualizar(partidas partida) throws SQLException {
        String sql = "UPDATE partidas SET jugador_id = ?, puntaje = ?, preguntas_respondidas = ?, fecha = ? WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, partida.getJugador() != null ? partida.getJugador().getId() : 0);
            ps.setInt(2, partida.getPuntaje());
            ps.setInt(3, partida.getPreguntas_respondidas());
            ps.setTimestamp(4, Timestamp.valueOf(partida.getFecha()));
            ps.setInt(5, partida.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM partidas WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
