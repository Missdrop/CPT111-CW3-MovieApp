package movie;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private int year;
    private double rating;


    public Movie(String id, String title, String genre, int year, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }
}
