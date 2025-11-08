package cpt111.group76.user;

import java.util.ArrayList;
import java.util.HashSet;

public class BasicUser extends User {
    private static final int maxWatchlistSize = 20;


    public BasicUser(String username, String password, HashSet<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, false);
    }


    public BasicUser(String username, String password) {
        super(username, password, false);
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
