package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;
import cpt111.group76.recommendation.Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class Recommendation {
    private User user;
    private HashMap<String, Movie> movieDatabase;

    public Recommendation(User user, HashMap<String, Movie> movieDatabase) {
        this.user = user;
        this.movieDatabase = movieDatabase;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Get Recommendations");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label typeLabel = new Label("Recommendation Type:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("genre", "year", "rating");
        typeComboBox.setValue("genre");

        Label countLabel = new Label("Number of Recommendations:");
        TextField countField = new TextField();
        countField.setText("5");

        Button recommendButton = new Button("Get Recommendations");
        ListView<String> recommendationsView = new ListView<>();

        // Add to watchlist button for recommendations
        Button addToWatchlistButton = new Button("Add Selected to Watchlist");
        Label statusLabel = new Label();

        recommendButton.setOnAction(e -> {
            try {
                String type = typeComboBox.getValue();
                int count = Integer.parseInt(countField.getText());
                
                Engine engine = new Engine(movieDatabase, user);
                String[] recommendations = engine.recommendation(type, count);
                
                ArrayList<String> recommendationItems = new ArrayList<>();
                for (String movieId : recommendations) {
                    Movie movie = movieDatabase.get(movieId);
                    if (movie != null) {
                        recommendationItems.add(movie.toString());
                    }
                }
                
                recommendationsView.setItems(FXCollections.observableArrayList(recommendationItems));
                statusLabel.setText("Found " + recommendationItems.size() + " recommendations.");
                
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a valid number for recommendations count.");
                alert.showAndWait();
            }
        });

        addToWatchlistButton.setOnAction(e -> {
            String selected = recommendationsView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = extractMovieId(selected);
                if (movieId != null) {
                    if (user.getWatchlist().contains(movieId)) {
                        statusLabel.setText("Movie is already in your watchlist.");
                    } else {
                        boolean success = user.addToWatchlist(movieId);
                        if (success) {
                            statusLabel.setText("Successfully added to watchlist!");
                            new UserManager().updateUser(user);
                        } else {
                            statusLabel.setText("Failed to add to watchlist. Watchlist may be full.");
                        }
                    }
                }
            } else {
                statusLabel.setText("Please select a recommendation first.");
            }
        });

        HBox recommendationButtons = new HBox(10, recommendButton, addToWatchlistButton);

        layout.getChildren().addAll(
            typeLabel, typeComboBox,
            countLabel, countField,
            recommendationButtons,
            new Label("Recommendations:"),
            recommendationsView,
            statusLabel
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private String extractMovieId(String movieString) {
        if (movieString != null && movieString.length() >= 4) {
            return movieString.substring(0, 4);
        }
        return null;
    }
}