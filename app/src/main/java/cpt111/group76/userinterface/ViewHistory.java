package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewHistory {
    private User user;
    private HashMap<String, Movie> movieDatabase;

    public ViewHistory(User user, HashMap<String, Movie> movieDatabase) {
        this.user = user;
        this.movieDatabase = movieDatabase;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("My Watch History");

        ListView<String> historyView = new ListView<>();
        updateHistoryView(historyView);

        // Action button
        Button removeButton = new Button("Remove Selected from History");
        Label statusLabel = new Label();

        removeButton.setOnAction(e -> {
            String selected = historyView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Extract movie ID from history entry (format: "M001 - Movie Title @ 2023-10-01")
                String[] parts = selected.split(" @ ")[0].split(" - ");
                if (parts.length > 0) {
                    String movieId = parts[0];
                    boolean success = user.removeFromHistory(movieId);
                    if (success) {
                        statusLabel.setText("Successfully removed from history!");
                        new UserManager().updateUser(user);
                        updateHistoryView(historyView);
                    } else {
                        statusLabel.setText("Failed to remove from history.");
                    }
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        VBox layout = new VBox(10, new Label("My Watch History:"), historyView, removeButton, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void updateHistoryView(ListView<String> historyView) {
        ArrayList<String> historyItems = new ArrayList<>();
        for (String historyEntry : user.getHistory().get()) {
            String[] parts = historyEntry.split("@");
            String movieId = parts[0];
            String date = parts.length > 1 ? parts[1] : "Unknown date";
            
            Movie movie = movieDatabase.get(movieId);
            if (movie != null) {
                String movieInfo = String.format("%s - %s @ %s",
                    movie.getId(), movie.getTitle(), date);
                historyItems.add(movieInfo);
            }
        }

        if (historyItems.isEmpty()) {
            historyItems.add("Your watch history is empty.");
        }

        historyView.setItems(FXCollections.observableArrayList(historyItems));
    }
}