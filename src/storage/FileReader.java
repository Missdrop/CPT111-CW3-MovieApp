package storage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/* This class is responsible for reading data from a csv file. */
public class FileReader {
    private File file;

    public FileReader() {
        throw new UnsupportedOperationException("File must be specified");
    }

    public FileReader(File file) {
        this.file = file;
    }

    public FileReader(String filePath) {
        this(new File(filePath));
    }

}
