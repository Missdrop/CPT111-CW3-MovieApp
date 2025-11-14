package cpt111.group76.storage;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserManagerTest {
    public static void initTestCsvFile() {
        FileManager userFileManager = new FileManager("resources/users.csv");
        try {
            userFileManager.save("username,password,watchlist,history,premium", new String[] {
            "bob,-527b97a3,M044;M043;M042;M041,M040@2025-06-21;M041@2025-07-02;M042@2025-07-31,false",
            "eric,-58acc5b5,M091;M094;M093;M092,M091@2025-07-19;M096@2025-08-12;M099@2025-09-12,false",
            "alice,664197b2,M048;M015;M056;M008;M071,M001@2025-07-12;M011@2025-08-10;M015@2025-09-01,false",
            "diana,-ddb275d,M077;M076;M078,M029@2025-04-20;M076@2025-08-22;M098@2025-09-10,false",
            "charlie,5d4de980,M066;M065;M064;M063,M063@2025-05-14;M080@2025-08-08;M064@2025-09-03,false"
        });
        } catch (Exception e) {
            fail("Save method threw an exception: " + e.getMessage());
        }
        userFileManager.close();
    }


    @Test
    public void testGetUsers() {
        initTestCsvFile();
        UserManager userManager = new UserManager();
        assertEquals(userManager.getUser("alice").getUsername(), "alice");
    }


    @Test
    public void testAddUser() {
        UserManager userManager = new UserManager();
        assertTrue(userManager.addUser("naipu","123123"));
        assertEquals(userManager.getUser("naipu").getUsername(), "naipu");
        userManager.deleteUser("naipu");
    }


    @Test
    public void testAddExistUser() {
        UserManager userManager = new UserManager();
        assertFalse(userManager.addUser("alice","wrongpassword"));
    }


    @Test
    public void testSave() {
        UserManager userManager = new UserManager();
        userManager.addUser("naipu","123123");
        try {
            userManager.save();
        } catch (Exception e) {
            fail("Save method threw an exception: " + e.getMessage());
        }
        userManager.close();

        // Reload to verify
        UserManager userManager2 = new UserManager();
        assertEquals(userManager2.getUser("naipu").getUsername(), "naipu");

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
        assertEquals(userManager.checkUsername("ab"), "Username must be between 3 and 20 characters long.");
        assertEquals(userManager.checkUsername("a".repeat(21)), "Username must be between 3 and 20 characters long.");
        assertEquals(userManager.checkUsername("user!name"), "Username can only contain letters and digits.");
        assertNull(userManager.checkUsername("validUser123"));
    }


    @Test
    public void testCheckExistingUsername() {
        UserManager userManager = new UserManager();
        assertEquals(userManager.checkUsername("bob"), "Username already exists.");
    }


    @Test
    public void testCheckPassword() {
        UserManager userManager = new UserManager();
        assertEquals("Password must be at least 6 characters long.", userManager.checkPassword("123"));
        assertEquals("Password must contain at least one digit.", userManager.checkPassword("abcdef"));
        assertNull(userManager.checkPassword("abc123"));
    }
}
