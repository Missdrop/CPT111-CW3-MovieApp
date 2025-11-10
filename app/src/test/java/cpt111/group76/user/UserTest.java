package cpt111.group76.user;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {
    @Test
    public void testUserCreation() throws Exception {
        String[] userData = {"john_doe", "password123", "movie1;movie2;movie3", "", "false"};
        User user = new User(userData);
        user.addToWatchlist("movieAdd");
        assertTrue(user.getWatchlist().contains("movieAdd"));
    }


    @Test
    public void testUserCreationEmptyWatchlist() throws Exception {
        String[] userData = {"jane_doe", "securepass", "", "movieA;movieB", "false"};
        User user = new User(userData);
        assertEquals(0, user.getWatchlist().length());
        assertEquals(2, user.getHistory().length());
    }


    @Test
    public void testAddAndRemoveWatchlist() {
        User user = new User("alice", "mypassword", false);
        assertTrue(user.addToWatchlist("movieX"));
        assertFalse(user.addToWatchlist("movieX")); // Adding again should return false
    }


    @Test
    public void testRemoveFromHistory() {
        User user = new User("bob", "pass456", false);
        user.addToHistory("movieY");
        user.addToHistory("movieZ");
        assertTrue(user.removeFromHistory("movieY"));
        assertEquals(1, user.getHistory().length());
    }


    @Test
    public void testVerifyPassword() {
        User user = new User("charlie", "charliepass", false);
        assertTrue(user.verifyPassword("charliepass"));
        assertFalse(user.verifyPassword( "wrongpass"));
        assertFalse(user.verifyPassword( "charliepass1"));
    }


    @Test
    public void testReadHistory() {
        User user = new User("david", "davidpass", false);
        user.addToHistory("movie1");
        assertTrue(user.getHistory().contains("movie1"));
    }


    @Test
    public void testReadWatchlist() {
        User user = new User("eve", "evepass", false);
        user.addToWatchlist("movieA");
        assertTrue(user.getWatchlist().contains("movieA"));
    }
}
