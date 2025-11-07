package storage;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class FileManagerTest {
    @Test
    public void testFileReaderInitialization() {
        FileManager fileReader = new FileManager("resources/movies.csv");
        assertNotNull(fileReader.nextLine());
    }
    @Test
    public void testFileAppendRow() {
        FileManager fileReader = new FileManager("resources/movies.csv");
        String[] newRow = {"9999", "Test Movie", "2024", "Drama", "7.5"};
        boolean appendResult = fileReader.appendRow(newRow);
        assert(appendResult);

        // Verify the last line is the newly appended row
        String[] lastLine = null;
        int lineIndex = 0;
        while (fileReader.hasNextLine()) {
            lineIndex++;
            lastLine = fileReader.nextLine();
        }
        assertNotNull(lastLine);
        assert(lastLine[0].equals("9999"));
        assert(lastLine[1].equals("Test Movie"));
        assert(lastLine[2].equals("2024"));
        assert(lastLine[3].equals("Drama"));
        assert(lastLine[4].equals("7.5"));
        fileReader.deleteRow(lineIndex - 1);
        fileReader.close();

    }
}
