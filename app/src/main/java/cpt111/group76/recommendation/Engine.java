package cpt111.group76.recommendation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cpt111.group76.movie.Movie;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.user.User;


/**
 * Recommendation engine that provides movie recommendations based on user preferences.
 * Supports multiple recommendation strategies: by rating, genre, and release year.
 */
public class Engine {
    private List<Movie> tempMovieList;
    private Set<String> likedMovies;
    private MovieManager movieManager;


    /**
     * Constructs a recommendation engine with movie database and user data.
     *
     * @param movieManager
     * @param user the user for whom recommendations are generated
     */
    public Engine(MovieManager movieManager, User user) {
        this.movieManager = movieManager;
        // array list is more convenient for sorting
        this.tempMovieList = movieManager.getMovieList();
        this.likedMovies = getLikedMovies(user);
    }


    public List<Movie> recommendation(String type) {
        return recommendation(type, 5);
    }


    public List<Movie> recommendation(int numRecommendations) {
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
    public List<Movie> recommendation(String type, int numRecommendations) {
        if (likedMovies == null) {
            type = "rating";
        }

        switch(type) {
            case "genre":
                getFavouriteGenreMovies();
                return tempMovieList.subList(0, Math.min(numRecommendations, tempMovieList.size()));
            case "year":
                getFavouriteYearMovies();
                return tempMovieList.subList(0, Math.min(numRecommendations, tempMovieList.size()));
            case "rating":
            default:
                getTopRatedMovies();
                return tempMovieList.subList(0, Math.min(numRecommendations, tempMovieList.size()));
        }
    }


    /**
     * Gets top rated movies from the database, sorted by rating descending.
     */
    private void getTopRatedMovies() {
        Sort.sort(tempMovieList, new Sort.Comparator() {
            @Override
            public int compare(Movie a, Movie b) {
                // higher rating should come first
                return Double.compare(b.getRating(), a.getRating());
            }
        });
    }


    /**
     * Gets movies sorted by proximity to user's favorite release year.
     * Favorite year is calculated as average year of watched movies.
     */
    private void getFavouriteYearMovies() {
        int favouriteYear = getFavouriteYear();
        final int fav = favouriteYear;
        Sort.sort(tempMovieList, new Sort.Comparator() {
            @Override
            public int compare(Movie a, Movie b) {
                int diffA = Math.abs(a.getYear() - fav);
                int diffB = Math.abs(b.getYear() - fav);
                if (diffA != diffB) {
                    return Integer.compare(diffA, diffB);
                } else {
                    // higher rating first
                    return Double.compare(b.getRating(), a.getRating());
                }
            }
        });
    }


    /**
     * Gets movies sorted by user's genre preferences.
     * Genre preference is calculated based on the count in watchlist and history.
     */
    private void getFavouriteGenreMovies() {
        Map<String, Integer> favouriteGenres = getFavouriteGenreMap();
        final Map<String, Integer> favMap = favouriteGenres;
        Sort.sort(tempMovieList, new Sort.Comparator() {
            @Override
            public int compare(Movie a, Movie b) {
                int scoreA = favMap.getOrDefault(a.getGenre(), 0);
                int scoreB = favMap.getOrDefault(b.getGenre(), 0);
                if (scoreA != scoreB) {
                    // higher genre score first
                    return Integer.compare(scoreB, scoreA);
                } else {
                    // higher rating first
                    return Double.compare(b.getRating(), a.getRating());
                }
            }
        });
    }


    /**
     * Calculates user's favorite release year based on watch history.
     *
     * @return the average release year of watched movies, or 0 if no history
     */
    private int getFavouriteYear() {
        int sum = 0;

        if (likedMovies == null) {
            return 0;
        }

        for (String movieID : likedMovies) {
            int year = movieManager.getMovie(movieID).getYear();
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

        if (likedMovies == null) {
            return scoreMap;
        }

        for (String movieID : likedMovies) {
            String genre = movieManager.getMovie(movieID).getGenre();
            scoreMap.put(genre, scoreMap.getOrDefault(genre, 0) + 1);

        }

        return scoreMap;
    }


    /**
     * Gets all movies the user has interacted with (in watchlist or history).
     *
     * @return set of movie IDs that user has liked, or null if no interactions
     */
    private static Set<String> getLikedMovies(User user) {
        Set<String> likedMovies = user.getWatchlist().getMovieIdSet();
        likedMovies.retainAll(user.getHistory().getMovies());

        return likedMovies;
    }
}
