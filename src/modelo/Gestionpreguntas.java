package modelo;

import java.util.ArrayList;
import java.sql.SQLException;

import dao.preguntasDAO;

public class Gestionpreguntas {

    private final preguntasDAO preguntaDAO = new preguntasDAO();

    public int guardar(preguntas pregunta) {
        try {
            return preguntaDAO.guardar(pregunta);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<preguntas> listar() {
        try {
            return preguntaDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public preguntas aleatorio(int categoriaId) throws SQLException {
        return preguntaDAO.Aleatorio(categoriaId);
    }

    public boolean actualizar(preguntas pregunta) {
        try {
            return preguntaDAO.actualizar(pregunta);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            return preguntaDAO.eliminar(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
