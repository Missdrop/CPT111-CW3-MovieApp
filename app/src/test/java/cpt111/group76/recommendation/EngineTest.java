package cpt111.group76.recommendation;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

import cpt111.group76.movie.Movie;
import cpt111.group76.user.PremiumUser;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.storage.MovieManagerTest;

public class EngineTest {
    @Test
    public void testEngineRecommendation() {
        MovieManagerTest.initTestCsvFile();
        MovieManager movieManager = new MovieManager();
        Map<String, Movie> movieDatabase = movieManager.getMovieDatabase();

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

        Engine engine = new Engine(movieDatabase, user);

        assertEquals(engine.recommendation("genre",3).length, 3);
        assertEquals(engine.recommendation("year",2).length, 2);
        assertEquals(engine.recommendation(4).length, 4);
    }
}
