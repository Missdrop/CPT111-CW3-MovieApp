package cpt111.group76.user.data;

import java.util.HashSet;

public class Watchlist {
    private HashSet<String> watchlist;


    public Watchlist() {
        this.watchlist = new HashSet<>();
    }


    public Watchlist(HashSet<String> watchlist) {
        this.watchlist = watchlist;
    }


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
