package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import cpt111.group76.storage.UserManager;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;

import java.util.HashMap;

public class BrowseMovies {
    private User user;
    private HashMap<String, Movie> movieDatabase;
    private MovieManager movieManager;
    private TableView<Movie> movieTable;
    private ObservableList<Movie> movieData;

    public BrowseMovies(User user, HashMap<String, Movie> movieDatabase, MovieManager movieManager) {
        this.user = user;
        this.movieDatabase = movieDatabase;
        this.movieManager = movieManager;
        this.movieData = FXCollections.observableArrayList();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Browse Movies");

        // Add title
        Label titleLabel = new Label("Browse All Movies");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Create TableView
        movieTable = new TableView<>();
        setupTableColumns();
        updateMovieData();

        // Set row factory for color highlighting
        movieTable.setRowFactory(tv -> new TableRow<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                
                if (empty || movie == null) {
                    setStyle("");
                } else {
                    String movieId = movie.getId();
                    boolean inWatchlist = user.getWatchlist().contains(movieId);
                    boolean inHistory = user.getHistory().contains(movieId);
                    
                    if (inHistory) {
                        // Blue color for movies in history (same as markWatched button)
                        setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    } else if (inWatchlist) {
                        // Green color for movies in watchlist (same as addToWatchlist button)
                        setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

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
                new AddNewMovie(movieManager).show(this);
                // Refresh the movie table when the add movie window is closed
                stage.setOnHidden(ev -> updateMovieData());
            });
        }
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        addToWatchlistButton.setOnAction(e -> {
            Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                String movieId = selectedMovie.getId();
                if (user.getWatchlist().contains(movieId)) {
                    statusLabel.setText("Movie is already in your watchlist.");
                } else {
                    boolean success = user.addToWatchlist(movieId);
                    if (success) {
                        statusLabel.setText("Successfully added to watchlist!");
                        new UserManager().updateUser(user);
                        movieTable.refresh(); // Refresh to update row colors
                    } else {
                        statusLabel.setText("Failed to add to watchlist. Watchlist may be full.");
                    }
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        markWatchedButton.setOnAction(e -> {
            Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                String movieId = selectedMovie.getId();
                if (user.getHistory().contains(movieId)) {
                    statusLabel.setText("Movie is already in your watch history.");
                } else {
                    user.addToHistory(movieId);
                    statusLabel.setText("Successfully marked as watched!");
                    new UserManager().updateUser(user);
                    movieTable.refresh(); // Refresh to update row colors
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        removeFromWatchlistButton.setOnAction(e -> {
            Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                String movieId = selectedMovie.getId();
                if (!user.getWatchlist().contains(movieId)) {
                    statusLabel.setText("Movie is not in your watchlist.");
                } else {
                    boolean success = user.removeFromWatchlist(movieId);
                    if (success) {
                        statusLabel.setText("Successfully removed from watchlist!");
                        new UserManager().updateUser(user);
                        movieTable.refresh(); // Refresh to update row colors
                    } else {
                        statusLabel.setText("Failed to remove from watchlist.");
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

        VBox layout = new VBox(10, titleLabel, movieTable, buttonBox, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 600); // Larger to accommodate table
        stage.setScene(scene);
        stage.show();
    }

    private void setupTableColumns() {
        // ID column
        TableColumn<Movie, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(60);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Title column
        TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(300);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Genre column
        TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setMinWidth(80);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Year column
        TableColumn<Movie, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setMinWidth(60);
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Rating column
        TableColumn<Movie, Double> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(60);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        
        // Add all columns to the table
        movieTable.getColumns().add(idColumn);
        movieTable.getColumns().add(titleColumn);
        movieTable.getColumns().add(genreColumn);
        movieTable.getColumns().add(yearColumn);
        movieTable.getColumns().add(ratingColumn);
        
        // Make the table take up available space
        movieTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void updateMovieData() {
        movieData.clear();
        movieData.addAll(movieDatabase.values());
        movieTable.setItems(movieData);
    }


    public void refresh() {
        updateMovieData();
        movieTable.refresh();
    }
}