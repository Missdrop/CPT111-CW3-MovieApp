package cpt111.group76.storage;

import java.util.HashMap;

import cpt111.group76.movie.Movie;

public class MovieManager extends FileManager {
    private HashMap<String, Movie> movies;
    int maxIndex; // to track the highest movie index


    public MovieManager() {
        super("resources/movies.csv");
        movies = getMovies();
        this.maxIndex = getMaxIndex();
    }


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


    public HashMap<String, Movie> getMovieDatabase() {
        return movies;
    }


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


    public boolean addMovie(String title, String genre, int year, double rating) {
        String movieId = String.format("M%03d", maxIndex + 1);
        Movie movie = new Movie(movieId, title, genre, year, rating);
        boolean added = addMovie(movie);
        if (added) {
            maxIndex++;
        }
        return added;
    }


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


    public boolean save(){
        String header = "id,title,genre,year,rating";
        
        String[] rows = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = (Movie) movies.values().toArray()[i];
            rows[i] = movie.toCSV();
        }

        return super.save(header, rows);
    }


    @Override
    public void close() {
        super.close();
        movies.clear();
    }
}
