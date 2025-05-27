package steganosaurus.Controllers;

import javax.swing.JFileChooser;
import steganosaurus.GUI.MainWindow;
import java.io.File;

public class BackendActions extends Actions<BackendEnum> {

    private int fileRepIndex = -1; // Index of the file represented by the button in the GUI. -1 unless modified by
    // a constructor.

    /**
     * Constructor for BackendActions.
     *
     * @param actionType the type of backend action this instance represents
     */
    public BackendActions(BackendEnum actionType) {
        super(actionType);
    }

    /**
     * Constructor for BackendActions with a specific file representation index.
     *
     * @param actionType   the type of backend action this instance represents
     * @param fileRepIndex the index of the file represented by this action
     */
    public BackendActions(BackendEnum actionType, int fileRepIndex) {
        super(actionType);
        this.fileRepIndex = fileRepIndex;
    }

    @Override
    public void execute() {
        switch (actionType) {
            case SWITCH_MODE:
                // Logic to switch modes
                break;
            case SELECT_FILE:
                File[] selectedFiles = openFileChooser(false);
                if (selectedFiles != null && selectedFiles.length > 0) {
                    File selectedFile = selectedFiles[0];
                    MainWindow.instance.getBackend().selectFile(selectedFile);
                }
                break;
            case SELECT_CARRIER:
                if (fileRepIndex < 0)
                    break;
                MainWindow.instance.getBackend().selectCarrier(fileRepIndex);
                break;
            case ADD_CARRIERS:
                File[] carrierFiles = openFileChooser(true);
                if (carrierFiles == null || carrierFiles.length == 0)
                    break;
                MainWindow.instance.getBackend().addCarriers(carrierFiles);
                break;
            case REMOVE_CARRIER:
                MainWindow.instance.getBackend().deleteCarrier();
                break;
            case CLEAR_CARRIERS:
                MainWindow.instance.getBackend().clearCarriers();
                break;
            case RUN:
                // Logic to run the backend process
                break;
            default:
                throw new UnsupportedOperationException("Action not supported: " + actionType);
        }
    }

    // #region Helper Functions

    /**
     * Opens a file chooser dialog to select a file.
     *
     * @return the selected file, or null if no file was selected
     */
    private File[] openFileChooser(boolean multipleSelection) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(multipleSelection);
        int returnValue = fileChooser.showOpenDialog(MainWindow.instance.getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (multipleSelection)
                return fileChooser.getSelectedFiles();
            else
                return new File[] { fileChooser.getSelectedFile() };
        }
        return null;
    }

    // #endregion

}
