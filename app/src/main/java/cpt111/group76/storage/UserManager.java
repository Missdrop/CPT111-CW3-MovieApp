package cpt111.group76.storage;

import java.util.HashMap;

import cpt111.group76.user.User;

public class UserManager extends FileManager {
    private static HashMap<String, User> users;


    public UserManager() {
        super("resources/users.csv");
        users = getUsers();
    }


    public static boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.verifyPassword(password)) {
            return true;
        }
        return false;
    }


    public static String checkUsername(String username) {
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


        public static String checkPassword(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        // password must contain at least one digit
        boolean hasDigit = false;
        boolean hasLetter = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)){
                hasDigit = true;
                break;
            }
            if (Character.isLetter(c)){
                hasLetter = true;
                break;
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
     * Create a User object from user data array.
     */
    private User createUser(String[] userData) {
        try {
            return new User(userData);
        } catch (Exception e) {
            return null;
        }
    }


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


    public User getUser(String username) {
        return users.get(username);
    }


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


    public void updateUser(User user) {
        deleteUser(user);
        addUser(user);
    }


    public boolean save(){
        String header = "username,password,watchlist,history,premium";

        String[] rows = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.values().toArray()[i];
            rows[i] = user.toCSV();
        }

        return super.save(header, rows);
    }


    @Override
    public void close() {
        super.close();
        users.clear();
    }
}
