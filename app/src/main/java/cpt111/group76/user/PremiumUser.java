package cpt111.group76.user;

import java.util.List;

import cpt111.group76.user.data.*;
import cpt111.group76.exception.*;

/**
 * Represents a basic user with limited privileges in the movie recommendation system.
 * Basic users have a watchlist capacity of 20 movies and access to basic features.
 * Extends the User class with restrictions on watchlist size.
 */
public class PremiumUser extends User {
    private static final int MAX_WATCHLIST_SIZE = 100;
    private static final List<String> AVAILABLE_RECOMMENDATION_TYPES = List.of("rating", "genre", "year");


    public PremiumUser(String username, String passwordHash, Watchlist watchlist, History history) {
        super(username, passwordHash, watchlist, history);
    }


    public PremiumUser(User user) {
        super(user.getUsername(), user.getPasswordHash(), user.getWatchlist(), user.getHistory());
    }


    public PremiumUser(String username, String password) throws PasswordValidationException, UsernameValidationException {
        super(username, password);
    }


    @Override
    public int getMaxWatchlistSize() {
        return MAX_WATCHLIST_SIZE;
    }


    @Override
    public boolean canAddMovies() {
        return true;
    }


    @Override
    public List<String> getAvailableRecommendationTypes() {
        return AVAILABLE_RECOMMENDATION_TYPES;
    }


    /**
     * Adds a movie to the watchlist if the watchlist is not at capacity.
     * Prevents adding movies when the watchlist has reached its maximum size.
     */
    @Override
    public boolean addToWatchlist(String movieId) {
        if (this.getWatchlist().length() >= MAX_WATCHLIST_SIZE) {
            return false; // watchlist full
        }
        return super.addToWatchlist(movieId);
    }


    @Override
    public String toCSV() {
        return super.toCSV() + ",true";
    }
}
