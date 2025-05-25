package steganosaurus.GUI;

import javax.swing.*;
import java.awt.*;

public class FilePane extends JPanel {

    public FilePane() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
    }

    public void addFile(String fileName) {
        JButton fileButton = new JButton(fileName);
        fileButton.setHorizontalAlignment(SwingConstants.LEFT);
        fileButton.setFocusPainted(false);
        fileButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        fileButton.setOpaque(true);
        fileButton.setBackground(Color.WHITE);
        fileButton.setFont(MainWindow.fileFont);
        fileButton.setForeground(Color.BLACK);

        fileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        fileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Store original color for reset
        Color defaultBg = Color.WHITE;
        Color hoverBg = new Color(220, 220, 220); // light grey
        Color clickBg = new Color(51, 153, 255); // soft blue

        // Hover and click effects
        fileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fileButton.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fileButton.setBackground(defaultBg);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fileButton.setBackground(clickBg);
                fileButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                // Restore hover or default depending on position
                if (fileButton.getBounds().contains(evt.getPoint())) {
                    fileButton.setBackground(hoverBg);
                    fileButton.setForeground(Color.BLACK);
                } else {
                    fileButton.setBackground(defaultBg);
                    fileButton.setForeground(Color.BLACK);
                }
            }
        });

        fileButton.addActionListener(e -> {
            System.out.println("Clicked: " + fileName);
        });

        this.add(fileButton);
        this.revalidate();
        this.repaint();
    }

}
