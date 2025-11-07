package user;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {
    @Test
    public void testUserCreation() {
        String[] userData = {"john_doe", "password123", "movie1;movie2;movie3", ""};
        User user = new User(userData);
        user.appendWatchlist("movieAdd");
        assertTrue(user.getWatchlist().contains("movieAdd"));
    }


    @Test
    public void testUserCreationEmptyWatchlist() {
        String[] userData = {"jane_doe", "securepass", "", "movieA;movieB"};
        User user = new User(userData);
        assertEquals(0, user.getWatchlist().size());
        assertEquals(2, user.getHistory().size());
    }

}
