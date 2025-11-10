package cpt111.group76.recommendation;

import java.util.HashMap;
import java.util.ArrayList;

import cpt111.group76.movie.Movie;
import cpt111.group76.user.User;

public class Engine {
    private HashMap<String, Movie> movieDatabase;
    private User user;


    public Engine(HashMap<String, Movie> movieDatabase, User user) {
        this.movieDatabase = movieDatabase;
        this.user = user;
    }


    public String[] recommendation(String type) {
        return recommendation(type, 5);
    }


    public String[] recommendation(int numRecommendations) {
        return recommendation("rating", numRecommendations);
    }


    /**
     * Recommend movies based on the specified type, then ordered by rating.
     * @param type Type of recommendation: "genre" or "year" or "rating"
     * @param numRecommendations Number of recommendations to return
     * @return A list of ordered recommended movie IDs
     */
    public String[] recommendation(String type, int numRecommendations) {
        String[] recommendation = new String[numRecommendations];

        if (getLikedMovies() == null) {
            type = "rating";
        }

        switch(type) {
            case "genre":
                ArrayList<String> genreMovies = getFavouriteGenreMovies();
                recommendation = genreMovies.subList(0, Math.min(numRecommendations, genreMovies.size()))
                    .toArray(new String[0]);
                break;
            case "year":
                ArrayList<String> yearMovies = getFavouriteYearMovies();
                recommendation = yearMovies.subList(0, Math.min(numRecommendations, yearMovies.size()))
                    .toArray(new String[0]);
                break;
            case "rating":
            default:
                ArrayList<String> ratedMovies = getTopRatedMovies();
                recommendation = ratedMovies.subList(0, Math.min(numRecommendations, ratedMovies.size()))
                    .toArray(new String[0]);
                break;
        }
        return recommendation;
    }


    private ArrayList<String> getTopRatedMovies() {
        ArrayList<String> topRatedMovies = new ArrayList<>();
        movieDatabase.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue().getRating(), e1.getValue().getRating()))
                .forEachOrdered(e -> topRatedMovies.add(e.getKey()));
        return topRatedMovies;
    }


    /**
     * Sorted firstly by abs(movie year - user favourite year)
     * then order by rating
     */
    private ArrayList<String> getFavouriteYearMovies() {
        ArrayList<String> favouriteYearMovies = new ArrayList<>();
        int favouriteYear = getFavouriteYear();
        movieDatabase.entrySet().stream()
                .sorted((e1, e2) -> {
                    int yearDiff1 = Math.abs(e1.getValue().getYear() - favouriteYear);
                    int yearDiff2 = Math.abs(e2.getValue().getYear() - favouriteYear);
                    if (yearDiff1 != yearDiff2) {
                        return Integer.compare(yearDiff1, yearDiff2);
                    } else {
                        return Double.compare(e2.getValue().getRating(), e1.getValue().getRating());
                    }
                })
                .forEachOrdered(e -> favouriteYearMovies.add(e.getKey()));
        return favouriteYearMovies;
    }


    /**
     * Sorted by user's favourite genre count
     * then order by rating
     */
    private ArrayList<String> getFavouriteGenreMovies() {
        ArrayList<String> favouriteGenreMovies = new ArrayList<>();
        HashMap<String, Integer> favouriteGenres = getFavouriteGenreMap();
        movieDatabase.entrySet().stream()
                .sorted((e1, e2) -> {
                    int genreScore1 = favouriteGenres.getOrDefault(e1.getValue().getGenre(), 0);
                    int genreScore2 = favouriteGenres.getOrDefault(e2.getValue().getGenre(), 0);
                    if (genreScore1 != genreScore2) {
                        return Integer.compare(genreScore2, genreScore1);
                    } else {
                        return Double.compare(e2.getValue().getRating(), e1.getValue().getRating());
                    }
                })
                .forEachOrdered(e -> favouriteGenreMovies.add(e.getKey()));
        return favouriteGenreMovies;
    }


    private int getFavouriteYear() {
        int sum = 0;

        ArrayList<String> likedMovies = getLikedMovies();
        if (likedMovies == null) {
            return 0;
        }

        for (String movieID : likedMovies) {
            int year = movieDatabase.get(movieID).getYear();
            sum += year;
        }
        return sum / likedMovies.size();
    }


    /**
     *  Returns a HashMap containing genres as keys and their corresponding counts as values.
     */
    private HashMap<String, Integer> getFavouriteGenreMap() {
        HashMap<String, Integer> scoreMap = new HashMap<>();

        ArrayList<String> likedMovies = getLikedMovies();
        if (likedMovies == null) {
            return scoreMap;
        }

        for (String movieID : likedMovies) {
            String genre = movieDatabase.get(movieID).getGenre();
            scoreMap.put(genre, scoreMap.getOrDefault(genre, 0) + 1);
        }

        return scoreMap;
    }


    /**
     * Returns a list of movie IDs that the user has liked (watched or in watchlist)
     */
    private ArrayList<String> getLikedMovies() {
        if (user.getHistory().length() + user.getWatchlist().length() == 0) {
            return null;
        }

        ArrayList<String> likedMovies = new ArrayList<>();
        for (String movieID : user.getHistory().getMovies()) {
            likedMovies.add(movieID);
        }
        for (String movieID : user.getWatchlist().get()) {
            likedMovies.add(movieID);
        }

        return likedMovies;
    }
}
