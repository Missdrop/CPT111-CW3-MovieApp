package cpt111.group76.movie;

/**
 * Represents a movie with basic information including ID, title, genre, release year and rating.
 * This class provides methods to convert movie data to CSV format for storage.
 */
public class Movie {
    private String id;
    private String title;
    private String genre;
    private int year;
    private double rating;


    /**
     * Constructs a Movie with specified details.
     *
     * @param id the unique identifier for the movie
     * @param title the title of the movie
     * @param genre the genre of the movie
     * @param year the release year of the movie
     * @param rating the rating of the movie (0.0-10.0)
     */
    public Movie(String id, String title, String genre, int year, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }


    /**
     * Constructs a Movie from CSV data array.
     * Expected format: [id, title, genre, year, rating]
     *
     * @param movieData the CSV data array containing movie information
     * @throws Exception if the data cannot be parsed correctly
     */
    public Movie(String[] movieData) throws Exception {
        this(movieData[0], movieData[1], movieData[2],
        movieData[3] != null && !movieData[3].isEmpty() ? Integer.parseInt(movieData[3]) : 0,
        movieData[4] != null && !movieData[4].isEmpty() ? Double.parseDouble(movieData[4]) : 0.0);
    }


    /**
     * Converts the movie to CSV format for storage.
     *
     * @return a CSV-formatted string representing the movie, separated by commas
     */
    public String toCSV() {
        return String.join(",", new String[] {
            this.id,
            this.title,
            this.genre,
            Integer.toString(this.year),
            Double.toString(this.rating)
        });
    }


    // Getters
    public String getId() {
        return this.id;
    }


    public String getTitle() {
        return this.title;
    }


    public String getGenre() {
        return this.genre;
    }


    public int getYear() {
        return this.year;
    }


    public double getRating() {
        return this.rating;
    }
}
