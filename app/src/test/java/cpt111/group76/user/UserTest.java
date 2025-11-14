package cpt111.group76.user;

import static org.junit.Assert.*;
import org.junit.Test;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

public class UserTest {
    @Test
    public void testUserCreation() throws Exception {
        User user = new User("john_doe", "password123", new Watchlist("movie1;movie2;movie3".split(";", -1)), new History("".split(";", -1)), false);
        user.addToWatchlist("movieAdd");
        assertTrue(user.getWatchlist().contains("movieAdd"));
    }


    @Test
    public void testAddAndRemoveWatchlist() {
        User user = new User("alice", "mypassword", false);
        assertTrue(user.addToWatchlist("movieX"));
        assertFalse(user.addToWatchlist("movieX")); // Adding again should
                                                    // return false
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
        assertFalse(user.verifyPassword("wrongpass"));
        assertFalse(user.verifyPassword("charliepass1"));
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
