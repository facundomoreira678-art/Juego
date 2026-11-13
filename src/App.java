import javax.swing.SwingUtilities;

import Vista.FormsRegistro;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormsRegistro().setVisible(true));
    }
}
