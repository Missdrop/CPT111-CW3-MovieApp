package movie;

import org.junit.Test;
import static org.junit.Assert.*;

public class MovieTest {
    @Test
    public void testMovieCreation() {
        String[] movieData = {"movie1", "Inception", "Sci-Fi", "1481", "8.8"};
        Movie movie = new Movie(movieData);
        assertEquals("Inception", movie.getTitle());
        assertEquals(1481, movie.getYear());
    }


    @Test
    public void testMovieCreationEmptyElements() {
        String[] movieData = {"", "", "", "", ""};
        Movie movie = new Movie(movieData);
        assertEquals("", movie.getTitle());
        assertEquals(0, movie.getYear());
    }

    
}
