package cpt111.group76.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Handles basic file I/O operations for CSV files.
 * Provides methods for reading and writing CSV data with proper exception handling.
 */
class FileManager {
    private File file;
    private Scanner scanner;


    public FileManager(File file) {
        this.file = file;
        this.scanner = getScanner();
    }


    public FileManager(String filePath) {
        this(new File(filePath));
    }


    /**
     * Reads the next line from the CSV file and splits it into an array by commas.
     *
     * @return array of strings from the CSV line, or null if no more lines
     */
    public String[] nextLine() {
        if (this.scanner == null) {
            return null;
        }

        if (this.scanner.hasNextLine()) {
            String line = this.scanner.nextLine();
            return line.split(",", -1); // -1 to include trailing empty strings
        } else {
            return null;
        }
    }


    /**
     * Checks if there are more lines to read from the file.
     *
     * @return true if more lines are available, false otherwise
     */
    public boolean hasNextLine() {
        if (this.scanner == null) {
            return false;
        }

        return this.scanner.hasNextLine();
    }


    /**
     * Saves data to the CSV file with the specified header and rows.
     * Creates the file and parent directories if they don't exist.
     *
     * @param header the header line for the CSV file
     * @param rows the data rows to write to the file
     * @return true if save was successful, false otherwise
     */
    public boolean save(String header, String[] rows) {
        if (this.file == null || rows == null)
            return false;
        // Ensure parent dirs exist
        File parent = this.file.getParentFile();
        try {
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        } catch (SecurityException e) {
            return false;
        }

        // use try-with-resources to ensure the writer is closed properly
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, false))) {
            bw.write(header);
            bw.newLine();
            for (String row : rows) {
                bw.write(row);
                bw.newLine();
            }
            bw.flush();
            return true;
        } catch (IOException e) {
            return false;
        }

    }


    public void close() {
        if (this.scanner != null) {
            this.scanner.close();
        }
        this.scanner = null;
        this.file = null;
    }

    /**
     * Gets a new Scanner instance for the file.
     *
     * @return Scanner for the file, or null if file cannot be opened
     */
    private Scanner getScanner() {
        try {
            return new Scanner(this.file);
        } catch (FileNotFoundException e) {
            // return null to indicate the file couldn't be opened
            return null;
        }
    }
}
