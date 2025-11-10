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
import cpt111.group76.storage.MovieManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;

import java.util.ArrayList;
import java.util.HashMap;

public class BrowseMovies {
    private User user;
    private HashMap<String, Movie> movieDatabase;
    private MovieManager movieManager;

    public BrowseMovies(User user, HashMap<String, Movie> movieDatabase, MovieManager movieManager) {
        this.user = user;
        this.movieDatabase = movieDatabase;
        this.movieManager = movieManager;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Browse Movies - Select movies to add to watchlist or mark as watched");

        // Add title
        Label titleLabel = new Label("Browse All Movies");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ListView<String> movieList = new ListView<>();
        updateMovieList(movieList);

        // Action buttons with styling
        Button addToWatchlistButton = new Button("Add to Watchlist");
        addToWatchlistButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        
        Button markWatchedButton = new Button("Mark as Watched");
        markWatchedButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        
        Button removeFromWatchlistButton = new Button("Remove from Watchlist");
        removeFromWatchlistButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        
        // Add New Movie button - only for Premium Users
        Button addNewMovieButton = null;
        if (user.isPremium()) {
            addNewMovieButton = new Button("Add New Movie");
            addNewMovieButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
            addNewMovieButton.setOnAction(e -> {
                new AddNewMovie(movieManager).show(stage);
                // Refresh the movie list when the add movie window is closed
                stage.setOnHidden(ev -> updateMovieList(movieList));
            });
        }
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        addToWatchlistButton.setOnAction(e -> {
            String selected = movieList.getSelectionModel().getSelectedItem();
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
                statusLabel.setText("Please select a movie first.");
            }
        });

        markWatchedButton.setOnAction(e -> {
            String selected = movieList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = extractMovieId(selected);
                if (movieId != null) {
                    if (user.getHistory().contains(movieId)) {
                        statusLabel.setText("Movie is already in your watch history.");
                    } else {
                        user.addToHistory(movieId);
                        statusLabel.setText("Successfully marked as watched!");
                        new UserManager().updateUser(user);
                    }
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        removeFromWatchlistButton.setOnAction(e -> {
            String selected = movieList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = extractMovieId(selected);
                if (movieId != null) {
                    if (!user.getWatchlist().contains(movieId)) {
                        statusLabel.setText("Movie is not in your watchlist.");
                    } else {
                        boolean success = user.removeFromWatchlist(movieId);
                        if (success) {
                            statusLabel.setText("Successfully removed from watchlist!");
                            new UserManager().updateUser(user);
                        } else {
                            statusLabel.setText("Failed to remove from watchlist.");
                        }
                    }
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        // Create button box - include Add New Movie button if user is premium
        HBox buttonBox;
        if (user.isPremium()) {
            buttonBox = new HBox(10, addToWatchlistButton, markWatchedButton, removeFromWatchlistButton, addNewMovieButton);
        } else {
            buttonBox = new HBox(10, addToWatchlistButton, markWatchedButton, removeFromWatchlistButton);
        }

        VBox layout = new VBox(10, titleLabel,
                            new Label("Select a movie to perform actions:"),
                            movieList, buttonBox, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 750, 500); // Slightly wider to accommodate the new button
        stage.setScene(scene);
        stage.show();
    }

    private void updateMovieList(ListView<String> movieList) {
        ArrayList<String> movieItems = new ArrayList<>();
        for (Movie movie : movieDatabase.values()) {
            movieItems.add(movie.toString());
        }
        movieList.setItems(FXCollections.observableArrayList(movieItems));
    }

    private String extractMovieId(String movieString) {
        if (movieString != null && movieString.length() >= 4) {
            return movieString.substring(0, 4);
        }
        return null;
    }
}