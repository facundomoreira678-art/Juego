package dao;
import conexion.Conexion;
import modelo.categorias;               
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoriaDAO {
    public int guardar(categorias categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre, color )  VALUES (?, ?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getColor());


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
 public ArrayList<categorias> listar() throws SQLException {
        String sql = "SELECT id, nombre, color FROM categorias ORDER BY nombre";
        ArrayList<categorias> lista = new ArrayList<>();

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new categorias(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("color")));
            }
        }
        return lista;
    }

    public categorias aleatoria() throws SQLException {
        String sql = "SELECT id, nombre, color FROM categorias ORDER BY RAND() LIMIT 1";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new categorias(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("color")
                );
            }
        }
        return null;
    }


    public boolean actualizar(categorias categoria) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ?, color = ? WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getColor());
            ps.setInt(3, categoria.getId());

            return ps.executeUpdate() > 0;
        }
    }
  public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

}
