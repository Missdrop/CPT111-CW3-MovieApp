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
            userMap.put(userData[0], user);
        }

        return userMap;
    }


    public User getUser(String username) {
        return this.users.get(username);
    }


    public boolean addUser(User user) {
        if (this.users.containsKey(user.getUsername())) {
            return false; // user already exists
        }
        this.users.put(user.getUsername(), user);
        return true;
    }


    public boolean deleteUser(String username) {
        if (this.users.get(username) == null) {
            return false; // user does not exist
        }
        this.users.remove(username);
        return true;
    }


    public boolean deleteUser(User user) {
        return deleteUser(user.getUsername());
    }


    public boolean save(){
        String header = "username,password,watchlist,history,premium";

        String[] rows = new String[this.users.size()];
        for (int i = 0; i < this.users.size(); i++) {
            User user = (User) this.users.values().toArray()[i];
            rows[i] = user.toCSV();
        }

        return super.save(header, rows);
    }


    @Override
    public void close() {
        super.close();
        this.users.clear();
    }

}
