package cpt111.group76.recommendation;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.BeforeClass;

import cpt111.group76.user.PremiumUser;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.storage.MovieManagerTest;

public class EngineTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        MovieManagerTest.initTestCsvFile();
    }


    @Test
    public void testEngineRecommendation() {
        MovieManager movieManager = new MovieManager();
        PremiumUser user;
        try {
            user = new PremiumUser("testuser", "pass123");
        } catch (Exception e) {
            fail("User creation threw an exception: " + e.getMessage());
            return;
        }
        user.getHistory().add("M037");
        user.getHistory().add("M038");
        user.getHistory().add("M039");
        user.getHistory().add("M040");

        Engine engine = new Engine(movieManager, user);

        assertEquals(engine.recommendation("genre",3).size(), 3);
        assertEquals(engine.recommendation("year",2).size(), 2);
        assertEquals(engine.recommendation(4).size(), 4);
    }
}
