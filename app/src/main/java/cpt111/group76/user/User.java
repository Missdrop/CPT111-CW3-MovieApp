package cpt111.group76.user;

import java.util.List;

import cpt111.group76.user.data.*;
import cpt111.group76.exception.*;

/**
 * Represents a user in the movie recommendation system.
 * Base class for all user types with common functionality.
 */
public class User {
    private String username;
    private String passwordHash;
    private Watchlist watchlist;
    private History history;


    /**
     * Constructs a User with all specified fields.
     * Performs basic validation on critical fields.
     *
     * @param username the user's username
     * @param passwordHash the hashed password
     * @param watchlist the user's watchlist
     * @param history the user's viewing history
     * @throws IllegalArgumentException if username is null or empty
     */
    protected User(String username, String passwordHash, Watchlist watchlist, History history) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        this.username = username.trim();
        this.passwordHash = passwordHash != null ? passwordHash : "";
        this.watchlist = watchlist != null ? watchlist : new Watchlist();
        this.history = history != null ? history : new History();
    }


    /**
     * Constructs a new User with plain text password (will be hashed).
     *
     * @param username the user's username
     * @param password the plain text password
     * @throws PasswordValidationException if the password is invalid
     * @throws UsernameValidationException if the username is invalid
     */
    protected User(String username, String password) throws PasswordValidationException, UsernameValidationException {
        setUsername(username);
        setPassword(password);
        this.watchlist = new Watchlist();
        this.history = new History();
    }

    /**
     * Gets the maximum size of the watchlist for this user type.
     * @return
     */
    public int getMaxWatchlistSize() {
        throw new UnsupportedOperationException("Subclass must override getMaxWatchlistSize()");
    }


    /**
     * Indicates if the user can add movies to their watchlist.
     * @return true if the user can add movies, false otherwise
     */
    public boolean canAddMovies() {
        throw new UnsupportedOperationException("Subclass must override canAddMovies()");
    }


    /**
     * Gets the available recommendation types for this user type.
     * @return a list of recommendation type names
     */
    public List<String> getAvailableRecommendationTypes() {
        throw new UnsupportedOperationException("Subclass must override getAvailableRecommendationTypes()");
    }


    /**
     * Gets the user type as a string.
     * @return the user type
     */
    public String getUserType() {
        throw new UnsupportedOperationException("Subclass must override getUserType()");
    }


    /**
     * Sets the username for the user.
     * Username must be unique, between 3 and 20 characters,
     * and can only contain letters and digits.
     *
     * @param username the username to validate
     * @throws UsernameValidationException if the username is invalid
     */
    private void setUsername(String username) throws UsernameValidationException {
        if (username.length() < 3 || username.length() > 20) {
            throw new UsernameValidationException("Username must be between 3 and 20 characters long.");
        }

        // username can only contain letters and digits
        for (char c : username.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                throw new UsernameValidationException("Username can only contain letters and digits.");
            }
        }

        // assign validated username to the field
        this.username = username;
    }


    /**
     * Sets a new password for the user.
     * Password should be at least 6 characters long, 18 characters max,
     * contain at least one digit and one letter.
     *
     * @param newPassword the password to validate
     * @throws PasswordValidationException if the password is invalid
     */
    public void setPassword(String newPassword) throws PasswordValidationException {
        if (newPassword.length() < 6) {
            throw new PasswordValidationException("Password must be at least 6 characters long.");
        }
        if (newPassword.length() > 18) {
            throw new PasswordValidationException("Password must be at most 18 characters long.");
        }
        // password must contain at least one digit
        boolean hasDigit = false;
        boolean hasLetter = false;
        for (char c : newPassword.toCharArray()) {
            if (Character.isDigit(c)){
                hasDigit = true;
            }
            if (Character.isLetter(c)){
                hasLetter = true;
            }
        }
        if (!hasDigit) {
            throw new PasswordValidationException("Password must contain at least one digit.");
        }
        if (!hasLetter) {
            throw new PasswordValidationException("Password must contain at least one letter.");
        }
        this.passwordHash = toHash(newPassword);
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
                this.history.toCSV()});
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
}
