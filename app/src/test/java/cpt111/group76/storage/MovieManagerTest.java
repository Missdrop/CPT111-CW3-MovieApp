package cpt111.group76.storage;

import static org.junit.Assert.*;
import org.junit.Test;

import cpt111.group76.movie.Movie;

public class MovieManagerTest {
    @Test
    public void testGetMovies() {
        MovieManager movieManager = new MovieManager();
        assertEquals(movieManager.getMovie("M001").getTitle(), "The Shawshank Redemption");
    }


    @Test
    public void testAddMovie() {
        MovieManager movieManager = new MovieManager();
        Movie newMovie = new Movie("M999", "naipu", "Comedy", 2006,2.5);
        assertTrue(movieManager.addMovie(newMovie));
        assertEquals(movieManager.getMovie("M999").getTitle(), "naipu");
        movieManager.deleteMovie("M999");
    }


    @Test
    public void testAddExistMovie() {
        MovieManager movieManager = new MovieManager();
        Movie newMovie = new Movie("M001", "The Shawshank Redemption", "Drama", 1994, 9.3);
        assertFalse(movieManager.addMovie(newMovie));
    }
}
