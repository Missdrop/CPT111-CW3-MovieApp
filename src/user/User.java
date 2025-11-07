package user;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class User {
    private String username;
    private String password;
    private HashSet<String> watchlist;
    private ArrayList<String> history;


    public User(String username, String password, HashSet<String> watchlist, ArrayList<String> history) {
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
        this.history = history;
    }


    public User(String[] userData) {
        this(userData[0], userData[1],
        userData[2].length() > 0 ? new HashSet<String>(Set.of(userData[2].split(";"))) : new HashSet<String>(),
        userData[3].length() > 0 ? new ArrayList<String>(List.of(userData[3].split(";"))) : new ArrayList<String>()
        );
    }


    public String getUsername() {
        return this.username;
    }


    public String getPassword() {
        return this.password;
    }


    public HashSet<String> getWatchlist() {
        return this.watchlist;
    }


    public ArrayList<String> getHistory() {
        return this.history;
    }


    public boolean appendWatchlist(String movieId) {
        return this.watchlist.add(movieId);
    }


    public String toCSV() {
        return String.join(",", new String[] {
            this.username,
            this.password,
            String.join(";", this.watchlist),
            String.join(";", this.history)
        });
    }
}
