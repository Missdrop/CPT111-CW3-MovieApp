package cpt111.group76.user.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Represents a user's movie watch history.
 * Tracks movies with their watch dates using a HashMap for efficient lookups.
 */
public class History {
    private HashMap<String, String> history; // movieId -> date


    /**
     * Constructs an empty History.
     */
    public History() {
        this.history = new HashMap<>();
    }


    /**
     * Constructs a History with existing data.
     *
     * @param history the existing history data
     */
    public History(HashMap<String, String> history) {
        this.history = history;
    }


    /**
     * Constructs a History from CSV entries array.
     * Expected format: ["movieId@date", "movieId@date", ...]
     *
     * @param historyEntries array of history entries in CSV format
     */
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


    public HashMap<String, String> get() {
        return history;
    }



    /**
     * Gets the set of movie IDs in history.
     *
     * @return HashSet of movie IDs
     */
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
