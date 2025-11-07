package user;

import java.util.List;

public class User {
    //username,password,watchlist,history
    private String username;
    private String password;
    private List<String> watchlist;
    private List<String> history;


    public User(String username, String password, List<String> watchlist, List<String> history) {
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
        this.history = history;
    }
}
