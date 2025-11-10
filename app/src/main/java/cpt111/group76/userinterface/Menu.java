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
import javafx.geometry.Pos;
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

        // Create main title
        Label titleLabel = new Label("Movie Recommendation & Tracker");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // User info
        Text usernameText = new Text("Welcome, " + user.getUsername() + "! (" + 
            (user.isPremium() ? "Premium User" : "Basic User") + ")");
        usernameText.setStyle("-fx-font-size: 14px; -fx-fill: #7f8c8d;");

        // Top bar with title and user info
        VBox titleBox = new VBox(5, titleLabel, usernameText);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // Action buttons
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            movieManager.save();
            movieManager.close();
            new App().start(new Stage());
            primaryStage.close();
        });

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        changePasswordButton.setOnAction(e -> {
            new ChangePassword(user).start(new Stage());
        });

        HBox buttonBox = new HBox(10, changePasswordButton, logoutButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Top bar container
        HBox topBar = new HBox();
        topBar.getChildren().addAll(titleBox, buttonBox);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 1 0;");

        // Main menu buttons with better styling
        Button browseMoviesButton = createMenuButton("Browse Movies", "#2ecc71");
        Button viewWatchlistButton = createMenuButton("View Watchlist", "#3498db");
        Button viewHistoryButton = createMenuButton("View History", "#9b59b6");
        Button getRecommendationsButton = createMenuButton("Recommendation", "#e67e22");

        browseMoviesButton.setOnAction(e -> openBrowseMovies());
        viewWatchlistButton.setOnAction(e -> openViewWatchlist());
        viewHistoryButton.setOnAction(e -> openViewHistory());
        getRecommendationsButton.setOnAction(e -> openGetRecommendations());

        // Create a grid layout for buttons
        HBox firstButtonRow = new HBox(20, browseMoviesButton, viewWatchlistButton);
        HBox secondButtonRow = new HBox(20, viewHistoryButton, getRecommendationsButton);
        firstButtonRow.setAlignment(Pos.CENTER);
        secondButtonRow.setAlignment(Pos.CENTER);

        VBox menuButtons = new VBox(20, firstButtonRow, secondButtonRow);
        menuButtons.setPadding(new Insets(40));
        menuButtons.setAlignment(Pos.CENTER);

        // Stats panel
        Label statsLabel = new Label("Your Stats: " +
            user.getWatchlist().length() + " movies in watchlist • " +
            user.getHistory().length() + " movies watched");
        statsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        VBox statsBox = new VBox(statsLabel);
        statsBox.setPadding(new Insets(10));
        statsBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(menuButtons);
        root.setBottom(statsBox);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movie Recommendation & Tracker - Main Menu");
        primaryStage.show();
    }

    // Helper method to create styled menu buttons
    private Button createMenuButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                    "-fx-pref-width: 200px; -fx-pref-height: 80px; -fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-text-fill: white; " +
                    "-fx-font-size: 16px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 80px; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                    "-fx-font-size: 16px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 80px; -fx-background-radius: 10;"));
        return button;
    }


    private void openBrowseMovies() {
        Stage stage = new Stage();
        stage.setTitle("Browse Movies - Select movies to add to watchlist or mark as watched");

        // Add title
        Label titleLabel = new Label("Browse All Movies");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ListView<String> movieList = new ListView<>();
        ArrayList<String> movieItems = new ArrayList<>();

        for (Movie movie : movieDatabase.values()) {
            movieItems.add(movie.toString());
        }

        movieList.setItems(FXCollections.observableArrayList(movieItems));

        // Action buttons with styling
        Button addToWatchlistButton = new Button("Add to Watchlist");
        addToWatchlistButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        
        Button markWatchedButton = new Button("Mark as Watched");
        markWatchedButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        
        Button removeFromWatchlistButton = new Button("Remove from Watchlist");
        removeFromWatchlistButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        
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

        HBox buttonBox = new HBox(10, addToWatchlistButton, markWatchedButton, removeFromWatchlistButton);
        VBox layout = new VBox(10, titleLabel,
                            new Label("Select a movie to perform actions:"),
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