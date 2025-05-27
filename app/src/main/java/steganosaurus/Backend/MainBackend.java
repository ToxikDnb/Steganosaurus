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

    private void selectFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            selectedFile = file;
            frontend.setFileName(file.getName());
            frontend.setFileSize(file.length());
            updateStatus(Status.IDLE);
        } else {
            frontend.setFileName("No file selected");
            frontend.setFileSize(0);
        }
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
            if (carrier.exists() && carrier.isFile()) {
                totalBytes += carrier.length();
            }
        }
        return totalBytes;
    }

    private long calculateNeededBytes() {
        long neededBytes = selectedFile != null ? selectedFile.length() : 0;
        neededBytes -= calculateTotalBytes();
        return neededBytes > 0 ? neededBytes : 0;
    }

}
