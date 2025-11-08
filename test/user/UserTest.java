package user;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {
    @Test
    public void testUserCreation() {
        String[] userData = {"john_doe", "password123", "movie1;movie2;movie3", ""};
        User user = new User(userData);
        user.addToWatchlist("movieAdd");
        assertTrue(user.getWatchlist().contains("movieAdd"));
    }


    @Test
    public void testUserCreationEmptyWatchlist() {
        String[] userData = {"jane_doe", "securepass", "", "movieA;movieB"};
        User user = new User(userData);
        assertEquals(0, user.getWatchlist().size());
        assertEquals(2, user.getHistory().size());
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
        assertEquals(1, user.getHistory().size());
    }

}
