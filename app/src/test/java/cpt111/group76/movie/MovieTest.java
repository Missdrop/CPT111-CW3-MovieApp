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
        Movie movie = new Movie( "M011", "Inception", "Sci-Fi", -1, 8.8 );
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testNullId() {
        Movie movie = new Movie(null, "Test Title", "Drama", 2020, 5.5);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId() {
        Movie movie = new Movie("", "Test Title", "Drama", 2020, 5.5);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testBlankId() {
        Movie movie = new Movie("   ", "Test Title", "Drama", 2020, 5.5);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testNullTitle() {
        Movie movie = new Movie("M002", null, "Comedy", 2015, 7.2);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        Movie movie = new Movie("M002", "", "Comedy", 2015, 7.2);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testYearTooEarly() {
        Movie movie = new Movie("M003", "Old Movie", "Silent", 1879, 3.0);
        assertNull(movie);
    }

    
    @Test
    public void testYearAtEdge() {
        Movie earlyMovie = new Movie("M004", "First Movie", "Documentary", 1880, 4.5);
        assertEquals(1880, earlyMovie.getYear());

        int maxYear = java.time.Year.now().getValue() + 5;
        Movie futureMovie = new Movie("M005", "Future Film", "Sci-Fi", maxYear, 9.9);
        assertEquals(maxYear, futureMovie.getYear());
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testYearTooFarFuture() {
        int invalidYear = java.time.Year.now().getValue() + 6;
        Movie movie = new Movie("M006", "Too Future", "Fantasy", invalidYear, 8.0);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testRatingTooLow() {
        Movie movie = new Movie("M007", "Bad Movie", "Horror", 2023, -0.1);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testRatingTooHigh() {
        Movie movie = new Movie("M008", "Overrated", "Action", 2022, 10.1);
        assertNull(movie);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testMovieCreationInvalidRating() throws Exception {
        Movie movie = new Movie( "M011", "Inception", "Sci-Fi", 1982, -1.0 );
        assertNull(movie);
    }

    
    @Test
    public void testRatingEdgeValues() {
        Movie minRating = new Movie("M009", "Minimum", "Thriller", 2021, 0.0);
        assertEquals(0.0, minRating.getRating(), 0.001);

        Movie maxRating = new Movie("M010", "Maximum", "Adventure", 2020, 10.0);
        assertEquals(10.0, maxRating.getRating(), 0.001);
    }

    
    @Test
    public void testToCSVFormat() {
        Movie movie = new Movie("M012", "The Matrix", "Action,Sci-Fi", 1999, 8.7);
        String csv = movie.toCSV();
        assertEquals("M012,The Matrix,Action,Sci-Fi,1999,8.7", csv);
    }

    
    @Test
    public void testNullGenreHandling() {
        Movie movie = new Movie("M013", "Unknown Genre", null, 2018, 6.3);
        assertEquals("", movie.getGenre());
    }
}



    
