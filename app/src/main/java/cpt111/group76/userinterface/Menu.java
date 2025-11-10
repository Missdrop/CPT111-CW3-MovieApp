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
        // when the window is closed, save and close movieManager
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
        Button addToWatchlistButton = new Button("Add to Watchlist");
        Button removeFromWatchlistButton = new Button("Remove from Watchlist");
        Button viewWatchlistButton = new Button("View Watchlist");
        Button markWatchedButton = new Button("Mark as Watched");
        Button viewHistoryButton = new Button("View History");
        Button getRecommendationsButton = new Button("Get Recommendations");

        // Set button actions
        browseMoviesButton.setOnAction(e -> openBrowseMovies());
        addToWatchlistButton.setOnAction(e -> openAddToWatchlist());
        removeFromWatchlistButton.setOnAction(e -> openRemoveFromWatchlist());
        viewWatchlistButton.setOnAction(e -> openViewWatchlist());
        markWatchedButton.setOnAction(e -> openMarkWatched());
        viewHistoryButton.setOnAction(e -> openViewHistory());
        getRecommendationsButton.setOnAction(e -> openGetRecommendations());

        // Layout for main menu
        VBox menuButtons = new VBox(10,
            browseMoviesButton,
            addToWatchlistButton,
            removeFromWatchlistButton,
            viewWatchlistButton,
            markWatchedButton,
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
        stage.setTitle("Browse Movies");

        ListView<String> movieList = new ListView<>();
        ArrayList<String> movieItems = new ArrayList<>();

        for (Movie movie : movieDatabase.values()) {
            String movieInfo = String.format("%s - %s (%d) | Genre: %s | Rating: %.1f", 
                movie.getId(), movie.getTitle(), movie.getYear(), movie.getGenre(), movie.getRating());
            movieItems.add(movieInfo);
        }

        movieList.setItems(FXCollections.observableArrayList(movieItems));

        VBox layout = new VBox(10, new Label("All Movies:"), movieList);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void openAddToWatchlist() {
        Stage stage = new Stage();
        stage.setTitle("Add Movie to Watchlist");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label instruction = new Label("Enter Movie ID to add to watchlist:");
        TextField movieIdField = new TextField();
        movieIdField.setPromptText("e.g., M001");

        Button addButton = new Button("Add to Watchlist");
        Label resultLabel = new Label();

        addButton.setOnAction(e -> {
            String movieId = movieIdField.getText().trim();
            if (movieId.isEmpty()) {
                resultLabel.setText("Please enter a movie ID.");
                return;
            }

            if (!movieDatabase.containsKey(movieId)) {
                resultLabel.setText("Movie ID not found.");
                return;
            }

            if (user.getWatchlist().contains(movieId)) {
                resultLabel.setText("Movie is already in your watchlist.");
                return;
            }

            boolean success = user.addToWatchlist(movieId);
            if (success) {
                resultLabel.setText("Successfully added to watchlist!");
                new UserManager().updateUser(user);
            } else {
                resultLabel.setText("Failed to add to watchlist. Watchlist may be full.");
            }
        });

        layout.getChildren().addAll(instruction, movieIdField, addButton, resultLabel);
        Scene scene = new Scene(layout, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void openRemoveFromWatchlist() {
        Stage stage = new Stage();
        stage.setTitle("Remove Movie from Watchlist");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label instruction = new Label("Enter Movie ID to remove from watchlist:");
        TextField movieIdField = new TextField();
        movieIdField.setPromptText("e.g., M001");

        Button removeButton = new Button("Remove from Watchlist");
        Label resultLabel = new Label();

        removeButton.setOnAction(e -> {
            String movieId = movieIdField.getText().trim();
            if (movieId.isEmpty()) {
                resultLabel.setText("Please enter a movie ID.");
                return;
            }

            if (!user.getWatchlist().contains(movieId)) {
                resultLabel.setText("Movie is not in your watchlist.");
                return;
            }

            boolean success = user.removeFromWatchlist(movieId);
            if (success) {
                resultLabel.setText("Successfully removed from watchlist!");
                new UserManager().updateUser(user);
            } else {
                resultLabel.setText("Failed to remove from watchlist.");
            }
        });

        layout.getChildren().addAll(instruction, movieIdField, removeButton, resultLabel);
        Scene scene = new Scene(layout, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void openViewWatchlist() {
        Stage stage = new Stage();
        stage.setTitle("My Watchlist");

        ListView<String> watchlistView = new ListView<>();
        ArrayList<String> watchlistItems = new ArrayList<>();

        for (String movieId : user.getWatchlist().get()) {
            Movie movie = movieDatabase.get(movieId);
            if (movie != null) {
                String movieInfo = String.format("%s - %s (%d) | Genre: %s | Rating: %.1f", 
                    movie.getId(), movie.getTitle(), movie.getYear(), movie.getGenre(), movie.getRating());
                watchlistItems.add(movieInfo);
            }
        }

        if (watchlistItems.isEmpty()) {
            watchlistItems.add("Your watchlist is empty.");
        }

        watchlistView.setItems(FXCollections.observableArrayList(watchlistItems));

        VBox layout = new VBox(10, new Label("My Watchlist:"), watchlistView);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void openMarkWatched() {
        Stage stage = new Stage();
        stage.setTitle("Mark Movie as Watched");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label instruction = new Label("Enter Movie ID to mark as watched:");
        TextField movieIdField = new TextField();
        movieIdField.setPromptText("e.g., M001");

        Button markButton = new Button("Mark as Watched");
        Label resultLabel = new Label();

        markButton.setOnAction(e -> {
            String movieId = movieIdField.getText().trim();
            if (movieId.isEmpty()) {
                resultLabel.setText("Please enter a movie ID.");
                return;
            }

            if (!movieDatabase.containsKey(movieId)) {
                resultLabel.setText("Movie ID not found.");
                return;
            }

            if (user.getHistory().contains(movieId)) {
                resultLabel.setText("Movie is already in your watch history.");
                return;
            }

            user.addToHistory(movieId);
            resultLabel.setText("Successfully marked as watched!");
            new UserManager().updateUser(user);
        });

        layout.getChildren().addAll(instruction, movieIdField, markButton, resultLabel);
        Scene scene = new Scene(layout, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void openViewHistory() {
        Stage stage = new Stage();
        stage.setTitle("My Watch History");

        ListView<String> historyView = new ListView<>();
        ArrayList<String> historyItems = new ArrayList<>();

        for (String historyEntry : user.getHistory().get()) {
            String[] parts = historyEntry.split("@");
            String movieId = parts[0];
            String date = parts.length > 1 ? parts[1] : "Unknown date";
            
            Movie movie = movieDatabase.get(movieId);
            if (movie != null) {
                String movieInfo = String.format("%s - %s | Watched on: %s", 
                    movie.getId(), movie.getTitle(), date);
                historyItems.add(movieInfo);
            }
        }

        if (historyItems.isEmpty()) {
            historyItems.add("Your watch history is empty.");
        }

        historyView.setItems(FXCollections.observableArrayList(historyItems));

        VBox layout = new VBox(10, new Label("My Watch History:"), historyView);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 500, 400);
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
                        String movieInfo = String.format("%s - %s (%d) | Genre: %s | Rating: %.1f", 
                            movie.getId(), movie.getTitle(), movie.getYear(), movie.getGenre(), movie.getRating());
                        recommendationItems.add(movieInfo);
                    }
                }
                
                recommendationsView.setItems(FXCollections.observableArrayList(recommendationItems));
                
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a valid number for recommendations count.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(
            typeLabel, typeComboBox, 
            countLabel, countField, 
            recommendButton, 
            new Label("Recommendations:"), 
            recommendationsView
        );

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
}