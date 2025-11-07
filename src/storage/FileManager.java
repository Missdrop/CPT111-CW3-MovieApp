package storage;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for reading data from a csv file.
 */
protected class FileManager {
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
            return line.split(",");
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


    public String[] readRow(int rowIndex) {
        if (this.file == null) {
            return null;
        }
        if (!flushScanner())
            return null;
        int idx = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (idx == rowIndex) {
                return line.split(",");
            }
            idx++;
        }
        return null;
    }


    public String[] readColumn(int columnIndex) {
        if (this.file == null) {
            return null;
        }
        List<String> column = new ArrayList<>();
        if (!flushScanner())
            return null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (columnIndex < parts.length) {
                column.add(parts[columnIndex]);
            } else {
                column.add(null);
            }
        }
        return column.toArray(new String[0]);
    }


    /**
     * Append a row to the CSV file. The row is an array of column values.
     * Returns true on success, false otherwise.
     */
    public boolean appendRow(String[] row) {
        if (this.file == null || row == null)
            return false;
        // Ensure parent dirs exist
        File parent = this.file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, true))) {
            bw.write(String.join(",", row));
            bw.newLine();
            bw.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * Delete a row from the CSV file by its index (0-based).
     * Returns true on success, false otherwise.
     */
    public boolean deleteRow(int rowIndex) {
        if (this.file == null) {
            return false;
        }

        List<String> lines = new ArrayList<>();

        if (!flushScanner())
            return false;
        int index = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (index != rowIndex) {
                lines.add(line);
            }
            index++;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, false))) {
            for (String line : lines) {
                bw.write(line);
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
    }
}
