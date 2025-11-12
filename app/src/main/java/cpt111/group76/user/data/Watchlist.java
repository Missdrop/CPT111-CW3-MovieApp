package cpt111.group76.user.data;

import java.util.HashSet;

/**
 * Represents a user's movie watchlist.
 * Uses HashSet to prevent duplicate entries and provide efficient operations.
 */
public class Watchlist {
    private HashSet<String> watchlist;


    /**
     * Constructs an empty Watchlist.
     */
    public Watchlist() {
        this.watchlist = new HashSet<>();
    }


    /**
     * Constructs a Watchlist with existing data.
     *
     * @param watchlist the existing watchlist data
     */
    public Watchlist(HashSet<String> watchlist) {
        this.watchlist = watchlist;
    }


    /**
     * Constructs a Watchlist from CSV entries array.
     *
     * @param watchlistEntries array of movie IDs
     */
    public Watchlist(String[] watchlistEntries) {
        this.watchlist = new HashSet<>();
        for (String entry : watchlistEntries) {
            this.watchlist.add(entry);
        }
    }


    public HashSet<String> get() {
        return watchlist;
    }


    public boolean add(String movieId) {
        return watchlist.add(movieId);
    }


    public boolean remove(String movieId) {
        return watchlist.remove(movieId);
    }


    public int length() {
        return watchlist.size();
    }


    public boolean contains(String movieId) {
        return watchlist.contains(movieId);
    }


    public String toCSV() {
        return String.join(";", watchlist);
    }
}
