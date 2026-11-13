package modelo;

import java.util.ArrayList;

import dao.partidasDAO;

public class Gestionpartidas {

    private final partidasDAO partidaDAO = new partidasDAO();

    public int guardar(partidas partida) {
        try {
            return partidaDAO.guardar(partida);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<partidas> listar() {
        try {
            return partidaDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean actualizar(partidas partida) {
        try {
            return partidaDAO.actualizar(partida);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            return partidaDAO.eliminar(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
