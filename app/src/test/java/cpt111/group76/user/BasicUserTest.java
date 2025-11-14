package cpt111.group76.user;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasicUserTest {
    @Test
    public void testBasicUserCreation() throws Exception {
        BasicUser basicUser;
        try {
            basicUser = new BasicUser("test", "password123");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        User user = new BasicUser("test", "password123");
        assertTrue(user instanceof BasicUser);
        assertTrue(basicUser instanceof BasicUser);
    }

    @Test
    public void testAddToWatchlistWithinLimit() {
        User basicUser;
        try {
            basicUser = new BasicUser("basicUser", "secure1Pass");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        for (int i = 0; i < 20; i++) {
            assertTrue(basicUser.addToWatchlist("movie" + i));
        }
        assertFalse(basicUser.addToWatchlist("movie21"));
    }
}