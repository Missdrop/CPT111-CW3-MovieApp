package user;

import java.util.ArrayList;
import java.util.HashSet;

public class PremiumUser extends User {
    public PremiumUser(String username, String password, HashSet<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, true);
    }


    public PremiumUser(String username, String password) {
        super(username, password, true);
    }
}
