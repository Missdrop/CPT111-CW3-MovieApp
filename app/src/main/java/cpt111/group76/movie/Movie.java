package cpt111.group76.movie;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private int year;
    private double rating;、
    private String director;
    private String company;
    private int time;


    public Movie(String id, String title, String genre, int year, double rating, String director, String company, int time) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.director = director;
        this.company = company;
        this.time = time;
    }


    public Movie(String[] movieData) throws Exception {
        this(movieData[0], movieData[1], movieData[2],
        movieData[3] != null && !movieData[3].isEmpty() ? Integer.parseInt(movieData[3]) : 0,
        movieData[4] != null && !movieData[4].isEmpty() ? Double.parseDouble(movieData[4]) : 0.0);
        movieData[5],
        movieData[6],
        movieData[7] != null && !movieData[7].isEmpty() ? Integer.parseInt(movieData[7]) : 0);
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
    

    public String getDirector() {
        return this.director;
    }


    public String getCompany() {
        return this.company;
    }


    public int getTime() {
        return this.time;
    }


    public String toCSV() {
        return String.join(",", new String[] {
            this.id,
            this.title,
            this.genre,
            this.director,
            this.company,
            Integer.toString(this.year),
            Double.toString(this.rating)
            Integer.toString(this.time)
        });
    }
}
