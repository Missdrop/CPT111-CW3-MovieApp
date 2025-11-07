package storage;

import java.util.Map;
import user.User;

public class UserManager extends FileManager {
    private Map<String, User> users;
    public UserManager() {
        super("resources/users.csv");
        this.users = getUsers();
    }


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



    @Override
    public void close() {
        super.close();
        this.users.clear();
    }

}
