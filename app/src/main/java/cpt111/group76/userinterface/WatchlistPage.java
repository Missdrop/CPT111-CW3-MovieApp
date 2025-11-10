package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;

import java.util.ArrayList;
import java.util.HashMap;

public class WatchlistPage {
    private User user;
    private HashMap<String, Movie> movieDatabase;

    public WatchlistPage(User user, HashMap<String, Movie> movieDatabase) {
        this.user = user;
        this.movieDatabase = movieDatabase;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("My Watchlist");

        ListView<String> watchlistView = new ListView<>();
        updateWatchlistView(watchlistView);

        // Action buttons
        Button removeButton = new Button("Remove Selected from Watchlist");
        Button markWatchedButton = new Button("Mark Selected as Watched");
        Label statusLabel = new Label();

        removeButton.setOnAction(e -> {
            String selected = watchlistView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = extractMovieId(selected);
                if (movieId != null) {
                    boolean success = user.removeFromWatchlist(movieId);
                    if (success) {
                        statusLabel.setText("Successfully removed from watchlist!");
                        new UserManager().updateUser(user);
                        updateWatchlistView(watchlistView);
                    } else {
                        statusLabel.setText("Failed to remove from watchlist.");
                    }
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        markWatchedButton.setOnAction(e -> {
            String selected = watchlistView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = extractMovieId(selected);
                if (movieId != null) {
                    user.addToHistory(movieId);
                    statusLabel.setText("Successfully marked as watched and removed from watchlist!");
                    new UserManager().updateUser(user);
                    updateWatchlistView(watchlistView);
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        HBox buttonBox = new HBox(10, removeButton, markWatchedButton);
        VBox layout = new VBox(10, new Label("My Watchlist:"), watchlistView, buttonBox, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void updateWatchlistView(ListView<String> watchlistView) {
        ArrayList<String> watchlistItems = new ArrayList<>();
        for (String movieId : user.getWatchlist().get()) {
            Movie movie = movieDatabase.get(movieId);
            if (movie != null) {
                watchlistItems.add(movie.toString());
            }
        }

        if (watchlistItems.isEmpty()) {
            watchlistItems.add("Your watchlist is empty.");
        }

        watchlistView.setItems(FXCollections.observableArrayList(watchlistItems));
    }

    private String extractMovieId(String movieString) {
        if (movieString != null && movieString.length() >= 4) {
            return movieString.substring(0, 4);
        }
        return null;
    }
}