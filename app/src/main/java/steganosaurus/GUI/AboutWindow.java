package steganosaurus.GUI;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.HyperlinkEvent;

public class AboutWindow {

    private final int borderPadding = 20;

    public AboutWindow() {
        JDialog aboutWindow = new JDialog(MainWindow.instance.getMainFrame(), "About", true);
        aboutWindow.setSize(600, 450);
        aboutWindow.setLocationRelativeTo(MainWindow.instance.getMainFrame());
        aboutWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        aboutWindow.setResizable(false);
        // Set logo
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon64.png"));
        aboutWindow.setIconImage(icon.getImage());

        // Create pane and layout
        JPanel about = new JPanel();
        aboutWindow.setContentPane(about);
        about.setLayout(new BorderLayout());
        about.setBorder(BorderFactory.createEmptyBorder(borderPadding, borderPadding, borderPadding, borderPadding));

        // Add image to north
        JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource("/icon128.png")));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        about.add(imageLabel, BorderLayout.NORTH);

        // Add text to center
        JEditorPane textPane = new JEditorPane("text/html",
                "<html>" +
                        "<h1><b>Steganosaurus v0.1<b/></h1>" +
                        "<h2>Created by Mackenzie Blackaby</h2>" +
                        "<p>A powerful tool for hiding files through steganography." +
                        "<br/>For more information, visit: " +
                        "<a href='https://www.blackaby.uk/projects/steganosaurus'>" +
                        "https://www.blackaby.uk/projects/steganosaurus</a></p>" +
                        "<p>This software was created to demonstrate the power of steganography.\n" +
                        "It is intended for educational and ethical use only. By using this software, you agree to take full responsibility for your actions and to comply with all applicable regulations in your jurisdiction.</p>"
                        +
                        "</html>");
        textPane.setEditable(false);
        textPane.setFocusable(false);
        textPane.setOpaque(false);
        textPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        about.add(textPane, BorderLayout.CENTER);

        // Set visible
        aboutWindow.setVisible(true);
    }
}
