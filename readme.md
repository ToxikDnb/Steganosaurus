# Steganosaurus

## Disclaimer

Steganography is a powerful tool that can be used for both legitimate and illegitimate purposes. The developer of Steganosaurus, Mackenzie, do not endorse or promote any illegal activities, and do not take responsibility for the misuse of this tool. Users are responsible for ensuring that their use of this software complies with all applicable laws and regulations in their jurisdiction.

## Introduction

Steganosaurus is a tool for creating and managing steganographic files. It allows users to hide data within images, audio files, and other media formats, providing a way to securely transmit information without drawing attention. It also allows users to extract hidden data from these files, and either save it to a file or print it to an output window.

## Features

-   Hide data within images, audio files, and other media formats.
-   Extract hidden data from files.
-   Save extracted data to a file or print it to an output window.
-   Supports various file formats for both hiding and extracting data.
-   User-friendly interface for easy navigation and operation.

## Data Format

Different data formats contain redundant data that can be manipulated for different purposes. One of these purposes is hiding information inside files. Each pixel of an image can store 1 byte of hidden data. This data is stored in the LSB of the pixel's RGB values. This barely affects the visible colours of an image, however still allows for a large amount of data to be stored. JPGs and PNGs both supported, however outputs will automatically become PNGs to maximise data storage.

The data is stored in a specific format. Each steganographic file starts with a header that contains the following information, followed by the hidden data itself:

-   **ID hash**: A unique SHA-256 hash of the contents of the hidden data, used to verify integrity and to match hidden data with its corresponding files (32 bytes).
-   **File name**: The name of the file containing the hidden data. (32 bytes)
-   **File type**: The type of the file (e.g., png, pdf) (8 bytes).
-   **Byte length**: The length of the hidden data in bytes. (4 bytes)
-   **File id**: The sequence number of the file in the steganographic sequence. This starts at 0, and is automatically calculated by the program to ensure that data is stored in the correct order. (4 bytes)

Since the header has a fixed size of 80 bytes, images must contain at least 81 pixels to be of any use (IE minimum 9x9 pixels). The program will automatically check the size of the image and will not allow images smaller than this size to be used for hiding data.

## Installation / Usage

This program is compiled as a JAR file, and as such requires Java 17 or later to run. Once this is installed, you can run the program by running the jar file from the command line:

```bash
java -jar steganosaurus.jar
```

Or alternatively, you can run the program by double-clicking the JAR file if your system is configured to do so.

## Planned Features

### Version 1.0

-   [ ] UI interface, containing upload points for files/text to be hidden, as well as managing images to hide them in.
-   [ ] Ability to hide data & files within images
-   [ ] Ability to extract hidden data from files.
-   [ ] Ability to save extracted data to a file.
-   [ ] Ability to print extracted data to an output window.

### Version 1.1

-   [ ] Ability to hide multiple files in a single image.
-   [ ] Ability to extract multiple files from a single image.

### Version 1.2

-   [ ] Ability to hide data within audio files.
-   [ ] Ability to extract data from audio files.