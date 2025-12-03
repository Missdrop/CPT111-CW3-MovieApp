package cpt111.group76.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpt111.group76.user.*;
import cpt111.group76.user.data.*;
import cpt111.group76.exception.*;


/**
 * Manages user database operations including authentication, registration, and user validation.
 * Extends FileManager to handle CSV file operations for user data.
 */
public class UserManager extends FileManager {
    private Map<String, User> users;


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
     * @throws PasswordValidationException if the password is invalid
     * @throws UsernameValidationException if the username is invalid
     */
    public void addUser(String username, String password) throws PasswordValidationException, UsernameValidationException {
        if (contains(username)) {
            throw new UsernameValidationException("Username already exists."); // user already exists
        }
        User newUser = new BasicUser(username, password);
        users.put(username, newUser);
    }


    public void addUser(User user) throws UsernameValidationException {
        if (contains(user.getUsername())) {
            throw new UsernameValidationException("User already exists."); // user already exists
        }
        users.put(user.getUsername(), user);
    }


    public boolean deleteUser(String username) {
        if (users.get(username) == null) {
            return false; // user does not exist
        }
        users.remove(username);
        return true;
    }


    private boolean contains(String username) {
        return users.containsKey(username);
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
        try {
            addUser(user);
        } catch (UsernameValidationException e) {
            // This should not happen as we are updating an existing user
        }
    }


    /**
     * Saves the current user database to CSV file.
     * @throws RuntimeException if an error occurs during saving
     */
    public void save() throws RuntimeException {
        String header = "username,password,watchlist,history,usertype";

        List<User> userList = getUserList();
        String[] rows = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            rows[i] = userList.get(i).toCSV();
        }

        try {
            super.save(header, rows);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user data: " + e.getMessage());
        }
    }


    private List<User> getUserList() {
        return new ArrayList<>(users.values());
    }


    /**
     * Creates a User object from CSV data array.
     * Centralizes all CSV parsing logic in the manager class.
     *
     * @param userData the CSV data array in format: [username, passwordHash, watchlistCSV, historyCSV, userType]
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
            String userType = userData[4] != null ? userData[4].trim() : "";

            switch (userType) {
                case "Premium":
                    return new PremiumUser(username, passwordHash, watchlist, history);
                case "Basic":
                    return new BasicUser(username, passwordHash, watchlist, history);
                default:
                    throw new IllegalArgumentException("Invalid user type in CSV data: " + userType);
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to create user from CSV data: " + e.getMessage(), e);
        }
    }


    /**
     * Loads users from CSV file into a HashMap.
     * Handles invalid data gracefully by skipping problematic entries.
     *
     * @return Map containing all valid users with usernames as keys
     */
    private Map<String, User> getUsers() {
        Map<String, User> userMap = new HashMap<String, User>();

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
                System.out.println("Warning: Skipping invalid user data: " + e.getMessage());
                if (userData != null && userData.length > 0) {
                    System.out.println("Data: " + String.join(",", userData));
                }
            }
        }

        return userMap;
    }


    @Override
    public void close() {
        super.close();
        users = null;
    }
}
