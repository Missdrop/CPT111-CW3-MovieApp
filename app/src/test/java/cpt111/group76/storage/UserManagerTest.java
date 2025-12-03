package cpt111.group76.storage;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class UserManagerTest {
    @BeforeClass
    public static void initTestCsvFile() {
        FileManager userFileManager = new FileManager("resources/users.csv");
        try {
            userFileManager.save("username,password,watchlist,history,usertype", new String[] {
            "bob,-527b97a3,M044;M043;M042;M041,M040@2025-06-21;M041@2025-07-02;M042@2025-07-31,Basic",
            "eric,-58acc5b5,M091;M094;M093;M092,M091@2025-07-19;M096@2025-08-12;M099@2025-09-12,Basic",
            "alice,664197b2,M048;M015;M056;M008;M071,M001@2025-07-12;M011@2025-08-10;M015@2025-09-01,Basic",
            "diana,-ddb275d,M077;M076;M078,M029@2025-04-20;M076@2025-08-22;M098@2025-09-10,Basic",
            "charlie,5d4de980,M066;M065;M064;M063,M063@2025-05-14;M080@2025-08-08;M064@2025-09-03,Basic"
        });
        } catch (Exception e) {
            fail("Save method threw an exception: " + e.getMessage());
        }
        userFileManager.close();
    }


    @Test
    public void testGetUsers() {
        UserManager userManager = new UserManager();
        assertEquals("alice", userManager.getUser("alice").getUsername());
    }


    @Test
    public void testAddUser() {
        UserManager userManager = new UserManager();
        try {
            userManager.addUser("naipu","123a123");
        } catch (Exception e) {
            fail("Add user method threw an exception: " + e.getMessage());
        }
        assertEquals("naipu", userManager.getUser("naipu").getUsername());
        userManager.deleteUser("naipu");
    }


    @Test
    public void testAddExistUser() {
        UserManager userManager = new UserManager();
        try {
            userManager.addUser("alice","123a123");
            fail("Expected exception for existing username was not thrown.");
        } catch (Exception e) {
            assertEquals("Username already exists.", e.getMessage());
        }
    }


    @Test
    public void testSave() {
        UserManager userManager = new UserManager();
        try {
            userManager.addUser("naipu","123a123");
        } catch (Exception e) {
            fail("Add user method threw an exception: " + e.getMessage());
        }
        try {
            userManager.save();
        } catch (Exception e) {
            fail("Save method threw an exception: " + e.getMessage());
        }
        userManager.save();
        userManager.close();

        // Reload to verify
        UserManager userManager2 = new UserManager();
        assertEquals("naipu", userManager2.getUser("naipu").getUsername());

        userManager2.deleteUser("naipu");
        try {
            userManager2.save();
        } catch (Exception e) {
            fail("Save method threw an exception: " + e.getMessage());
        }
    }


    @Test
    public void testCheckUsername() {
        UserManager userManager = new UserManager();
        try {
            userManager.addUser("ab", "somePassw1ord");
        } catch (Exception e) {
            assertEquals("Username must be between 3 and 20 characters long.", e.getMessage());
        }
        try {
            userManager.addUser("a".repeat(21), "somePass1word");
        } catch (Exception e) {
            assertEquals("Username must be between 3 and 20 characters long.", e.getMessage());
        }
        try {
            userManager.addUser("user!name", "somePass1word");
        } catch (Exception e) {
            assertEquals("Username can only contain letters and digits.", e.getMessage());
        }
        try {
            userManager.addUser("validUser123", "somePassw1ord");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckExistingUsername() {
        UserManager userManager = new UserManager();
        try {
            userManager.addUser("alice", "somePassword");
            fail("Expected exception for existing username was not thrown.");
        } catch (Exception e) {
            assertEquals("Username already exists.", e.getMessage());
        }
    }
}
