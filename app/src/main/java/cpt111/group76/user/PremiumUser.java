package cpt111.group76.user;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

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


    @Override
    public boolean addToWatchlist(String movieId) {
        if (this.getWatchlist().length() >= maxWatchlistSize) {
            return false; // watchlist full
        }
        return super.addToWatchlist(movieId);
    }
}
