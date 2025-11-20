package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;
import cpt111.group76.storage.MovieManager;

public class ViewWatchlist {
    private User user;
    private TableView<Movie> watchlistTable;
    private MovieManager movieManager;
    private UserManager userManager;


    public ViewWatchlist(User user, MovieManager movieManager, UserManager userManager) {
        this.user = user;
        this.userManager = userManager;
        this.movieManager = movieManager;
    }


    public void show() {
        Stage stage = new Stage();
        stage.setTitle("My Watchlist");

        // Create TableView
        watchlistTable = new TableView<>();
        setupTableColumns();
        updateWatchlistData();

        // Action buttons
        Button removeButton = new Button("Remove Selected from Watchlist");
        Button markWatchedButton = new Button("Mark Selected as Watched");
        Label statusLabel = new Label();

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Movie selected = watchlistTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    String movieId = selected.getId();
                    boolean success = user.removeFromWatchlist(movieId);
                    if (success) {
                        statusLabel.setText("Successfully removed from watchlist!");
                        userManager.updateUser(user);
                        updateWatchlistData();
                    } else {
                        statusLabel.setText("Failed to remove from watchlist.");
                    }
                } else {
                    statusLabel.setText("Please select a movie first.");
                }
            }
        });

        markWatchedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Movie selected = watchlistTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    String movieId = selected.getId();
                    user.addToHistory(movieId);
                    statusLabel.setText("Successfully marked as watched and removed from watchlist!");
                    userManager.updateUser(user);
                    updateWatchlistData();
                } else {
                    statusLabel.setText("Please select a movie first.");
                }
            }
        });

        HBox buttonBox = new HBox(10, removeButton, markWatchedButton);
        VBox layout = new VBox(10, new Label("My Watchlist:"), watchlistTable, buttonBox, statusLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 500);
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
        titleColumn.setMinWidth(200);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Genre column
        TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setMinWidth(100);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Year column
        TableColumn<Movie, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setMinWidth(60);
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Rating column
        TableColumn<Movie, Double> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(60);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Add columns individually to avoid type safety warning
        watchlistTable.getColumns().add(idColumn);
        watchlistTable.getColumns().add(titleColumn);
        watchlistTable.getColumns().add(genreColumn);
        watchlistTable.getColumns().add(yearColumn);
        watchlistTable.getColumns().add(ratingColumn);

        // Make the table take up available space
        watchlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }


    private void updateWatchlistData() {
        List<Movie> watchlistData = new ArrayList<>();
        for (String movieId : user.getWatchlist().getMovieIdSet()) {
            Movie movie = movieManager.getMovie(movieId);
            if (movie != null) {
                watchlistData.add(movie);
            }
        }

        watchlistTable.setItems(FXCollections.observableArrayList(watchlistData));
    }
}