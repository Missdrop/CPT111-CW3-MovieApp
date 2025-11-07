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


    public Movie(String[] movieData) {
        this(movieData[0], movieData[1], movieData[2],
        movieData[3] != null && !movieData[3].isEmpty() ? Integer.parseInt(movieData[3]) : 0,
        movieData[4] != null && !movieData[4].isEmpty() ? Double.parseDouble(movieData[4]) : 0.0);
    }


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


    public String toCSV() {
        return String.join(",", new String[] {
            this.id,
            this.title,
            Integer.toString(this.year),
            this.genre,
            Double.toString(this.rating)
        });
    }
}
