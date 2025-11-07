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

}
