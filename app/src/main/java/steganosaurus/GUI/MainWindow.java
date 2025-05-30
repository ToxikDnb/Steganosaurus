package steganosaurus.GUI;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    private FilePane filePane;
    private static final Dimension minSize = new Dimension(400, 200);
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
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        // TODO: Add action listener for the Exit menu item
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        // TODO: Add action listener for the About menu item
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        mainFrame.setJMenuBar(menuBar);
        // Set the main frame's background color

        // Initialize the FilePane
        filePane = new FilePane();
        JScrollPane scrollPane = new JScrollPane(filePane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Input Files"));

        mainFrame.add(scrollPane, BorderLayout.WEST);
        // Test files for the main frame
        for (int i = 0; i <= 20; i++) {
            filePane.addFile("example" + i + ".txt: 500 bytes");
        }

        // Add a 2-panel layout to the centre of the main frame
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Top centre panel
        JPanel topCentrePanel = new JPanel();
        topCentrePanel.setLayout(new BorderLayout());
        topCentrePanel.setBorder(BorderFactory.createTitledBorder("Options"));
        topCentrePanel.setPreferredSize(new Dimension(400, 300));

        // Bottom centre panel, containing file control buttons
        JPanel bottomCentrePanel = new JPanel();
        bottomCentrePanel.setLayout(new GridLayout(1, 5, 10, 10));
        bottomCentrePanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        bottomCentrePanel.setPreferredSize(new Dimension(400, 100));

        // Adding controls to the bottom centre panel
        // TODO: Add action listeners for these buttons

        JButton inputFile = new JButton("<html>Select<br/>File</html>");
        inputFile.setFocusPainted(false);
        inputFile.setFont(buttonFont);
        JButton addFile = new JButton("<html>Add<br/>Carriers</html>");
        addFile.setFocusPainted(false);
        addFile.setFont(buttonFont);
        JButton removeFile = new JButton("<html>Remove<br/>Carrier</html>");
        removeFile.setFocusPainted(false);
        removeFile.setFont(buttonFont);
        JButton clearFiles = new JButton("<html>Clear<br/>Files</html>");
        clearFiles.setFocusPainted(false);
        clearFiles.setFont(buttonFont);
        JButton runButton = new JButton("Run");
        runButton.setFocusPainted(false);
        runButton.setFont(buttonFont);
        bottomCentrePanel.add(inputFile);
        bottomCentrePanel.add(addFile);
        bottomCentrePanel.add(removeFile);
        bottomCentrePanel.add(clearFiles);
        bottomCentrePanel.add(runButton);

        // Add the centre panels to the main frame
        centerPanel.add(topCentrePanel, BorderLayout.CENTER);
        centerPanel.add(bottomCentrePanel, BorderLayout.SOUTH);
        mainFrame.add(centerPanel, BorderLayout.CENTER);

        // Adding stuff to the top centre panel
        JPanel fileInfoPanel = new JPanel();
        fileInfoPanel.setLayout(new GridLayout(5, 2, 10, 10));
        fileInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fileInfoPanel.add(new JLabel("File to hide:"));
        fileInfoPanel.add(fileLabel = new JLabel("No file selected"));
        fileInfoPanel.add(new JLabel("File size (bytes):"));
        fileInfoPanel.add(sizeLabel = new JLabel("0"));
        fileInfoPanel.add(new JLabel("Total bytes offered by images:"));
        fileInfoPanel.add(totalBytesLabel = new JLabel("0"));
        fileInfoPanel.add(new JLabel("Needed bytes for encryption:"));
        fileInfoPanel.add(neededBytesLabel = new JLabel("0"));
        fileInfoPanel.add(new JLabel("Status:"));
        fileInfoPanel.add(statusInfoLabel = new JLabel("Idle"));
        topCentrePanel.add(fileInfoPanel, BorderLayout.CENTER);

        // Add a status bar at the bottom of the main frame
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel versionLabel = new JLabel("Steganosaurus v0.1");
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusBar.add(versionLabel, BorderLayout.EAST);
        mainFrame.add(statusBar, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    public void updateFileInfo(String fileName, long fileSize, long totalBytes, long neededBytes, String status) {
        fileLabel.setText(fileName);
        sizeLabel.setText(String.valueOf(fileSize));
        totalBytesLabel.setText(String.valueOf(totalBytes));
        neededBytesLabel.setText(String.valueOf(neededBytes));
        statusInfoLabel.setText(status);
    }
}
