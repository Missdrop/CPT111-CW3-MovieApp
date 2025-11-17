package cpt111.group76.recommendation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import cpt111.group76.movie.Movie;
import cpt111.group76.user.User;


/**
 * Recommendation engine that provides movie recommendations based on user preferences.
 * Supports multiple recommendation strategies: by rating, genre, and release year.
 */
public class Engine {
    private Map<String, Movie> movieDatabase;
    private User user;


    /**
     * Constructs a recommendation engine with movie database and user data.
     *
     * @param movieDatabase the database of all available movies
     * @param user the user for whom recommendations are generated
     */
    public Engine(Map<String, Movie> movieDatabase, User user) {
        this.movieDatabase = movieDatabase;
        this.user = user;
    }


    public List<String> recommendation(String type) {
        return recommendation(type, 5);
    }


    public List<String> recommendation(int numRecommendations) {
        return recommendation("rating", numRecommendations);
    }


    /**
     * Generates movie recommendations based on specified type and count.
     * Recommendations are ordered by rating within each strategy.
     *
     * @param type the recommendation strategy: "genre", "year", or "rating"
     * @param numRecommendations number of recommendations to generate
     * @return a list of recommended movie IDs, ordered by relevance
     */
    public List<String> recommendation(String type, int numRecommendations) {
        List<String> recommendation = new ArrayList<>();

        if (getLikedMovies() == null) {
            type = "rating";
        }

        switch(type) {
            case "genre":
                List<String> genreMovies = getFavouriteGenreMovies();
                recommendation = genreMovies.subList(0, Math.min(numRecommendations, genreMovies.size()));
                break;
            case "year":
                List<String> yearMovies = getFavouriteYearMovies();
                recommendation = yearMovies.subList(0, Math.min(numRecommendations, yearMovies.size()));
                break;
            case "rating":
            default:
                List<String> ratedMovies = getTopRatedMovies();
                recommendation = ratedMovies.subList(0, Math.min(numRecommendations, ratedMovies.size()));
                break;
        }
        return recommendation;
    }


    /**
     * Gets top rated movies from the database, sorted by rating descending.
     *
     * @return list of movie IDs sorted by rating
     */
    private List<String> getTopRatedMovies() {
        List<String> topRatedMovies = new ArrayList<>();
        movieDatabase.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue().getRating(), e1.getValue().getRating()))
                .forEachOrdered(e -> topRatedMovies.add(e.getKey()));
        return topRatedMovies;
    }


    /**
     * Gets movies sorted by proximity to user's favorite release year.
     * Favorite year is calculated as average year of watched movies.
     *
     * @return list of movie IDs sorted by year proximity then rating
     */
    private List<String> getFavouriteYearMovies() {
        List<String> favouriteYearMovies = new ArrayList<>();
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
     * Gets movies sorted by user's genre preferences.
     * Genre preference is calculated based on the count in watchlist and history.
     *
     * @return list of movie IDs sorted by genre preference then rating
     */
    private List<String> getFavouriteGenreMovies() {
        List<String> favouriteGenreMovies = new ArrayList<>();
        Map<String, Integer> favouriteGenres = getFavouriteGenreMap();
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


    /**
     * Calculates user's favorite release year based on watch history.
     *
     * @return the average release year of watched movies, or 0 if no history
     */
    private int getFavouriteYear() {
        int sum = 0;

        Set<String> likedMovies = getLikedMovies();
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
     * Calculates genre preferences based on user's watch history and watchlist.
     *
     * @return map of genres to their frequency in user's preferences
     */
    private Map<String, Integer> getFavouriteGenreMap() {
        Map<String, Integer> scoreMap = new HashMap<>();

        Set<String> likedMovies = getLikedMovies();
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
     * Gets all movies the user has interacted with (in watchlist or history).
     *
     * @return set of movie IDs that user has liked, or null if no interactions
     */
    private Set<String> getLikedMovies() {
        if (user.getHistory().length() + user.getWatchlist().length() == 0) {
            return null;
        }

        Set<String> likedMovies = new HashSet<>();
        for (String movieID : user.getHistory().getMovies()) {
            likedMovies.add(movieID);
        }
        for (String movieID : user.getWatchlist().get()) {
            likedMovies.add(movieID);
        }

        return likedMovies;
    }
}
