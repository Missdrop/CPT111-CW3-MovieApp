package storage;

import static org.junit.Assert.*;
import org.junit.Test;
import user.User;

public class UserManagerTest {
    @Test
    public void testGetUsers() {
        UserManager userManager = new UserManager();
        assertEquals(userManager.getUser("alice").getPassword(), "alice123");
    }


    @Test
    public void testAddUser() {
        UserManager userManager = new UserManager();
        user.User newUser = new User("naipu","123123",java.util.List.of(),java.util.List.of());
        assertTrue(userManager.addUser(newUser));
        assertEquals(userManager.getUser("naipu").getPassword(), "123123");
        userManager.deleteUser("naipu");
    }


    @Test
    public void testAddExistUser() {
        UserManager userManager = new UserManager();
        user.User newUser = new User("alice","newpass",java.util.List.of(),java.util.List.of());
        assertFalse(userManager.addUser(newUser));

    }
}
