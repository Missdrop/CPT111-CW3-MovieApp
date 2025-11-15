package cpt111.group76.user;

import java.util.List;

import cpt111.group76.user.data.*;
import cpt111.group76.exception.*;

/**
 * Represents a premium user with enhanced privileges in the movie recommendation system.
 * Premium users have a larger watchlist capacity (100 movies) and access to advanced features
 * such as adding new movies to the database and enhanced recommendation algorithms.
 * Extends the User class with premium capabilities.
 */
public class BasicUser extends User {
    private static final int MAX_WATCHLIST_SIZE = 20;
    private static final List<String> AVAILABLE_RECOMMENDATION_TYPES = List.of("rating");


    public BasicUser(String username, String passwordHash, Watchlist watchlist, History history) {
        super(username, passwordHash, watchlist, history);
    }


    public BasicUser(User user) {
        super(user.getUsername(), user.getPasswordHash(), user.getWatchlist(), user.getHistory());
    }


    public BasicUser(String username, String password) throws PasswordValidationException, UsernameValidationException {
        super(username, password);
    }


    @Override
    public int getMaxWatchlistSize() {
        return MAX_WATCHLIST_SIZE;
    }


    @Override
    public boolean canAddMovies() {
        return false;
    }


    @Override
    public List<String> getAvailableRecommendationTypes() {
        return AVAILABLE_RECOMMENDATION_TYPES;
    }


    @Override
    public String getUserType() {
        return "Basic";
    }


    /**
     * Adds a movie to the watchlist if the watchlist is not at capacity.
     * Premium users have a larger capacity (100 movies) compared to basic users.
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
        return super.toCSV() + ",Basic";
    }
}
