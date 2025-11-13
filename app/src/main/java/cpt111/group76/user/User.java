package cpt111.group76.user;

import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;

/**
 * Represents a user with basic information.
 * Handles user authentication, watchlist and history management.
 */
public class User {
    private String username;
    private String passwordHash;
    private Watchlist watchlist;
    private History history;
    private boolean isPremium;


    /**
     * Constructs a User with all specified fields.
     *
     * @param username the user's username
     * @param passwordHash the hashed password
     * @param watchlist the user's watchlist
     * @param history the user's viewing history
     * @param isPremium whether the user has a premium account
     */
    public User(String username, String passwordHash, Watchlist watchlist, History history, boolean isPremium) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.watchlist = watchlist;
        this.history = history;
        this.isPremium = isPremium;
    }


    
    /**
     * Constructs a new User with plain text password (will be hashed).
     *
     * @param username the user's username
     * @param password the plain text password
     * @param isPremium whether the user has premium status
     */
    public User(String username, String password, boolean isPremium) {
        this(username, toHash(password), new Watchlist(), new History(), isPremium);
    }


    /**
     * Constructs a User from CSV data array.
     * Expected format: [username, passwordHash, watchlistCSV, historyCSV, isPremium]
     *
     * @param userData the CSV data array containing user information
     * @throws Exception if the data cannot be parsed correctly
     */
    public User(String[] userData) throws NumberFormatException {
        this(userData[0], userData[1],
                userData[2].length() > 0 ? new Watchlist(userData[2].split(";", -1)) : new Watchlist(),
                userData[3].length() > 0 ? new History(userData[3].split(";", -1)) : new History(),
                userData[4].length() > 0 ? Boolean.parseBoolean(userData[4]) : false);
    }


    /**
     * Hashes a password using simple hexadecimal conversion of hashCode.
     *
     * @param password the plain text password to hash
     * @return the hashed password as hexadecimal string
     */
    private static String toHash(String password) {
        return Integer.toString(password.hashCode(), 16);
    }


    /**
     * Adds a movie to the watchlist.
     * @param movieId the movie ID to add
     * @return true if added successfully, false otherwise
     */
    public boolean addToWatchlist(String movieId) {
        return this.watchlist.add(movieId);
    }


    /**
     * Removes a movie from the watchlist.
     * @param movieId the movie ID to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeFromWatchlist(String movieId) {
        return this.watchlist.remove(movieId);
    }


    /**
     * Adds a movie to the user's history and removes it from watchlist if present.
     *
     * @param movieId the ID of the movie to add to history
     */
    public void addToHistory(String movieId) {
        history.add(movieId);

        // Remove from watchlist if present
        removeFromWatchlist(movieId);
    }


    /**
     * Removes a movie from the history.
     * @param movieId the movie ID to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeFromHistory(String movieId) {
        return history.remove(movieId);
    }


    /**
     * Verifies if the provided password matches the user's stored password.
     *
     * @param password the password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String password) {
        return getPasswordHash().equals(toHash(password));
    }


    /**
     * Converts the user to CSV format for storage.
     *
     * @return a CSV-formatted string representing the user, separated by commas
     */
    public String toCSV() {
        return String.join(",", new String[] { this.username, this.passwordHash, this.watchlist.toCSV(),
                this.history.toCSV(), String.valueOf(this.isPremium) });
    }


    // getters
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


    // setters
    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }


    public void setPassword(String newPassword) {
        this.passwordHash = toHash(newPassword);
    }
}
