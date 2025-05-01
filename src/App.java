import javax.swing.SwingUtilities;

import gui.LoginGUI;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}
