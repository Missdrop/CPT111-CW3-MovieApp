package cpt111.group76.user;

import java.util.ArrayList;
import java.util.HashSet;

public class PremiumUser extends User {
    private static final int maxWatchlistSize = 100;


    public PremiumUser(String username, String password, HashSet<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, true);
    }


    public PremiumUser(String username, String password) {
        super(username, password, true);
    }


    public int getMaxWatchlistSize() {
        return maxWatchlistSize;
    }


    @Override
    public boolean addToWatchlist(String movieId) {
        if (this.getWatchlist().size() >= maxWatchlistSize) {
            return false; // watchlist full
        }
        return super.addToWatchlist(movieId);
    }
}
