package cpt111.group76.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is responsible for reading data from a csv file.
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


    private Scanner getScanner() {
        try {
            return new Scanner(this.file);
        } catch (java.io.FileNotFoundException e) {
            // return null to indicate the file couldn't be opened
            return null;
        }
    }


    public boolean flushScanner() {
        if (this.scanner != null) {
            this.scanner.close();
        }
        this.scanner = getScanner();
        return this.scanner != null;
    }


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


    public boolean hasNextLine() {
        if (this.scanner == null) {
            return false;
        }

        return this.scanner.hasNextLine();
    }


    public boolean save(String header, String[] rows) {
        if (this.file == null || rows == null)
            return false;
        // Ensure parent dirs exist
        File parent = this.file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

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
}
