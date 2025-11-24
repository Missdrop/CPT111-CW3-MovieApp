package cpt111.group76.storage;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import cpt111.group76.movie.Movie;

/**
 * Manages the movie database including loading, saving, adding, and deleting movies.
 * Extends FileManager to handle CSV file operations for movie data.
 */
public class MovieManager extends FileManager {
    private Map<String, Movie> movies;
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


    public List<Movie> getMovieList() {
        return List.copyOf(movies.values());
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


    /**
     * Adds a movie to the database with auto-generated ID.
     *
     * @param title the title of the movie
     * @param genre the genre of the movie
     * @param year the release year of the movie
     * @param rating the rating of the movie
     * @throws IllegalArgumentException if the movie already exists or parameters are invalid
     */
    public void addMovie(String title, String genre, int year, double rating) throws IllegalArgumentException {
        String movieId = String.format("M%03d", maxIndex + 1);
        Movie movie = new Movie(movieId, title, genre, year, rating);
        // check for duplicate movie ID (which should not happen with auto-generated IDs)
        if (movies.containsKey(movie.getId())) {
            throw new IllegalArgumentException("Movie ID already exists");
        }
        // check for duplicate movie based on title, year, and genre
        for (Movie m : getMovieList()) {
            if (m.getTitle().equalsIgnoreCase(title) && m.getYear() == year && m.getGenre().equalsIgnoreCase(genre)) {
                throw new IllegalArgumentException("Movie already exists");
            }
        }

        movies.put(movie.getId(), movie);

        maxIndex++;
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
     * @throws RuntimeException if an error occurs during saving
     */
    public void save() throws RuntimeException {
        String header = "id,title,genre,year,rating";
        
        List<Movie> movieList = getMovieList();
        String[] rows = new String[movieList.size()];
        for (int i = 0; i < movieList.size(); i++) {
            rows[i] = movieList.get(i).toCSV();
        }

        try {
            super.save(header, rows);
        } catch (Exception e) {
            throw new RuntimeException("Error saving movie database: " + e.getMessage());
        }
    }


    /**
     * Creates a Movie object from CSV data array.
     * Centralizes all CSV parsing logic in the manager class.
     *
     * @param movieData the CSV data array in format: [id, title, genre, year, rating]
     * @return the created Movie object
     * @throws IllegalArgumentException if the data is invalid or malformed
     */
    private static Movie createFromCSV(String[] movieData) throws IllegalArgumentException {
        if (movieData == null || movieData.length < 5) {
            throw new IllegalArgumentException("Movie data array must contain 5 elements");
        }
        
        try {
            String id = movieData[0] != null ? movieData[0].trim() : "";
            String title = movieData[1] != null ? movieData[1].trim() : "";
            String genre = movieData[2] != null ? movieData[2].trim() : "";

            // handle missing year and rating
            if (movieData[3] == null || movieData[3].trim().isEmpty()) {
                movieData[3] = "0";
            }
            if (movieData[4] == null || movieData[4].trim().isEmpty()) {
                movieData[4] = "0.0";
            }
            int year = Integer.parseInt(movieData[3].trim());
            double rating = Double.parseDouble(movieData[4].trim());

            try {
                return new Movie(id, title, genre, year, rating);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric format in movie data", e);
        }
    }


    /**
     * Loads movies from CSV file into a HashMap.
     * Handles invalid data gracefully by skipping problematic entries.
     *
     * @return Map containing all valid movies with IDs as keys
     */
    private Map<String, Movie> getMovies() {
        Map<String, Movie> movieMap = new HashMap<String, Movie>();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                this.nextLine(); // skip header
                continue;
            }

            String[] movieData = this.nextLine();

            // try to create movie from CSV data
            try {
                Movie movie = createFromCSV(movieData);
                if (movie != null) {
                    movieMap.put(movie.getId(), movie);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Skipping invalid movie data: " + e.getMessage());
                if (movieData != null && movieData.length > 0) {
                    System.out.println("Data: " + String.join(",", movieData));
                }
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
