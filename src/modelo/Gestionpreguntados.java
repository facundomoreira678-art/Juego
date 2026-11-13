package modelo;

import java.util.ArrayList;

import dao.jugadoresDAO;

public class Gestionpreguntados {

    private final jugadoresDAO jugadorDAO = new jugadoresDAO();

    public int guardar(jugadores jugador) {
        try {
            return jugadorDAO.guardar(jugador);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<jugadores> listar() {
        try {
            return jugadorDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean actualizar(jugadores jugador) {
        try {
            return jugadorDAO.actualizar(jugador);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            return jugadorDAO.eliminar(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
