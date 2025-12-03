package cpt111.group76.storage;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class FileManagerTest {
    @BeforeClass
    public static void initTestCsvFile() {
        MovieManagerTest.initTestCsvFile();
        UserManagerTest.initTestCsvFile();
    }


    @Test
    public void testFileReaderInitialization() {
        try (FileManager fileManager = new FileManager("resources/movies.csv")) {
            assertNotNull(fileManager);
            assertNotNull(fileManager.nextLine());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void emptyStringConstructorTest() {
        try (FileManager fileManager = new FileManager("")) {
            assertNotNull(fileManager);
            assertNull(fileManager.nextLine());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testSaveFunction() {
        try (FileManager fileManager = new FileManager("resources/test_save.csv")) {
            assertNotNull(fileManager);
            String header = "col1,col2,col3";
            String[] rows = new String[] {
                "data1,data2,data3",
                "data4,data5,data6"
            };
            fileManager.save(header, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try (FileManager fileManager = new FileManager("resources/test_save.csv")) {
            assertNotNull(fileManager);
            assertEquals("col1", fileManager.nextLine()[0]);
            assertEquals("data2", fileManager.nextLine()[1]);
            assertEquals("data6", fileManager.nextLine()[2]);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        File file = new File("resources/test_save.csv");
        file.delete();
    }
}
