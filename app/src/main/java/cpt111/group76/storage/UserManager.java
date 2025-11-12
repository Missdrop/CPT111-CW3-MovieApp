package cpt111.group76.storage;

import java.util.HashMap;

import cpt111.group76.user.User;


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
     *
     * @return true if save was successful, false otherwise
     */
    public boolean save(){
        String header = "username,password,watchlist,history,premium";

        String[] rows = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.values().toArray()[i];
            rows[i] = user.toCSV();
        }

        return super.save(header, rows);
    }


    /**
     * Create a User object from user data array.
     */
    private User createUser(String[] userData) {
        try {
            return new User(userData);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Loads users from CSV file into a HashMap.
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

            User user = createUser(userData);
            if (user != null) {
                userMap.put(userData[0], user);
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
