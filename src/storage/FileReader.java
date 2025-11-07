package storage;

import java.io.File;
import java.util.Scanner;

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


    public String[] readLine() {
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

}
