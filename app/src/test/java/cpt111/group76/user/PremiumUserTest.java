package cpt111.group76.user;

import org.junit.Test;
import static org.junit.Assert.*;

public class PremiumUserTest {
    @Test
    public void testPremiumUserCreation() throws Exception {
        PremiumUser premiumUser = new PremiumUser("test", "password123");
        User user = new PremiumUser("test", "password123");
        assertTrue(user instanceof PremiumUser);
        assertTrue(premiumUser instanceof PremiumUser);
    }

    @Test
    public void testAddToWatchlistWithinLimit() {
        User premiumUser = new PremiumUser("premiumUser", "securePass");
        for (int i = 0; i < 100; i++) {
            assertTrue(premiumUser.addToWatchlist("movie" + i));
        }
        assertFalse(premiumUser.addToWatchlist("movie101"));
    }
}