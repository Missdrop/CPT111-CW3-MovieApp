package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.layout.Priority;
import java.util.HashMap;

import cpt111.group76.App;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;

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
            new App().start(new Stage());
            primaryStage.close();
        });

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        changePasswordButton.setOnAction(e -> {
            new ChangePassword(user).start(new Stage());
        });

        // Get Premium button - only show for Basic Users
        Button getPremiumButton = null;
        if (!user.isPremium()) {
            getPremiumButton = new Button("Get Premium");
            getPremiumButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
            getPremiumButton.setOnAction(e -> {
                new GetPremium(user, primaryStage).show();
            });
        }

        HBox buttonBox;
        if (!user.isPremium()) {
            buttonBox = new HBox(10, changePasswordButton, getPremiumButton, logoutButton);
        } else {
            buttonBox = new HBox(10, changePasswordButton, logoutButton);
        }
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

        browseMoviesButton.setOnAction(e -> new BrowseMovies(user, movieDatabase, movieManager).show());
        viewWatchlistButton.setOnAction(e -> new ViewWatchlist(user, movieDatabase).show());
        viewHistoryButton.setOnAction(e -> new ViewHistory(user, movieDatabase).show());
        getRecommendationsButton.setOnAction(e -> new Recommendation(user, movieDatabase).show());

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
}