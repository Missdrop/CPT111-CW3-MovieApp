package storage;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;


public class FileReaderTest {
    @Test
    public void testFileReaderInitialization() {
        FileReader fileReader = new FileReader("resources/movies.csv");
        assertNotNull(fileReader.readLine());
    }
}
