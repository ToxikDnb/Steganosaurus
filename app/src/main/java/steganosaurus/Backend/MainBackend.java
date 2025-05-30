package steganosaurus.Backend;

import java.io.File;
import java.util.ArrayList;

import steganosaurus.GUI.MainWindow;

public class MainBackend {

    // Status codes for backend process
    private enum Status {
        IDLE,
        RUNNING,
        DONE,
    }

    private File selectedFile;
    private ArrayList<File> carriers = new ArrayList<>();
    private MainWindow frontend;
    private int selectedCarrierIndex = -1;

    /**
     * Constructor for MainBackend.
     *
     * @param frontend the MainWindow instance that interacts with this backend
     */
    public MainBackend(MainWindow frontend) {
        this.frontend = frontend;
        updateStatus(Status.IDLE);
        selectFile(null);
        updateTotalBytes(calculateTotalBytes());
        updateNeededBytes(calculateNeededBytes());
    }

    private void updateStatus(Status status) {
        switch (status) {
            case IDLE:
                frontend.setStatus("Idle");
                break;
            case RUNNING:
                frontend.setStatus("Running");
                break;
            case DONE:
                frontend.setStatus("Done");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    /**
     * Selects a file to be processed by the backend.
     *
     * @param file the file to select
     */
    public void selectFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            selectedFile = file;
            frontend.setFileName(file.getName());
            frontend.setFileSize(file.length());
            updateFileSize(file.length());
            updateNeededBytes(calculateNeededBytes());
            updateStatus(Status.IDLE);
        } else {
            frontend.setFileName("No file selected");
            frontend.setFileSize(0);
        }

    }

    /**
     * Adds carrier files to the backend.
     *
     * @param newCarriers an array of files to be added as carriers
     */
    public void addCarriers(File[] newCarriers) {
        for (File carrier : newCarriers) {
            if (carrier == null || !carrier.exists() || !carrier.isFile())
                throw new IllegalArgumentException("Invalid carrier file: " + carrier);
            carriers.add(carrier);
            frontend.addCarrier(carrier.getName(), carrier.length());
        }
        updateTotalBytes(calculateTotalBytes());
        updateNeededBytes(calculateNeededBytes());
    }

    /**
     * Removes a carrier file at the specified index.
     *
     * @param index the index of the carrier to remove
     */
    public void selectCarrier(int indexSelected) {
        if (indexSelected < 0 && indexSelected > carriers.size())
            throw new IndexOutOfBoundsException("Invalid carrier index: " + indexSelected);
        selectedCarrierIndex = indexSelected;
    }

    /**
     * Deletes the currently selected carrier file.
     * If no carrier is selected, this method does nothing.
     */
    public void deleteCarrier() {
        if (selectedCarrierIndex < 0 || selectedCarrierIndex > carriers.size())
            return;
        carriers.remove(selectedCarrierIndex);
        frontend.removeCarrier(selectedCarrierIndex);
        selectedCarrierIndex = -1; // Reset selection
        updateTotalBytes(calculateTotalBytes());
        updateNeededBytes(calculateNeededBytes());
    }

    /**
     * Clears all carrier files from the backend.
     */
    public void clearCarriers() {
        carriers.clear();
        frontend.clearCarriers();
        selectedCarrierIndex = -1;
        updateTotalBytes(0);
        updateNeededBytes(calculateNeededBytes());
    }

    // #region Helper functions
    private void updateFileSize(long size) {
        frontend.setFileSize(size);
    }

    private void updateTotalBytes(long totalBytes) {
        frontend.setTotalBytes(totalBytes);
    }

    private void updateNeededBytes(long neededBytes) {
        frontend.setNeededBytes(neededBytes);
    }

    private long calculateTotalBytes() {
        long totalBytes = 0;
        for (File carrier : carriers) {
            if (carrier.exists() && carrier.isFile())
                totalBytes += carrier.length();
        }
        return totalBytes;
    }

    private long calculateNeededBytes() {
        long neededBytes = selectedFile != null ? selectedFile.length() : 0;
        neededBytes -= calculateTotalBytes();
        return neededBytes > 0 ? neededBytes : 0;
    }
    // #endregion

}
