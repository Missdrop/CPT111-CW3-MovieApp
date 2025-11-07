package storage;

import static org.junit.Assert.*;

import org.junit.Test;


public class FileManagerTest {
    @Test
    public void testFileReaderInitialization() {
        FileManager fileReader = new FileManager("resources/movies.csv");
        assertNotNull(fileReader.nextLine());
    }


    @Test
    public void testSaveFunction() {
        FileManager fileManager = new FileManager("resources/test_save.csv");
        String header = "col1,col2,col3";
        String[] rows = new String[] {
            "data1,data2,data3",
            "data4,data5,data6"
        };
        assertTrue(fileManager.save(header, rows));
        fileManager.flushScanner();
        assertEquals("col1", fileManager.nextLine()[0]);
        fileManager.close();
        java.io.File file = new java.io.File("resources/test_save.csv");
        file.delete();

    }
}
