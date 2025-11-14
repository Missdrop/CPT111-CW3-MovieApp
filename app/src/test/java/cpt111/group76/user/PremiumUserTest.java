package cpt111.group76.user;

import org.junit.Test;
import static org.junit.Assert.*;

public class PremiumUserTest {
    @Test
    public void testPremiumUserCreation() throws Exception {
        try {
            PremiumUser premiumUser = new PremiumUser("test", "password123");
            User user = new PremiumUser("test", "password123");
            assertTrue(user instanceof PremiumUser);
            assertTrue(premiumUser instanceof PremiumUser);
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddToWatchlistWithinLimit() {
        try {
            PremiumUser premiumUser = new PremiumUser("premiumUser", "secure1Pass");
            for (int i = 0; i < 100; i++) {
                assertTrue(premiumUser.addToWatchlist("movie" + i));
            }
            assertFalse(premiumUser.addToWatchlist("movie101"));
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
        }

    }
}