package cpt111.group76.storage;

import java.util.HashMap;

import cpt111.group76.movie.Movie;

/**
 * Manages the movie database including loading, saving, adding, and deleting movies.
 * Extends FileManager to handle CSV file operations for movie data.
 */
public class MovieManager extends FileManager {
    private HashMap<String, Movie> movies;
    int maxIndex; // to track the highest movie index


    /**
     * Constructs a MovieManager and
     * loads movies from CSV file, path: resources/movies.csv,
     * and gets the maximum movie index.
     */
    public MovieManager() {
        super("resources/movies.csv");
        movies = getMovies();
        this.maxIndex = getMaxIndex();
    }


    public HashMap<String, Movie> getMovieDatabase() {
        return movies;
    }


    /**
     * Retrieves a movie by its ID.
     *
     * @param movieId the ID of the movie to retrieve
     * @return the Movie object, or null if not found
     */
    public Movie getMovie(String movieId) {
        return movies.get(movieId);
    }


    public boolean addMovie(Movie movie) {
        if (movies.containsKey(movie.getId())) {
            return false; // movie already exists
        }
        movies.put(movie.getId(), movie);
        return true;
    }


    /**
     * Adds a movie to the database with auto-generated ID.
     *
     * @return true if movie was added successfully, false if movie already exists
     */
    public boolean addMovie(String title, String genre, int year, double rating) {
        String movieId = String.format("M%03d", maxIndex + 1);
        Movie movie = new Movie(movieId, title, genre, year, rating);
        boolean added = addMovie(movie);
        if (added) {
            maxIndex++;
        }
        return added;
    }


    /**
     * Deletes a Movie object from the database.
     * @param movieId the ID of the Movie to delete
     * @return true if deleted successfully, false if movie doesn't exist
     */
    public boolean deleteMovie(String movieId) {
        if (movies.get(movieId) == null) {
            return false; // movie does not exist
        }

        movies.remove(movieId);
        return true;
    }


    public boolean deleteMovie(Movie movie) {
        return deleteMovie(movie.getId());
    }


    /**
     * Saves the current movie database to CSV file.
     *
     * @return true if save was successful, false otherwise
     */
    public boolean save(){
        String header = "id,title,genre,year,rating";
        
        String[] rows = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = (Movie) movies.values().toArray()[i];
            rows[i] = movie.toCSV();
        }

        return super.save(header, rows);
    }


    // Private helper methods
    private Movie createMovie(String[] movieData) {
        try {
            return new Movie(movieData);
        } catch (Exception e) {
            return null;
        }
    }


    private HashMap<String, Movie> getMovies() {
        HashMap<String, Movie> movieMap = new HashMap<String, Movie>();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                this.nextLine(); // skip header
                continue;
            }

            String[] movieData = this.nextLine();

            Movie movie = createMovie(movieData);
            if (movie != null) {
                movieMap.put(movie.getId(), movie);
            }
        }

        return movieMap;
    }


    private int idToIndex(String movieId) {
        try {
            return Integer.parseInt(movieId.substring(1));
        } catch (Exception e) {
            return -1;
        }
    }


    private int getMaxIndex() {
        int max = 0;
        for (String movieId : movies.keySet()) {
            int index = idToIndex(movieId);
            if (index > max) {
                max = index;
            }
        }
        return max;
    }


    @Override
    public void close() {
        super.close();
        movies.clear();
    }
}
