package user;

import java.util.ArrayList;
import java.util.HashSet;

public class BasicUser extends User {
    public BasicUser(String username, String password, HashSet<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, false);
    }


    public BasicUser(String username, String password) {
        super(username, password, false);
    }
}
