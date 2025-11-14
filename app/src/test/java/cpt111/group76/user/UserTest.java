package cpt111.group76.user;

import static org.junit.Assert.*;
import org.junit.Test;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

public class UserTest {
    @Test
    public void testUserCreation() throws Exception {
        PremiumUser user = new PremiumUser("john_doe", "password123", new Watchlist("movie1;movie2;movie3".split(";", -1)), new History("".split(";", -1)));
        user.addToWatchlist("movieAdd");
        assertTrue(user.getWatchlist().contains("movieAdd"));
    }


    @Test
    public void testAddAndRemoveWatchlist() {
        PremiumUser user;
        try {
            user = new PremiumUser("alice", "mypas1sword");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        assertTrue(user.addToWatchlist("movieX"));
        assertFalse(user.addToWatchlist("movieX")); // Adding again should return false
    }


    @Test
    public void testRemoveFromHistory() {
        PremiumUser user;
        try {
            user = new PremiumUser("bob", "pass456");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        user.addToHistory("movieY");
        user.addToHistory("movieZ");
        assertTrue(user.removeFromHistory("movieY"));
        assertEquals(1, user.getHistory().length());
    }


    @Test
    public void testVerifyPassword() {
        PremiumUser user;
        try {
            user = new PremiumUser("charlie", "charl1iepass");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        assertTrue(user.verifyPassword("charl1iepass"));
        assertFalse(user.verifyPassword("wrongpass"));
        assertFalse(user.verifyPassword("charl1iepass1"));
    }


    @Test
    public void testReadHistory() {
        PremiumUser user;
        try {
            user = new PremiumUser("dave", "davep1ass");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        user.addToHistory("movie1");
        assertTrue(user.getHistory().contains("movie1"));
    }


    @Test
    public void testReadWatchlist() {
        PremiumUser user;
        try {
            user = new PremiumUser("eve", "evep1ass");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        user.addToWatchlist("movieA");
        assertTrue(user.getWatchlist().contains("movieA"));
    }


    @Test
    public void testCheckPassword() {
        try {
            new PremiumUser("bob", "123");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password must be at least 6 characters long.");
        }
        try {
            new PremiumUser("bob", "a".repeat(19));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password must be at most 18 characters long.");
        }
        try {
            new PremiumUser("bob", "abcdef");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password must contain at least one digit.");
        }
        try {
            new PremiumUser("bob", "abc123");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
