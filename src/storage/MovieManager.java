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
        Map<String, Movie> movieMap = new java.util.HashMap<String, Movie>();

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


    public boolean addMovie(Movie movie) {
        if (this.movies.containsKey(movie.getId())) {
            return false; // movie already exists
        }
        this.movies.put(movie.getId(), movie);
        return true;
    }


    public boolean deleteMovie(String movieId) {
        if (this.movies.get(movieId) == null) {
            return false; // movie does not exist
        }

        this.movies.remove(movieId);
        return true;
    }


    public boolean deleteMovie(Movie movie) {
        return deleteMovie(movie.getId());
    }


    public boolean save(){
        String header = "id,title,year,genre,rating";
        
        String[] rows = new String[this.movies.size()];
        for (int i = 0; i < this.movies.size(); i++) {
            Movie movie = (Movie) this.movies.values().toArray()[i];
            rows[i] = movie.toCSV();
        }

        return super.save(header, rows);
    }


    @Override
    public void close() {
        super.close();
        this.movies.clear();
    }
}
