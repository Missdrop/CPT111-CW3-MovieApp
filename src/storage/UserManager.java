package storage;

import java.util.Map;
import user.User;

public class UserManager extends FileManager {
    private Map<String, User> users;
    public UserManager() {
        super("resources/users.csv");
        this.users = getUsers();
    }


    /**
     * Create a User object from user data array.
     */
    private User createUser(String[] userData) {
        return new User(userData);
    }


    private Map<String, User> getUsers() {
        Map<String, User> userMap = new java.util.HashMap<>();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                this.nextLine(); // skip header
                continue;
            }

            String[] userData = this.nextLine();
            User user = createUser(userData);
            userMap.put(userData[0], user);
        }

        return userMap;
    }


    public User getUser(String username) {
        return this.users.get(username);
    }


    private boolean appendRow(User user) {
        String[] row = new String[] {
            user.getUsername(),
            user.getPassword(),
            String.join(";", user.getWatchlist()),
            String.join(";", user.getHistory())
        };
        return super.appendRow(row);
    }


    public boolean addUser(User user) {
        if (this.users.containsKey(user.getUsername())) {
            return false; // user already exists
        }
        boolean appendResult = this.appendRow(user);
        if (appendResult) {
            this.users.put(user.getUsername(), user);
        }
        return appendResult;
    }


    private boolean deleteRow(String username) {
        if (!this.users.containsKey(username)) {
            return false; // user does not exist
        }

        // Find the row index of the user to delete
        int rowIndex = 0;

        this.flushScanner();

        boolean firstLine = true;
        while (this.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                this.nextLine(); // skip header
                rowIndex++;
                continue;
            }

            String[] userData = this.nextLine();
            if (userData[0].equals(username)) {
                return super.deleteRow(rowIndex);
            }
            rowIndex++;
        }
        return false;
    }


    public boolean deleteUser(String username) {
        User user = this.users.get(username);
        if (user == null) {
            return false; // user does not exist
        }
        boolean deleteResult = this.deleteRow(username);
        if (deleteResult) {
            this.users.remove(username);
        }
        return deleteResult;
    }


    public boolean deleteUser(User user) {
        return deleteUser(user.getUsername());
    }


    @Override
    public void close() {
        super.close();
        this.users.clear();
    }

}
