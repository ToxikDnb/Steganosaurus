package steganosaurus.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * FilePane is a JPanel that displays a list of files with buttons.
 * Each button shows the file name and size, and has hover and click effects.
 * It uses a vertical BoxLayout for layout management.
 */
public class FilePane extends JPanel {

    private ArrayList<FileButton> fileButtons;

    /**
     * Constructs a FilePane with a vertical BoxLayout and a white background.
     */
    public FilePane() {
        super();
        fileButtons = new ArrayList<FileButton>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
    }

    /**
     * Adds a file button with the specified name and size to the pane.
     * The button displays the file name and its formatted size.
     *
     * @param fileName the name of the file
     * @param fileSize the size of the file in bytes
     */
    public void addFile(String fileName, long fileSize) {
        addFile(fileName + " (" + MainWindow.formatFileSize(fileSize) + ")");
    }

    /**
     * Adds a file button with the specified name to the pane.
     * The button has hover and click effects, and prints the file name when
     * clicked.
     *
     * @param fileName the name of the file
     */
    public void addFile(String fileName) {
        FileButton fileButton = new FileButton(fileName, fileButtons.size());
        fileButtons.add(fileButton);
        this.add(fileButton);
        this.revalidate();
        this.repaint();
    }

    /**
     * Removes a file button at the specified index from the pane.
     * If the index is out of bounds, it prints an error message.
     *
     * @param index the index of the button to remove
     */
    public void removeButtonAtIndex(int index) {
        if (index < 0 || index > fileButtons.size())
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        JButton button = fileButtons.get(index);
        removeFileButton(button);
    }

    /**
     * Removes all file buttons from the pane.
     * This clears the list of buttons and repaints the pane.
     */
    public void removeAllButtons() {
        for (JButton button : fileButtons)
            this.remove(button);
        fileButtons.clear();
        this.revalidate();
        this.repaint();
    }

    // #region Helper Functions

    /**
     * Removes a specific file button from the pane.
     * This method updates the list of buttons and repaints the pane.
     *
     * @param button the button to remove
     */
    private void removeFileButton(JButton button) {
        fileButtons.remove(button);
        this.remove(button);
        recreateActions();
        this.revalidate();
        this.repaint();
    }

    /**
     * Recreates the actions for each file button.
     * This updates the index of each button in the list to maintain consistency.
     */
    private void recreateActions() {
        for (int i = 0; i < fileButtons.size(); i++) {
            FileButton button = fileButtons.get(i);
            button.replaceIndex(i);
        }
    }

    // #endregion
}