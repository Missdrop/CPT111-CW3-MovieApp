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
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Movie(String id, String title, String genre, int year, double rating) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie ID cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }
        if (year < 1880 || year > java.time.Year.now().getValue() + 5) {
            throw new IllegalArgumentException("Invalid release year: " + year);
        }
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }
        this.id = id;
        this.title = title;
        this.genre = genre != null ? genre : "";
        this.year = year;
        this.rating = rating;
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
