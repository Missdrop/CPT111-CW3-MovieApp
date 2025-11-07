package user;

import java.util.List;
import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<String> watchlist;
    private ArrayList<String> history;


    public User(String username, String password, ArrayList<String> watchlist, ArrayList<String> history) {
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
        this.history = history;
    }


    public User(String[] userData) {
        this(userData[0], userData[1], new ArrayList<String>(List.of(userData[2].split(";"))), new ArrayList<String>(List.of(userData[3].split(";"))));
    }


    public String getUsername() {
        return this.username;
    }


    public String getPassword() {
        return this.password;
    }


    public List<String> getWatchlist() {
        return this.watchlist;
    }


    public List<String> getHistory() {
        return this.history;
    }


    public boolean appendWatchlist(String movieId) {
        return this.watchlist.add(movieId);
    }
}
