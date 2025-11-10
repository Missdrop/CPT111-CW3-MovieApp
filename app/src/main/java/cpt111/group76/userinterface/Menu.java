package cpt111.group76.userinterface;

import javafx.application.Application;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;

import cpt111.group76.App;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;
import cpt111.group76.recommendation.Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu extends Application{
    private User user;
    private static MovieManager movieManager = new MovieManager();
    private HashMap<String, Movie> movieDatabase;


    public Menu(User user){
        this.user = user;
        this.movieDatabase = movieManager.getMovieDatabase();
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> {
            movieManager.save();
            movieManager.close();
            new UserManager().updateUser(user);
        });

        // Top bar with user info and buttons
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            movieManager.save();
            movieManager.close();
            new App().start(new Stage());
            primaryStage.close();
        });

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> {
            new ChangePassword(user).start(new Stage());
        });

        Text usernameText = new Text("User: " + user.getUsername() + " Type: " + (user.isPremium() ? "[Premium]" : "[Non-Premium]"));

        HBox topBar = new HBox();
        topBar.getChildren().addAll(usernameText, changePasswordButton, logoutButton);
        HBox.setHgrow(usernameText, Priority.ALWAYS);
        HBox.setMargin(usernameText, new Insets(10));
        HBox.setMargin(changePasswordButton, new Insets(10));
        HBox.setMargin(logoutButton, new Insets(10));

        // Main menu buttons
        Button browseMoviesButton = new Button("Browse Movies");
        Button viewWatchlistButton = new Button("View Watchlist");
        Button viewHistoryButton = new Button("View History");
        Button getRecommendationsButton = new Button("Get Recommendations");

        browseMoviesButton.setOnAction(e -> openBrowseMovies());
        viewWatchlistButton.setOnAction(e -> openViewWatchlist());
        viewHistoryButton.setOnAction(e -> openViewHistory());
        getRecommendationsButton.setOnAction(e -> openGetRecommendations());

        VBox menuButtons = new VBox(10,
            browseMoviesButton,
            viewWatchlistButton,
            viewHistoryButton,
            getRecommendationsButton
        );
        menuButtons.setPadding(new Insets(20));
        menuButtons.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(menuButtons);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movie Recommendation & Tracker - Main Menu");
        primaryStage.show();
    }


    private void openBrowseMovies() {
        Stage stage = new Stage();
        stage.setTitle("Browse Movies - Select movies to add to watchlist or mark as watched");

        ListView<String> movieList = new ListView<>();
        ArrayList<String> movieItems = new ArrayList<>();

        for (Movie movie : movieDatabase.values()) {
            movieItems.add(movie.toString());
        }

        movieList.setItems(FXCollections.observableArrayList(movieItems));

        // Action buttons
        Button addToWatchlistButton = new Button("Add Selected to Watchlist");
        Button markWatchedButton = new Button("Mark Selected as Watched");
        Button removeFromWatchlistButton = new Button("Remove Selected from Watchlist");
        Label statusLabel = new Label();

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

        HBox buttonBox = new HBox(10, addToWatchlistButton, markWatchedButton, removeFromWatchlistButton);
        VBox layout = new VBox(10, new Label("All Movies (select a movie to perform actions):"), 
                                movieList, buttonBox, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.show();
    }


    private void openViewWatchlist() {
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


    private void openViewHistory() {
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


    private void openGetRecommendations() {
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


    // Helper method to extract movie ID from the displayed string
    private String extractMovieId(String movieString) {
        if (movieString != null && movieString.length() >= 4) {
            return movieString.substring(0, 4); // Assuming format like "M001 - ..."
        }
        return null;
    }


    // Helper method to update watchlist view
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


    // Helper method to update history view
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