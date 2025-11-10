package cpt111.group76.user;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

public class User {
    private String username;
    private String passwordHash;
    private Watchlist watchlist;
    private History history;
    private boolean isPremium;


    public User(String username, String passwordHash, Watchlist watchlist, History history, boolean isPremium) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.watchlist = watchlist;
        this.history = history;
        this.isPremium = isPremium;
    }


    public User(String username, String password, boolean isPremium) {
        this(username, toHash(password), new Watchlist(), new History(), isPremium);
    }


    public User(String[] userData) throws Exception {
        this(userData[0], userData[1],
        userData[2].length() > 0 ? new Watchlist(userData[2].split(";", -1)) : new Watchlist(),
        userData[3].length() > 0 ? new History(userData[3].split(";", -1)) : new History(),
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


    public String getPasswordHash() {
        return this.passwordHash;
    }


    public Watchlist getWatchlist() {
        return this.watchlist;
    }


    public History getHistory() {
        return this.history;
    }


    public boolean isPremium() {
        return this.isPremium;
    }

    //setters
    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }


    public void setPassword(String newPassword) {
        this.passwordHash = toHash(newPassword);
    }


    public boolean addToWatchlist(String movieId) {
        return this.watchlist.add(movieId);
    }


    public boolean removeFromWatchlist(String movieId) {
        return this.watchlist.remove(movieId);
    }


    public void addToHistory(String movieId) {
        // Ensure no duplicates in history
        removeFromHistory(movieId);
        history.add(movieId);

        // Remove from watchlist if present
        removeFromWatchlist(movieId);
    }


    public boolean removeFromHistory(String movieId) {
        return history.remove(movieId);
    }


    public boolean verifyPassword(String password) {
        return getPasswordHash().equals(toHash(password));
    }


    public String toCSV() {
        return String.join(",", new String[] {
            this.username,
            this.passwordHash,
            this.watchlist.toCSV(),
            this.history.toCSV(),
            String.valueOf(this.isPremium)
        });
    }
}
