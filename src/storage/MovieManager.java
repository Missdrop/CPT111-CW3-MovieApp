package storage;

import java.util.Map;

import movie.Movie;

public class MovieManager extends FileManager {
    private Map<String, Movie> movies;


    public MovieManager() {
        super("resources/movies.csv");
        this.movies = getMovies();
    }


    private Movie createMovie(String[] movieData) {
        return new Movie(movieData);
    }


    private Map<String, Movie> getMovies() {
        Map<String, Movie> movieMap = new java.util.HashMap<>();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                this.nextLine(); // skip header
                continue;
            }

            String[] movieData = this.nextLine();
            Movie movie = createMovie(movieData);
            movieMap.put(movie.getId(), movie);
        }

        return movieMap;
    }


    public Movie getMovie(String movieId) {
        return this.movies.get(movieId);
    }


    public boolean appendRow(Movie movie) {
        String[] row = new String[] {
            movie.getId(),
            movie.getTitle(),
            Integer.toString(movie.getYear()),
            movie.getGenre(),
            Double.toString(movie.getRating())
        };
        return super.appendRow(row);
    }


    public boolean addMovie(Movie movie) {
        if (this.movies.containsKey(movie.getId())) {
            return false; // movie already exists
        }
        boolean appendResult = this.appendRow(movie);
        if (appendResult) {
            this.movies.put(movie.getId(), movie);
        }
        return appendResult;
    }


    private boolean deleteRow(String movieId) {
        if (!this.movies.containsKey(movieId)) {
            return false; // movie does not exist
        }

        // Find the row index of the movie to delete
        int rowIndex = 0;

        this.flushScanner();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                this.nextLine(); // skip header
                rowIndex++;
                continue;
            }

            String[] movieData = this.nextLine();
            if (movieData[0].equals(movieId)) {
                return super.deleteRow(rowIndex);
            }
            rowIndex++;
        }
        return false;
    }


    public boolean deleteMovie(String movieId) {
        if (this.movies.get(movieId) == null) {
            return false; // movie does not exist
        }
        boolean deleteResult = this.deleteRow(movieId);
        if (deleteResult) {
            this.movies.remove(movieId);
        }
        return deleteResult;
    }


    public boolean deleteMovie(Movie movie) {
        return deleteMovie(movie.getId());
    }


    @Override
    public void close() {
        super.close();
        this.movies.clear();
    }
}
