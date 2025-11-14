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
     * @throws RuntimeException if an error occurs during saving
     */
    public void save() throws RuntimeException {
        String header = "id,title,genre,year,rating";
        
        String[] rows = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = (Movie) movies.values().toArray()[i];
            rows[i] = movie.toCSV();
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
            
            // super key validation
            if (id.isEmpty()) {
                throw new IllegalArgumentException("Movie ID cannot be empty");
            }
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Movie title cannot be empty");
            }
            
            return new Movie(id, title, genre, year, rating);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric format in movie data", e);
        }
    }


    /**
     * Loads movies from CSV file into a HashMap.
     * Handles invalid data gracefully by skipping problematic entries.
     *
     * @return HashMap containing all valid movies with IDs as keys
     */
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

            // try to create movie from CSV data
            try {
                Movie movie = createFromCSV(movieData);
                if (movie != null) {
                    movieMap.put(movie.getId(), movie);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Skipping invalid movie data: " + e.getMessage());
                if (movieData != null && movieData.length > 0) {
                    System.err.println("Data: " + String.join(",", movieData));
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
