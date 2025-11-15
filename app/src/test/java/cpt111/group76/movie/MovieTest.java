package cpt111.group76.movie;

import org.junit.Test;
import static org.junit.Assert.*;

public class MovieTest {
    @Test
    public void testMovieCreation() throws Exception {
        Movie movie = new Movie( "M011", "Inception", "Sci-Fi", 1982, 8.8 );
        assertEquals("Inception", movie.getTitle());
        assertEquals(1982, movie.getYear());
    }


    @Test
    public void testMovieCreationEmptyElements() throws Exception {
        Movie movie = new Movie( "M011", "Inception", "", 1982, 0.1 );
        assertEquals("", movie.getGenre());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testMovieCreationInvalidYear() throws Exception {
        Movie movie = new Movie( "M011", "Inception", "Sci-Fi", 1800, 8.8 );
        assertNull(movie);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testMovieCreationInvalidRating() throws Exception {
        Movie movie = new Movie( "M011", "Inception", "Sci-Fi", 1982, 1.11 );
        assertNull(movie);
    }
}
