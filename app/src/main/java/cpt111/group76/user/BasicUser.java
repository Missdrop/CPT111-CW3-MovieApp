package cpt111.group76.user;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

/**
 * Represents a premium user with enhanced privileges in the movie recommendation system.
 * Premium users have a larger watchlist capacity (100 movies) and access to advanced features
 * such as adding new movies to the database and enhanced recommendation algorithms.
 * Extends the User class with premium capabilities.
 */
public class BasicUser extends User {
    private static final int maxWatchlistSize = 20;


    public BasicUser(String username, String passwordHash, Watchlist watchlist, History history) {
        super(username, passwordHash, watchlist, history, false);
    }


    public BasicUser(User user) {
        super(user.getUsername(), user.getPasswordHash(), user.getWatchlist(), user.getHistory(), false);
    }


    public BasicUser(String username, String password) {
        super(username, password, false);
    }


    public int getMaxWatchlistSize() {
        return maxWatchlistSize;
    }


    /**
     * Adds a movie to the watchlist if the watchlist is not at capacity.
     * Premium users have a larger capacity (100 movies) compared to basic users.
     */
    @Override
    public boolean addToWatchlist(String movieId) {
        if (this.getWatchlist().length() >= maxWatchlistSize) {
            return false; // watchlist full
        }
        return super.addToWatchlist(movieId);
    }

}
