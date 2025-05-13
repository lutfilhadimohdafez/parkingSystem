import javax.swing.SwingUtilities;

public class start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu.MainMenu());
    }
}
