package cpt111.group76.user;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

/**
 * Represents a basic user with limited privileges in the movie recommendation system.
 * Basic users have a watchlist capacity of 20 movies and access to basic features.
 * Extends the User class with restrictions on watchlist size.
 */
public class PremiumUser extends User {
    private static final int maxWatchlistSize = 100;


    public PremiumUser(String username, String passwordHash, Watchlist watchlist, History history) {
        super(username, passwordHash, watchlist, history, true);
    }


    public PremiumUser(User user) {
        super(user.getUsername(), user.getPasswordHash(), user.getWatchlist(), user.getHistory(), true);
    }


    public PremiumUser(String username, String password) {
        super(username, password, true);
    }


    public int getMaxWatchlistSize() {
        return maxWatchlistSize;
    }


    /**
     * Adds a movie to the watchlist if the watchlist is not at capacity.
     * Prevents adding movies when the watchlist has reached its maximum size.
     */
    @Override
    public boolean addToWatchlist(String movieId) {
        if (this.getWatchlist().length() >= maxWatchlistSize) {
            return false; // watchlist full
        }
        return super.addToWatchlist(movieId);
    }
}
