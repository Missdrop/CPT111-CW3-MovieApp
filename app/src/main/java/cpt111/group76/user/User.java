package cpt111.group76.user;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class User {
    private String username;
    private String passwordHash;
    private HashSet<String> watchlist;
    private ArrayList<String> history;
    private boolean isPremium;


    public User(String username, String passwordHash, HashSet<String> watchlist, ArrayList<String> history, boolean isPremium) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.watchlist = watchlist;
        this.history = history;
        this.isPremium = isPremium;
    }


    public User(String username, String password, boolean isPremium) {
        this(username, toHash(password), new HashSet<String>(), new ArrayList<String>(), isPremium);
    }


    public User(String[] userData) throws Exception {
        this(userData[0], userData[1],
        userData[2].length() > 0 ? new HashSet<String>(Set.of(userData[2].split(";"))) : new HashSet<String>(),
        userData[3].length() > 0 ? new ArrayList<String>(List.of(userData[3].split(";"))) : new ArrayList<String>(),
        userData[4].length() > 0 ? Boolean.parseBoolean(userData[4]) : false
        );
    }


    private static String toHash(String password) {
        return Integer.toString(password.hashCode(), 16);
    }


    //getters
    public String getUsername() {
        return this.username;
    }


    private String getPasswordHash() {
        return this.passwordHash;
    }


    public HashSet<String> getWatchlist() {
        return this.watchlist;
    }


    public ArrayList<String> getHistory() {
        return this.history;
    }


    public boolean isPremium() {
        return this.isPremium;
    }

    //setters
    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }


    public boolean addToWatchlist(String movieId) {
        return this.watchlist.add(movieId);
    }


    public boolean removeFromWatchlist(String movieId) {
        return this.watchlist.remove(movieId);
    }


    public void addToHistory(String movieId) {
        String date = java.time.LocalDate.now().toString();
        String historyEntry = movieId + "@" + date;
        
        // Remove if already exists to update date
        removeFromHistory(movieId);
        history.add(historyEntry);
        
        // Remove from watchlist if present
        removeFromWatchlist(movieId);
    }


    public boolean removeFromHistory(String movieId) {
        return this.history.removeIf(entry -> entry.startsWith(movieId + "@"));
    }


    public boolean verifyPassword(String password) {
        return getPasswordHash().equals(toHash(password));
    }


    public String toCSV() {
        return String.join(",", new String[] {
            this.username,
            this.passwordHash,
            String.join(";", this.watchlist),
            String.join(";", this.history),
            String.valueOf(this.isPremium)
        });
    }
}
