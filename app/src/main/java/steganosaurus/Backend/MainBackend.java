package steganosaurus.Backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import steganosaurus.GUI.MainWindow;

public class MainBackend {

    // Status codes for backend process
    private enum Status {
        IDLE,
        RUNNING,
        DONE,
    }

    // Mode for backend process
    private enum Mode {
        ENCRYPTION,
        DECRYPTION,
    }

    // Constants
    public static final int HEADER_SIZE = 84;
    // Private variables
    private File selectedFile;
    private ArrayList<File> carriers = new ArrayList<File>();
    private MainWindow frontend;
    private int selectedCarrierIndex = -1;
    private Mode mode = Mode.ENCRYPTION;

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

    public String getMode() {
        if (mode == Mode.ENCRYPTION)
            return "encrypted";
        return "decrypted";
    }

    /**
     * Runs the main backend process to encrypt the file. It will return a true or
     * false depending on whether the process was successful or not.
     * 
     * @return true if the process was successful, false otherwise
     */
    public File run() throws IOException, SteganException {
        if (mode == Mode.ENCRYPTION) {
            byte fileData[] = Files.readAllBytes(selectedFile.toPath());
            ArrayList<Byte> fileDataList = new ArrayList<Byte>();
            // Converted to List for easier manipulation
            for (byte b : fileData) {
                fileDataList.add(b);
            }
            // for each file
            for (int i = 0; i < carriers.size(); i++) {
                File carrier = carriers.get(i);
                // Calculate the space available in bytes for that specific carrier
                BufferedImage image = ImageIO.read(carrier);
                if (image == null)
                    throw new SteganException("Cannot read image from carrier file: " + carrier.getName());
                int width = image.getWidth();
                int height = image.getHeight();
                long totalPixels = width * height;
                long availableBytes = totalPixels - HEADER_SIZE;
                // If carrier is too small, throw a Steganexception
                if (availableBytes <= 0)
                    throw new SteganException("Carrier file " + carrier.getName() + " is too small to hold any data.");
                // Create a header for that file
                byte[] header = createHeader(carrier, i);
                ArrayList<Byte> headerList = new ArrayList<Byte>();
                for (byte b : header) {
                    headerList.add(b);
                }
                ArrayList<Byte> fileDataToWrite = new ArrayList<Byte>();
                fileDataToWrite.addAll(headerList);
                fileDataToWrite.addAll(fileDataList);
                // For each pixel in the image
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // Get the pixel colour
                        int pixel = image.getRGB(x, y);
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;
                        int a = (pixel >> 24) & 0xFF;

                        // Store 2 bits of data in each colour channel
                        int toWrite = fileDataToWrite.remove(0) & 0xFF;
                        b = (b & 0xFC) | (toWrite & 0x03);
                        g = (g & 0xFC) | ((toWrite >> 2) & 0x03);
                        r = (r & 0xFC) | ((toWrite >> 4) & 0x03);
                        a = (a & 0xFC) | ((toWrite >> 6) & 0x03);

                        // Set the modified pixel back to the image
                        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);

                        // If we have no more data to write, break out of the loop
                        if (fileDataToWrite.isEmpty())
                            break;
                    }
                    // Same here
                    if (fileDataToWrite.isEmpty())
                        break;
                }
                // Outside the loop, add this carrier to the encrypted files list
                File encryptedFile = new File(carrier.getParent(), "encrypted_" + carrier.getName());
                ImageIO.write(image, "png", encryptedFile);
            }
            return carriers.get(0).getParentFile();
        }
        return null; // Placeholder for decryption logic
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

    private byte[] hashFile(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            return digest.digest(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file for SHA-256 hash", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private byte[] createHeader(File file, int fileId) {
        byte[] idHash = hashFile(file);
        // Process fileName
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String fileExtension = "";
        if (dotIndex != -1) {
            fileName = fileName.substring(0, dotIndex);
            fileExtension = file.getName().substring(dotIndex + 1);
        }
        fileName = fileName.length() > 32 ? fileName.substring(0, 32) : fileName;
        // Pad fileName to 32 bytes
        byte[] fileNameBytes = new byte[32];
        System.arraycopy(fileName.getBytes(), 0, fileNameBytes, 0, Math.min(fileName.length(), 32));
        // Pad fileExtension to 8 bytes
        byte[] fileExtensionBytes = new byte[8];
        System.arraycopy(fileExtension.getBytes(), 0, fileExtensionBytes, 0, Math.min(fileExtension.length(), 8));
        // Get file size as 64-bit integer
        long fileSize = file.length();
        byte[] fileSizeBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            fileSizeBytes[i] = (byte) (fileSize >> (8 * (7 - i)));
        }
        // Get file ID as 32-bit integer
        byte[] fileIdBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            fileIdBytes[i] = (byte) (fileId >> (8 * (3 - i)));
        }
        // Create the header
        byte[] header = new byte[84];
        System.arraycopy(idHash, 0, header, 0, idHash.length);
        System.arraycopy(fileNameBytes, 0, header, 32, fileNameBytes.length);
        System.arraycopy(fileExtensionBytes, 0, header, 64, fileExtensionBytes.length);
        System.arraycopy(fileSizeBytes, 0, header, 72, fileSizeBytes.length);
        System.arraycopy(fileIdBytes, 0, header, 80, fileIdBytes.length);
        return header;
    }

    // #endregion

}
