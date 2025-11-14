package cpt111.group76.storage;

import java.util.HashMap;

import cpt111.group76.user.User;
import cpt111.group76.user.data.History;
import cpt111.group76.user.data.Watchlist;


/**
 * Manages user database operations including authentication, registration, and user validation.
 * Extends FileManager to handle CSV file operations for user data.
 */
public class UserManager extends FileManager {
    private HashMap<String, User> users;


    /**
     * Constructs a UserManager and
     * loads users from CSV file, path: resources/users.csv
     */
    public UserManager() {
        super("resources/users.csv");
        users = getUsers();
    }


    /**
     * Authenticates a user with username and password.
     *
     * @param username the username to authenticate
     * @param password the password to verify
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.verifyPassword(password)) {
            return true;
        }
        return false;
    }


    /**
     * Validates a username against requirements and database.
     * Username must be unique, between 3 and 20 characters,
     * and can only contain letters and digits.
     *
     * @param username the username to validate
     * @return null if valid, otherwise an error message string
     */
    public String checkUsername(String username) {
        if (users.containsKey(username)) {
            return "Username already exists.";
        }
        if (username.length() < 3 || username.length() > 20) {
            return "Username must be between 3 and 20 characters long.";
        }
        //username can only contain letters, digits
        for (char c : username.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return "Username can only contain letters and digits.";
            }
        }
        return null; // valid username
    }


    /**
     * Validates a password against requirements and database.
     * Password should be at least 6 characters long, 18 characters max,
     * contain at least one digit and one letter.
     *
     * @param password the password to validate
     * @return null if valid, otherwise an error message string
     */
    public String checkPassword(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        if (password.length() > 18) {
            return "Password must be at most 18 characters long.";
        }
        // password must contain at least one digit
        boolean hasDigit = false;
        boolean hasLetter = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)){
                hasDigit = true;
            }
            if (Character.isLetter(c)){
                hasLetter = true;
            }
        }
        if (!hasDigit) {
            return "Password must contain at least one digit.";
        }
        if (!hasLetter) {
            return "Password must contain at least one letter.";
        }
        return null; // valid password
    }


    /**
     * Retrieves a user by username.
     *
     * @param username the username to look up
     * @return the User object, or null if not found
     */
    public User getUser(String username) {
        return users.get(username);
    }


    /**
     * Adds a new user with username and password.
     *
     * @param username the new user's username
     * @param password the new user's password
     * @return true if user was added successfully, false if username already exists
     */
    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // user already exists
        }
        users.put(username, new User(username, password, false));
        return true;
    }


    public boolean addUser(User user) {
        if (users.get(user.getUsername()) == null) {
            users.put(user.getUsername(), user);
            return true;
        }
        return false; // user already exists
    }


    public boolean deleteUser(String username) {
        if (users.get(username) == null) {
            return false; // user does not exist
        }
        users.remove(username);
        return true;
    }


    public boolean deleteUser(User user) {
        return deleteUser(user.getUsername());
    }


    /**
     * Updates an existing user in the database.
     *
     * @param user the user object with updated information
     */
    public void updateUser(User user) {
        deleteUser(user);
        addUser(user);
    }


    /**
     * Saves the current user database to CSV file.
     * @throws RuntimeException if an error occurs during saving
     */
    public void save() throws RuntimeException {
        String header = "username,password,watchlist,history,premium";

        String[] rows = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.values().toArray()[i];
            rows[i] = user.toCSV();
        }

        try {
            super.save(header, rows);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user data: " + e.getMessage());
        }
    }


    /**
     * Creates a User object from CSV data array.
     * Centralizes all CSV parsing logic in the manager class.
     *
     * @param userData the CSV data array in format: [username, passwordHash, watchlistCSV, historyCSV, isPremium]
     * @return the created User object
     * @throws IllegalArgumentException if the data is invalid or malformed
     */
    private static User createFromCSV(String[] userData) throws IllegalArgumentException {
        if (userData == null || userData.length < 5) {
            throw new IllegalArgumentException("User data array must contain 5 elements");
        }
        
        try {
            String username = userData[0] != null ? userData[0].trim() : "";
            String passwordHash = userData[1] != null ? userData[1].trim() : "";
            
            // super key validation
            if (username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty in CSV data");
            }
            
            // Parse watchlist from CSV format
            Watchlist watchlist = userData[2].length() > 0 ?
                new Watchlist(userData[2].split(";", -1)) : new Watchlist();
            
            // Parse history from CSV format
            History history = userData[3].length() > 0 ?
                new History(userData[3].split(";", -1)) : new History();
            
            // Parse premium status
            boolean isPremium = userData[4].length() > 0 ?
                Boolean.parseBoolean(userData[4]) : false;
            
            return new User(username, passwordHash, watchlist, history, isPremium);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to create user from CSV data: " + e.getMessage(), e);
        }
    }


    /**
     * Loads users from CSV file into a HashMap.
     * Handles invalid data gracefully by skipping problematic entries.
     *
     * @return HashMap containing all valid users with usernames as keys
     */
    private HashMap<String, User> getUsers() {
        HashMap<String, User> userMap = new HashMap<String, User>();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            // skip header
            if (firstLine) {
                firstLine = false;
                this.nextLine();
                continue;
            }

            // ensure userData has exactly 5 elements
            String[] userData = new String[5];
            String[] readData = this.nextLine();
            for (int i = 0; i < userData.length; i++) {
                userData[i] = i < readData.length ? readData[i] : "";
            }

            // try to create user from CSV data
            try {
                User user = createFromCSV(userData);
                if (user != null) {
                    userMap.put(user.getUsername(), user);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Skipping invalid user data: " + e.getMessage());
                if (userData != null && userData.length > 0) {
                    System.err.println("Data: " + String.join(",", userData));
                }
            }
        }

        return userMap;
    }


    @Override
    public void close() {
        super.close();
        users.clear();
    }
}
