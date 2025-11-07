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
public class FileReader {
    private File file;
    private Scanner scanner;

    public FileReader() {
        throw new UnsupportedOperationException("File must be specified");
    }

    public FileReader(File file) {
        this.file = file;
        this.scanner = getScanner();
    }

    public FileReader(String filePath) {
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
        try (Scanner s = getScanner()) {
            if (s == null) return null;
            int idx = 0;
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (idx == rowIndex) {
                    return line.split(",");
                }
                idx++;
            }
            return null;
        }
    }

    public String[] readColumn(int columnIndex) {
        if (this.file == null) {
            return null;
        }
        List<String> column = new ArrayList<>();
        try (Scanner s = getScanner()) {
            if (s == null) return null;
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] parts = line.split(",");
                if (columnIndex < parts.length) {
                    column.add(parts[columnIndex]);
                } else {
                    column.add(null);
                }
            }
        }
        return column.toArray(new String[0]);
    }

    /**
     * Append a row to the CSV file. The row is an array of column values.
     * Returns true on success, false otherwise.
     */
    public boolean appendRow(String[] row) {
        if (this.file == null || row == null) return false;
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
        try (Scanner s = getScanner()) {
            if (s == null) return false;
            int index = 0;
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (index != rowIndex) {
                    lines.add(line);
                }
                index++;
            }
        } catch (Exception e) {
            return false;
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


    /**
     * Escape a field for CSV output: wrap in quotes if necessary and double any quotes inside.
     */
    private String escapeCsvField(String field) {
        if (field == null) return "";
        boolean needQuotes = field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r");
        String escaped = field.replace("\"", "\"\"");
        if (needQuotes) {
            return "\"" + escaped + "\"";
        } else {
            return escaped;
        }
    }

    public void close() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}
