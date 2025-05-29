package steganosaurus.GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import steganosaurus.Controllers.*;
import steganosaurus.Backend.MainBackend;
import java.io.File;

/**
 * MainWindow class represents the main GUI window of the Steganosaurus
 * application.
 * It initializes the main frame, menu bar, file pane, and various controls for
 * user interaction.
 */
public class MainWindow {

    private JFrame mainFrame;
    private MainBackend backend;
    private FilePane filePane;
    private static final Dimension minSize = new Dimension(400, 200);
    // Fonts used in the application
    public static final Font fileFont = new Font("Arial", Font.PLAIN, 12);
    public static final Font buttonFont = new Font("Arial", Font.BOLD, 14);

    private JLabel fileLabel;
    private JLabel sizeLabel;
    private JLabel totalBytesLabel;
    private JLabel neededBytesLabel;
    private JLabel statusInfoLabel;
    public static MainWindow instance;

    /**
     * Singleton constructor for the MainWindow class.
     * Ensures that only one instance of MainWindow exists.
     */
    public MainWindow() {
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException("MainWindow instance already exists.");
        }
        // Set up the main frame
        JFrame mainFrame = new JFrame("Steganosaurus - Encrypt");
        mainFrame.setSize(new Dimension(800, 400));
        mainFrame.setMinimumSize(minSize);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        // Add the icon to the frame
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon64.png"));
        mainFrame.setIconImage(icon.getImage());

        // Add a menu bar to the main frame
        JMenuBar menuBar = new JMenuBar();
        addMenu(menuBar, "File",
                createMenuItem("Switch Mode", new BackendActions(BackendEnum.SWITCH_MODE)),
                null,
                createMenuItem("Exit", new GUIActions(GUIEnum.EXIT)));
        addMenu(menuBar, "Steganosaurus",
                createMenuItem("Select File", new BackendActions(BackendEnum.SELECT_FILE)),
                null,
                createMenuItem("Add Carriers", new BackendActions(BackendEnum.ADD_CARRIERS)),
                createMenuItem("Remove Carrier", new BackendActions(BackendEnum.REMOVE_CARRIER)),
                createMenuItem("Clear Carriers", new BackendActions(BackendEnum.CLEAR_CARRIERS)),
                null,
                createMenuItem("Run", new BackendActions(BackendEnum.RUN)));
        addMenu(
                menuBar, "Help", createMenuItem("About", new GUIActions(GUIEnum.ABOUT)));
        mainFrame.setJMenuBar(menuBar);

        // Initialize the FilePane
        filePane = new FilePane();
        JScrollPane scrollPane = new JScrollPane(filePane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Input Files"));

        mainFrame.add(scrollPane, BorderLayout.WEST);

        // Add a 2-panel layout to the centre of the main frame
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Top centre panel
        JPanel topCentrePanel = new JPanel();
        topCentrePanel.setLayout(new BorderLayout());
        topCentrePanel.setBorder(BorderFactory.createTitledBorder("File Information"));
        topCentrePanel.setPreferredSize(new Dimension(400, 300));

        // Bottom centre panel, containing file control buttons
        JPanel bottomCentrePanel = new JPanel();
        bottomCentrePanel.setLayout(new GridLayout(1, 5, 10, 10));
        bottomCentrePanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        bottomCentrePanel.setPreferredSize(new Dimension(400, 100));

        // Adding controls to the bottom centre panel
        addButton(bottomCentrePanel, "<html>Select<br/>File</html>", new BackendActions(BackendEnum.SELECT_FILE));
        addButton(bottomCentrePanel, "<html>Add<br/>Carriers</html>", new BackendActions(BackendEnum.ADD_CARRIERS));
        addButton(bottomCentrePanel, "<html>Remove<br/>Carrier</html>", new BackendActions(BackendEnum.REMOVE_CARRIER));
        addButton(bottomCentrePanel, "<html>Clear<br/>Carriers</html>", new BackendActions(BackendEnum.CLEAR_CARRIERS));
        addButton(bottomCentrePanel, "Run", new BackendActions(BackendEnum.RUN));

        // Add the centre panels to the main frame
        centerPanel.add(topCentrePanel, BorderLayout.CENTER);
        centerPanel.add(bottomCentrePanel, BorderLayout.SOUTH);
        mainFrame.add(centerPanel, BorderLayout.CENTER);

        // Adding stuff to the top centre panel
        JPanel fileInfoPanel = new JPanel();
        fileInfoPanel.setLayout(new GridLayout(5, 2, 10, 10));
        fileInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addLabelPair(fileInfoPanel, "Uploaded file", fileLabel = new JLabel());
        addLabelPair(fileInfoPanel, "File size (bytes)", sizeLabel = new JLabel());
        addLabelPair(fileInfoPanel, "Total bytes offered by images", totalBytesLabel = new JLabel());
        addLabelPair(fileInfoPanel, "Remaining bytes", neededBytesLabel = new JLabel());
        addLabelPair(fileInfoPanel, "Status", statusInfoLabel = new JLabel());
        topCentrePanel.add(fileInfoPanel, BorderLayout.CENTER);

        // Add a status bar at the bottom of the main frame
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        // Add a label to display the version information
        JLabel versionLabel = new JLabel("Steganosaurus v0.1");
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusBar.add(versionLabel, BorderLayout.EAST);
        mainFrame.add(statusBar, BorderLayout.SOUTH);

        // Initialize the backend
        backend = new MainBackend(this);

        // Set the main frame to be visible
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setVisible(true);
        mainFrame.setAlwaysOnTop(false);
    }

    /**
     * Sets the name of the file being processed.
     * 
     * @param fileName the name of the file
     */
    public void setFileName(String fileName) {
        fileLabel.setText(fileName);
    }

    /**
     * Sets the size of the file in bytes.
     * 
     * @param fileSize the size of the file
     */
    public void setFileSize(long fileSize) {
        sizeLabel.setText(formatFileSize(fileSize));
    }

    /**
     * Sets the total number of bytes available in the carrier images.
     * 
     * @param totalBytes the total available bytes
     */
    public void setTotalBytes(long totalBytes) {
        totalBytesLabel.setText(formatFileSize(totalBytes));
    }

    /**
     * Sets the number of bytes needed for encryption.
     * 
     * @param neededBytes the required bytes
     */
    public void setNeededBytes(long neededBytes) {
        neededBytesLabel.setText(formatFileSize(neededBytes));
    }

    /**
     * Sets the current status of the operation.
     * 
     * @param status the current status (e.g., "Idle", "Processing", "Completed")
     */
    public void setStatus(String status) {
        statusInfoLabel.setText(status);
    }

    /**
     * Returns the main frame of the application.
     * 
     * @return the main JFrame instance
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Returns the backend instance used in the main window.
     * 
     * @return the backend instance
     */
    public MainBackend getBackend() {
        return backend;
    }

    /**
     * Adds a file to the file pane with the specified name and size.
     * 
     * @param fileName the name of the file
     * @param fileSize the size of the file in bytes
     */
    public void addCarrier(String fileName, long fileSize) {
        filePane.addFile(fileName, fileSize);
    }

    /**
     * Adds a file to the file pane with the specified name.
     * 
     * @param fileName the name of the file
     */
    public void removeCarrier(int index) {
        filePane.removeButtonAtIndex(index);
    }

    /**
     * Clears all files from the file pane.
     */
    public void clearCarriers() {
        filePane.removeAllButtons();
    }

    /**
     * Displays an error dialog with the specified title and message.
     * 
     * @param title   the title of the error dialog
     * @param message the error message to display
     */
    public void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information dialog with the specified title and message.
     * 
     * @param title   the title of the information dialog
     * @param message the information message to display
     */
    public void showInformationDialog(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a success dialog with the specified file and mode (e.g.,
     * "encrypted",
     * "decrypted").
     * 
     * @param file the file that was successfully processed
     * @param mode the mode of operation (e.g., "encrypted", "decrypted")
     */
    public void showSuccessDialog(File file, String mode) {
        String message = "File successfully " + mode + "!\n" +
                "Path: " + file.getAbsolutePath();
        showInformationDialog("Success!", message);
        // Open explorer to the file location
        String dirLocation = file.getAbsolutePath();
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(dirLocation));
            } catch (Exception e) {
                showErrorDialog("Error", "Could not open file location: " + e.getMessage());
            }
        } else {
            showErrorDialog("Error", "Desktop operations are not supported on this platform.");
        }
    }

    /**
     * Formats the file size into a human-readable string.
     * Sizes are formatted as bytes, kilobytes, megabytes, or gigabytes.
     *
     * @param size the size in bytes
     * @return a formatted string representing the file size
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    // #region Helper functions
    // Add a menu to the menu bar with the given name and items
    private void addMenu(JMenuBar menuBar, String name, JMenuItem... items) {
        JMenu menu = new JMenu(name);
        for (JMenuItem item : items)
            if (item == null)
                menu.addSeparator();
            else
                menu.add(item);
        menuBar.add(menu);
    }

    // Create a menu item with the given name and action
    private <E extends Enum<E>> JMenuItem createMenuItem(String name, Actions<E> actionListener) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    // Add a button to the given panel with the specified name and action
    private <E extends Enum<E>> void addButton(JPanel panel, String name, Actions<E> actionListener) {
        JButton button = new JButton(name);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        button.setFont(buttonFont);
        button.addActionListener(actionListener);
        panel.add(button);
    }

    // Add a label pair to the given panel with the specified label text and
    // interactive label
    private void addLabelPair(JPanel panel, String labelText, JLabel interactiveLabel) {
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(
                new CompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.gray),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        labelPanel.add(new JLabel(labelText + ": "), BorderLayout.WEST);
        labelPanel.add(interactiveLabel, BorderLayout.EAST);
        panel.add(labelPanel);
    }

    // #endregion
}
