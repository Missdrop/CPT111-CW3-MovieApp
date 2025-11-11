package cpt111.group76.user.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class History {
    private HashMap<String, String> history; // movieId -> date

    public History() {
        this.history = new HashMap<>();
    }

    public History(HashMap<String, String> history) {
        this.history = history;
    }

    public History(String[] historyEntries) {
        this.history = new HashMap<>();
        for (String entry : historyEntries) {
            if (entry != null && !entry.isEmpty()) {
                String[] parts = entry.split("@");
                if (parts.length >= 2) {
                    String movieId = parts[0];
                    String date = parts[1];
                    this.history.put(movieId, date);
                }
            }
        }
    }

    public HashMap<String, String> getHistoryMap() {
        return history;
    }

    public HashMap<String, String> get() {
        return history;
    }

    public HashSet<String> getMovies() {
        return new HashSet<>(history.keySet());
    }

    public void add(String movieId) {
        String date = LocalDate.now().toString();
        history.put(movieId, date);
    }

    public boolean remove(String movieId) {
        return history.remove(movieId) != null;
    }

    public int length() {
        return history.size();
    }

    public boolean contains(String movieId) {
        return history.containsKey(movieId);
    }

    public String toCSV() {
        ArrayList<String> entries = new ArrayList<>();
        for (String movieID : getMovies()) {
            entries.add(movieID + "@" + history.get(movieID));
        }
        return String.join(";", entries);
    }
}