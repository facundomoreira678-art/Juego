package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import conexion.Conexion;
import modelo.categorias;
import modelo.preguntas;

public class preguntasDAO {
    public int guardar(preguntas pregunta) throws SQLException {
        String sql = "INSERT INTO preguntas (categoria_id, pregunta, opcion_a, opcion_b, opcion_c, opcion_d, respuesta_correcta, dificultad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pregunta.getCategoria() != null ? pregunta.getCategoria().getId() : 0);
            ps.setString(2, pregunta.getPregunta());
            ps.setString(3, pregunta.getOpcion_a());
            ps.setString(4, pregunta.getOpcion_b());
            ps.setString(5, pregunta.getOpcion_c());
            ps.setString(6, pregunta.getOpcion_d());
            ps.setString(7, pregunta.getRespuesta_correcta());
            ps.setString(8, pregunta.getDificultad());

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

    public ArrayList<preguntas> listar() throws SQLException {
        String sql = "SELECT p.id, p.categoria_id, p.pregunta, p.opcion_a, p.opcion_b, p.opcion_c, p.opcion_d, p.respuesta_correcta, p.dificultad, c.id,c.nombre, c.color \r\n" + //
                        "FROM preguntas p\r\n" + //
                        "JOIN categorias c ON p.categoria_id = c.id\r\n" + //
                        "ORDER BY RAND()\r\n" + //
                        "LIMIT 1;";
        ArrayList<preguntas> lista = new ArrayList<>();

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int categoriaId = rs.getInt("categoria_id");
                categorias categoria = new categorias(categoriaId, "", "");

                lista.add(new preguntas(
                        rs.getInt("id"),
                        categoria,
                        rs.getString("pregunta"),
                        rs.getString("opcion_a"),
                        rs.getString("opcion_b"),
                        rs.getString("opcion_c"),
                        rs.getString("opcion_d"),
                        rs.getString("respuesta_correcta"),
                        rs.getString("dificultad")
                ));
            }
        }
        return lista;
    }
public preguntas Aleatorio(int categoriaId) throws SQLException {
        String sql = "SELECT p.id, p.categoria_id, c.nombre AS categoria_nombre, "
                + "c.color AS categoria_color, p.pregunta, p.opcion_a, p.opcion_b, "
                + "p.opcion_c, p.opcion_d, p.respuesta_correcta, p.dificultad "
                + "FROM preguntas p "
                + "JOIN categorias c ON p.categoria_id = c.id "
                + "WHERE p.categoria_id = ? "
                + "ORDER BY RAND() LIMIT 1";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    categorias categoria = new categorias(
                            rs.getInt("categoria_id"),
                            rs.getString("categoria_nombre"),
                            rs.getString("categoria_color")
                    );

                    return new preguntas(
                            rs.getInt("id"),
                            categoria,
                            rs.getString("pregunta"),
                            rs.getString("opcion_a"),
                            rs.getString("opcion_b"),
                            rs.getString("opcion_c"),
                            rs.getString("opcion_d"),
                            rs.getString("respuesta_correcta"),
                            rs.getString("dificultad")
                    );
                }
            }
        }
        return null;
    }


    public boolean actualizar(preguntas pregunta) throws SQLException {
        String sql = "UPDATE preguntas SET categoria_id = ?, pregunta = ?, opcion_a = ?, opcion_b = ?, opcion_c = ?, opcion_d = ?, respuesta_correcta = ?, dificultad = ? WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, pregunta.getCategoria() != null ? pregunta.getCategoria().getId() : 0);
            ps.setString(2, pregunta.getPregunta());
            ps.setString(3, pregunta.getOpcion_a());
            ps.setString(4, pregunta.getOpcion_b());
            ps.setString(5, pregunta.getOpcion_c());
            ps.setString(6, pregunta.getOpcion_d());
            ps.setString(7, pregunta.getRespuesta_correcta());
            ps.setString(8, pregunta.getDificultad());
            ps.setInt(9, pregunta.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM preguntas WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}

