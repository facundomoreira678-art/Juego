package Vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.CuentasDAO;
import dao.jugadoresDAO;
import dao.partidasDAO;
import modelo.Gestioncategorias;
import modelo.Gestionpreguntados;
import modelo.PuntajeJugador;
import modelo.categorias;
import modelo.jugadores;

public class FormsRegistro extends JFrame {

    private static final Pattern FORMATO_CORREO =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final CuentasDAO cuentasDAO = new CuentasDAO();
    private final jugadoresDAO jugadoresDAO = new jugadoresDAO();
    private final JTextField campoCorreo = new JTextField(22);
    private final JPasswordField campoContrasena = new JPasswordField(22);
    private final JButton botonIniciarSesion = new JButton("Iniciar sesión");
    private final JButton botonRegistrarse = new JButton("Registrarse");
    private final JButton botonVerPuntajes = new JButton("Ver puntajes");
    private jugadores jugadorActual;

    public FormsRegistro() {
        setTitle("Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(22f));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(titulo, constraints);

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridy = 1;
        constraints.gridx = 0;
        panel.add(new JLabel("Correo:"), constraints);
        constraints.gridx = 1;
        panel.add(campoCorreo, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        panel.add(new JLabel("Contraseña:"), constraints);
        constraints.gridx = 1;
        panel.add(campoContrasena, constraints);

        constraints.gridy = 3;
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        JPanel botones = new JPanel();
        botones.add(botonRegistrarse);
        botones.add(botonIniciarSesion);
        botones.add(botonVerPuntajes);
        panel.add(botones, constraints);

        setContentPane(panel);
        getRootPane().setDefaultButton(botonIniciarSesion);
        pack();
        setLocationRelativeTo(null);

        botonIniciarSesion.addActionListener(event -> iniciarSesion());
        botonRegistrarse.addActionListener(event -> registrarCuenta());
        botonVerPuntajes.addActionListener(event -> mostrarPuntajes());
    }

    public FormsRegistro(Gestionpreguntados gestion) {
        this();
    }

    public jugadores getJugadorActual() {
        return jugadorActual;
    }

    private void mostrarPuntajes() {
        try {
            ArrayList<PuntajeJugador> puntajes = new partidasDAO().listarMejoresPuntajes();
            if (puntajes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todavía no hay jugadores registrados.",
                        "Puntajes", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            DefaultTableModel modelo = new DefaultTableModel(
                    new Object[] { "Jugador", "Mejor puntaje" }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (PuntajeJugador puntaje : puntajes) {
                modelo.addRow(new Object[] { puntaje.getNombre(), puntaje.getPuntaje() });
            }

            JTable tabla = new JTable(modelo);
            tabla.setFillsViewportHeight(true);
            JOptionPane.showMessageDialog(this, new JScrollPane(tabla), "Puntajes de jugadores",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErrorBaseDeDatos(e);
        }
    }

    private void iniciarSesion() {
        String correo = campoCorreo.getText().trim();
        char[] contrasena = campoContrasena.getPassword();

        if (correo.isEmpty() || contrasena.length == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa tu correo y contraseña.",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE
            );
            Arrays.fill(contrasena, '\0');
            return;
        }

        try {
            if (cuentasDAO.autenticar(normalizarCorreo(correo), contrasena)) {
                jugadorActual = jugadoresDAO.obtenerOCrearPorNombre(normalizarCorreo(correo));
                categorias categoria = new Gestioncategorias().aleatoria();
                if (categoria == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No hay categorías registradas para iniciar el juego.",
                            "Sin categorías",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                JOptionPane.showMessageDialog(
                        this,
                        "Sesión iniciada correctamente. Categoría: " + categoria.getNombre()
                );
                FormJuego formJuego = new FormJuego(jugadorActual, categoria);
                formJuego.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos. Regístrate antes de iniciar sesión.",
                        "No se pudo iniciar sesión",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErrorBaseDeDatos(e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mostrarErrorSeguridad(e);
        } finally {
            Arrays.fill(contrasena, '\0');
            campoContrasena.setText("");
        }
    }

    private void registrarCuenta() {
        JTextField correoCampo = new JTextField(22);
        JPasswordField contrasenaCampo = new JPasswordField(22);
        JPasswordField confirmarCampo = new JPasswordField(22);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;

        agregarCampo(panel, constraints, 0, "Correo:", correoCampo);
        agregarCampo(panel, constraints, 1, "Contraseña:", contrasenaCampo);
        agregarCampo(panel, constraints, 2, "Confirmar contraseña:", confirmarCampo);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Crear cuenta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String correo = normalizarCorreo(correoCampo.getText().trim());
        char[] contrasena = contrasenaCampo.getPassword();
        char[] confirmacion = confirmarCampo.getPassword();
        try {
            if (!FORMATO_CORREO.matcher(correo).matches()) {
                JOptionPane.showMessageDialog(this, "Ingresa un correo válido.", "Datos inválidos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (contrasena.length < 8) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres.",
                        "Datos inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!Arrays.equals(contrasena, confirmacion)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Datos inválidos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cuentasDAO.registrar(correo, contrasena)) {
                JOptionPane.showMessageDialog(this, "Cuenta creada. Ya puedes iniciar sesión.");
                campoCorreo.setText(correo);
                campoContrasena.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe una cuenta con ese correo.",
                        "Correo registrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErrorBaseDeDatos(e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mostrarErrorSeguridad(e);
        } finally {
            Arrays.fill(contrasena, '\0');
            Arrays.fill(confirmacion, '\0');
        }
    }

    private void agregarCampo(JPanel panel, GridBagConstraints constraints, int fila,
                              String etiqueta, JTextField campo) {
        constraints.gridx = 0;
        constraints.gridy = fila;
        panel.add(new JLabel(etiqueta), constraints);
        constraints.gridx = 1;
        panel.add(campo, constraints);
    }

    private String normalizarCorreo(String correo) {
        return correo.toLowerCase(Locale.ROOT);
    }

    private void mostrarErrorBaseDeDatos(SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "No fue posible acceder a la base de datos. Verifica que MySQL esté iniciado y que exista la base "
                        + "`preguntados`.\nDetalle: " + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void mostrarErrorSeguridad(IllegalStateException e) {
        JOptionPane.showMessageDialog(
                this,
                "No fue posible proteger la contraseña.\nDetalle: " + e.getMessage(),
                "Error de seguridad",
                JOptionPane.ERROR_MESSAGE
        );









        
    }
}
