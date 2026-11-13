package modelo;

import java.util.ArrayList;
import java.sql.SQLException;

import dao.CategoriaDAO;

public class Gestioncategorias {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public int guardar(categorias categoria) {
        try {
            return categoriaDAO.guardar(categoria);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<categorias> listar() {
        try {
            return categoriaDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public categorias aleatoria() throws SQLException {
        return categoriaDAO.aleatoria();
    }


    public boolean actualizar(categorias categoria) {
        try {
            return categoriaDAO.actualizar(categoria);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            return categoriaDAO.eliminar(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
