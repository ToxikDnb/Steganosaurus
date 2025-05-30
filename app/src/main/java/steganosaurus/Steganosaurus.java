/*
 * Main application class for Steganosaurus.
 * This class serves as the entry point for the application.
 */
package steganosaurus;

import javax.swing.UIManager;
import steganosaurus.GUI.MainWindow;

public class Steganosaurus {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainWindow();
    }
}
