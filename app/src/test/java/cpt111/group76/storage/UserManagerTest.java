package cpt111.group76.storage;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;

import cpt111.group76.user.User;
import cpt111.group76.storage.UserManager;


public class UserManagerTest {
    @Test
    public void testGetUsers() {
        UserManager userManager = new UserManager();
        assertEquals(userManager.getUser("alice").getUsername(), "alice");
    }


    @Test
    public void testAddUser() {
        UserManager userManager = new UserManager();
        User newUser = new User("naipu","123123",new HashSet<String>(),new ArrayList<String>(), false);
        assertTrue(userManager.addUser(newUser));
        assertEquals(userManager.getUser("naipu").getUsername(), "naipu");
        userManager.deleteUser("naipu");
    }


    @Test
    public void testAddExistUser() {
        UserManager userManager = new UserManager();
        User newUser = new User("alice","newpass",new HashSet<String>(),new ArrayList<String>(), false);
        assertFalse(userManager.addUser(newUser));
    }


    @Test
    public void testSave() {
        UserManager userManager = new UserManager();
        User newUser = new User("naipu","123123",new HashSet<String>(),new ArrayList<String>(), false);
        userManager.addUser(newUser);
        assertTrue(userManager.save());
        userManager.close();

        // Reload to verify
        UserManager userManager2 = new UserManager();
        assertEquals(userManager2.getUser("naipu").getUsername(), "naipu");

        userManager2.deleteUser("naipu");
        userManager2.save();
    }
}
