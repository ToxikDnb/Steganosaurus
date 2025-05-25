package steganosaurus.GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import steganosaurus.Controllers.*;

public class MainWindow {

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

    public MainWindow() {
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        addLabelPair(fileInfoPanel, "Uploaded file", fileLabel = new JLabel("No file selected"));
        addLabelPair(fileInfoPanel, "File size (bytes)", sizeLabel = new JLabel("0"));
        addLabelPair(fileInfoPanel, "Total bytes offered by images", totalBytesLabel = new JLabel("0"));
        addLabelPair(fileInfoPanel, "Remaining bytes", neededBytesLabel = new JLabel("0"));
        addLabelPair(fileInfoPanel, "Status", statusInfoLabel = new JLabel("Idle"));
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

        // Set the main frame to be visible
        mainFrame.setVisible(true);
    }

    /**
     * Updates the file information displayed in the main window.
     * 
     * @param fileName    the name of the file being processed
     * @param fileSize    the size of the file in bytes
     * @param totalBytes  the total bytes available in the carrier images
     * @param neededBytes the bytes needed for encryption
     * @param status      the current status of the operation (e.g., "Idle",
     *                    "Processing", "Completed")
     */
    public void updateFileInfo(String fileName, long fileSize, long totalBytes, long neededBytes, String status) {
        fileLabel.setText(fileName);
        sizeLabel.setText(String.valueOf(fileSize));
        totalBytesLabel.setText(String.valueOf(totalBytes));
        neededBytesLabel.setText(String.valueOf(neededBytes));
        statusInfoLabel.setText(status);
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
