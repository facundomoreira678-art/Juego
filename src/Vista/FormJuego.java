package Vista;
import javax.swing.*;

import modelo.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.awt.*;
import java.awt.event.*;

// [USER_IMPORTS_START]
//  ESCRIBE TUS IMPORTS AQUI (Ej: import java.io.*;)
    // ----------------------------------------------------
// [USER_IMPORTS_END]

public class FormJuego extends JFrame {

    private Gestionpreguntados gestion;
    private Gestionpartidas gestionpartidas;

    private JLabel labelusuario;
    private JLabel labelpregunta;
    private JButton btnopcion1;
    private JButton btnopcion4;
    private JButton btnopcion2;
    private JButton btnopcion3;
    private JLabel labelpuntaje;
    private JLabel labelpreguntas_respondidas;
    private JLabel labelcategoria;

// [USER_DECLS_START]
//  DECLARA TUS VARIABLES AQUI (Ej: private int contador;)
    // ----------------------------------------------------
    partidas partidaActual ; 
    categorias categoriaActual;
    preguntas preguntaActual;
// [USER_DECLS_END]

    public FormJuego(jugadores jugador, categorias categoria) {
         LocalDateTime ahora = LocalDateTime.now();
        partidaActual = new partidas(0, jugador, 0, 0, ahora); 
        categoriaActual = categoria;
        gestionpartidas = new Gestionpartidas();
        setTitle("FormJuego");
        setSize(900, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        initComponents();
        initEvents();
        mostrarPregunta();
    }

    private void initComponents() {
        labelusuario = new JLabel("Label");
        labelusuario.setFont(new Font("Tahoma", Font.PLAIN, 11));
        labelusuario.setBounds(30, 28, 500, 24);
        labelpregunta = new JLabel("Label");
        labelpregunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
        labelpregunta.setBounds(30, 145, 820, 100);
        btnopcion1 = new JButton("Aceptar");
        btnopcion1.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnopcion1.setBounds(30, 285, 400, 40);
        btnopcion4 = new JButton("Aceptar");
        btnopcion4.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnopcion4.setBounds(470, 355, 400, 40);
        btnopcion2 = new JButton("Aceptar");
        btnopcion2.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnopcion2.setBounds(470, 285, 400, 40);
        btnopcion3 = new JButton("Aceptar");
        btnopcion3.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnopcion3.setBounds(30, 355, 400, 40);
        labelpuntaje = new JLabel("Label");
        labelpuntaje.setFont(new Font("Tahoma", Font.PLAIN, 11));
        labelpuntaje.setBounds(650, 20, 220, 24);
        labelpreguntas_respondidas = new JLabel("Label");
        labelpreguntas_respondidas.setFont(new Font("Tahoma", Font.PLAIN, 11));
        labelpreguntas_respondidas.setBounds(650, 55, 220, 24);
        labelcategoria = new JLabel("Label");
        labelcategoria.setFont(new Font("Tahoma", Font.PLAIN, 11));
        labelcategoria.setBounds(30, 90, 500, 24);
        getContentPane().add(labelusuario);
        getContentPane().add(labelpregunta);
        getContentPane().add(btnopcion1);
        getContentPane().add(btnopcion4);
        getContentPane().add(btnopcion2);
        getContentPane().add(btnopcion3);
        getContentPane().add(labelpuntaje);
        getContentPane().add(labelpreguntas_respondidas);
        getContentPane().add(labelcategoria);
    }

    private void initEvents() {
        // [USER_ACTION_START:btnopcion1]
        btnopcion1.addActionListener(e -> {
            responder("A");
        });
        // [USER_ACTION_END:btnopcion1]

        // [USER_ACTION_START:btnopcion4]
        btnopcion4.addActionListener(e -> {
            responder("D");
        });
        // [USER_ACTION_END:btnopcion4]

        // [USER_ACTION_START:btnopcion2]
        btnopcion2.addActionListener(e -> {
            responder("B");
        });
        // [USER_ACTION_END:btnopcion2]

        // [USER_ACTION_START:btnopcion3]
        btnopcion3.addActionListener(e -> {
            responder("C");
        });
        // [USER_ACTION_END:btnopcion3]

    }

// [USER_METHODS_START]
//  AGREGA TUS METODOS PERSONALIZADOS AQUI
    // ----------------------------------------------------
    public void actualizarTabla() {
        // TODO: recargar datos en la(s) tabla(s)
    }

    public void limpiarFormulario() {
        // TODO: limpiar campos del formulario
    }

    private void responder(String respuestaSeleccionada) {
        String respuestaCorrecta = preguntaActual.getRespuesta_correcta();
        boolean esCorrecta = respuestaCorrecta != null
                && respuestaSeleccionada.equalsIgnoreCase(respuestaCorrecta.trim());

        partidaActual.setPreguntas_respondidas(partidaActual.getPreguntas_respondidas() + 1);
        if (esCorrecta) {
            partidaActual.setPuntaje(partidaActual.getPuntaje() + 1);
        }

        labelpuntaje.setText("Puntaje: " + partidaActual.getPuntaje());
        labelpreguntas_respondidas.setText(
                "Preguntas Respondidas: " + partidaActual.getPreguntas_respondidas());

        boolean puntajeGuardado;
        if (partidaActual.getId() == 0) {
            int idPartida = gestionpartidas.guardar(partidaActual);
            puntajeGuardado = idPartida > 0;
            if (puntajeGuardado) {
                partidaActual.setId(idPartida);
            }
        } else {
            puntajeGuardado = gestionpartidas.actualizar(partidaActual);
        }

        if (!puntajeGuardado) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el puntaje de la partida.",
                    "Error al guardar puntaje", JOptionPane.ERROR_MESSAGE);
        }

        String mensaje = esCorrecta
                ? "¡Correcto!"
                : "Incorrecto. La respuesta correcta era " + respuestaCorrecta + ".";
        JOptionPane.showMessageDialog(this, mensaje,
                esCorrecta ? "Respuesta correcta" : "Respuesta incorrecta",
                esCorrecta ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        mostrarPregunta();
    }

    public void mostrarPregunta(){
        labelusuario.setText(partidaActual.getJugador().getNombre());
        labelcategoria.setText(categoriaActual.getNombre());
        try {
            preguntaActual = new Gestionpreguntas().aleatorio(categoriaActual.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErrorPregunta("No fue posible cargar una pregunta: " + e.getMessage());
            return;
        }

        if (preguntaActual == null) {
            mostrarErrorPregunta("No hay preguntas disponibles en la base de datos.");
            return;
        }

        labelpregunta.setText(preguntaActual.getPregunta());
        btnopcion1.setText(preguntaActual.getOpcion_a());
        btnopcion2.setText(preguntaActual.getOpcion_b());
        btnopcion3.setText(preguntaActual.getOpcion_c());
        btnopcion4.setText(preguntaActual.getOpcion_d());
        labelcategoria.setText(preguntaActual.getCategoria().getNombre());
        labelpuntaje.setText("Puntaje: " + partidaActual.getPuntaje());
        labelpreguntas_respondidas.setText("Preguntas Respondidas: " + partidaActual.getPreguntas_respondidas());
    }

    private void mostrarErrorPregunta(String mensaje) {
        labelpregunta.setText(mensaje);
        btnopcion1.setEnabled(false);
        btnopcion2.setEnabled(false);
        btnopcion3.setEnabled(false);
        btnopcion4.setEnabled(false);
        JOptionPane.showMessageDialog(this, mensaje, "No se pudo iniciar el juego",
                JOptionPane.ERROR_MESSAGE);
    }

// [USER_METHODS_END]
}