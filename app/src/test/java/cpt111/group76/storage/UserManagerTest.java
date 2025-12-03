package cpt111.group76.storage;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class UserManagerTest {
    @BeforeClass
    public static void initTestCsvFile() {
        try (FileManager userFileManager = new FileManager("resources/users.csv")) {
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
    }


    @Test
    public void testGetUsers() {
        try (UserManager userManager = new UserManager()) {
            assertEquals(userManager.getUser("alice").getUsername(), "alice");
        }
    }


    @Test
    public void testAddUser() {
        try (UserManager userManager = new UserManager()){
            userManager.addUser("naipu","123a123");
            assertEquals(userManager.getUser("naipu").getUsername(), "naipu");
            userManager.deleteUser("naipu");
        } catch (Exception e) {
            fail("Add user method threw an exception: " + e.getMessage());
        }
    }


    @Test
    public void testAddExistUser() {
        try (UserManager userManager = new UserManager()) {
            userManager.addUser("alice","123a123");
            fail("Expected exception for existing username was not thrown.");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username already exists.");
        }
    }


    @Test
    public void testSave() {
        try (UserManager userManager = new UserManager()) {
            userManager.addUser("naipu","123a123");
            userManager.save();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Reload to verify
        try (UserManager userManager2 = new UserManager()) {
            assertEquals(userManager2.getUser("naipu").getUsername(), "naipu");
            userManager2.deleteUser("naipu");
            userManager2.save();
        } catch (Exception e) {
            fail("Save method threw an exception: " + e.getMessage());
        }
    }


    @Test
    public void testCheckUsername() {
        try (UserManager userManager = new UserManager()) {
            try {
                userManager.addUser("ab", "somePassw1ord");
            } catch (Exception e) {
                assertEquals(e.getMessage(), "Username must be between 3 and 20 characters long.");
            }
            try {
                userManager.addUser("a".repeat(21), "somePass1word");
            } catch (Exception e) {
                assertEquals(e.getMessage(), "Username must be between 3 and 20 characters long.");
            }
            try {
                userManager.addUser("user!name", "somePass1word");
            } catch (Exception e) {
                assertEquals(e.getMessage(), "Username can only contain letters and digits.");
            }
            try {
                userManager.addUser("validUser123", "somePassw1ord");
            } catch (Exception e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testCheckExistingUsername() {
        try (UserManager userManager = new UserManager()) {
            try {
                userManager.addUser("alice", "somePassword");
                fail("Expected exception for existing username was not thrown.");
            } catch (Exception e) {
                assertEquals(e.getMessage(), "Username already exists.");
            }
        }
    }
}
