package cpt111.group76.user.data;

import java.time.LocalDate;

import java.util.ArrayList;

public class History {
    private ArrayList<String> history;


    public History() {
        this.history = new ArrayList<>();
    }


    public History(ArrayList<String> history) {
        this.history = history;
    }


    public History(String[] historyEntries) {
        this.history = new ArrayList<>();
        for (String entry : historyEntries) {
            this.history.add(entry);
        }
    }


    public ArrayList<String> get() {
        return history;
    }


    public void add(String movieId) {
        String date = LocalDate.now().toString();
        String historyEntry = movieId + "@" + date;
        history.add(historyEntry);
    }


    public boolean remove(String movieId) {
        return history.removeIf(entry -> entry.startsWith(movieId + "@"));
    }


    public int length() {
        return history.size();
    }


    public boolean contains(String movieId) {
        for (String entry : history) {
            if (entry.startsWith(movieId + "@")) {
                return true;
            }
        }
        return false;
    }
}
