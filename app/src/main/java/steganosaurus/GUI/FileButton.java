package steganosaurus.GUI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;

import steganosaurus.Controllers.BackendActions;
import steganosaurus.Controllers.BackendEnum;

import java.awt.Color;

public class FileButton extends JButton {

    BackendActions currentAction = null;

    /**
     * Constructs a FileButton with the specified text.
     * The button is styled with a left alignment, custom font, and hover/click
     * effects.
     *
     * @param text the text to display on the button
     */
    public FileButton(String text, int fileRepIndex) {
        super(text);
        setHorizontalAlignment(SwingConstants.LEFT);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setOpaque(true);
        setBackground(Color.WHITE);
        setFont(MainWindow.fileFont);
        setForeground(Color.BLACK);

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Store original color for reset
        Color defaultBg = Color.WHITE;
        Color hoverBg = new Color(220, 220, 220);
        Color clickBg = new Color(51, 153, 255);

        // Hover and click effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverBg);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(defaultBg);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(clickBg);
                setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                // Restore hover or default depending on position
                if (getBounds().contains(evt.getPoint())) {
                    setBackground(hoverBg);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(defaultBg);
                    setForeground(Color.BLACK);
                }
            }
        });
        replaceIndex(fileRepIndex);
    }

    /**
     * Replaces the current action with a new BackendActions instance for selecting a
     * carrier at the specified index.
     *
     * @param index the index of the carrier to select
     */
    public void replaceIndex(int index) {
        if (currentAction != null)
            removeActionListener(currentAction);
        currentAction = new BackendActions(BackendEnum.SELECT_CARRIER, index);
        addActionListener(currentAction);
    }
}
