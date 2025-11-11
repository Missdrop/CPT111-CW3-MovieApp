package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;

public class ViewHistory {
    private User user;
    private HashMap<String, Movie> movieDatabase;
    private TableView<Movie> historyTable;
    private ObservableList<Movie> historyData;
    private HashMap<String, String> movieDateMap;

    public ViewHistory(User user, HashMap<String, Movie> movieDatabase) {
        this.user = user;
        this.movieDatabase = movieDatabase;
        this.historyData = FXCollections.observableArrayList();
        this.movieDateMap = user.getHistory().getHistoryMap();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("My Watch History");

        // Create TableView
        historyTable = new TableView<>();
        setupTableColumns();
        updateHistoryData();

        // Action button
        Button removeButton = new Button("Remove Selected from History");
        Label statusLabel = new Label();

        removeButton.setOnAction(e -> {
            Movie selected = historyTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = selected.getId();
                boolean success = user.removeFromHistory(movieId);
                if (success) {
                    statusLabel.setText("Successfully removed from history!");
                    new UserManager().updateUser(user);
                    updateHistoryData();
                } else {
                    statusLabel.setText("Failed to remove from history.");
                }
            } else {
                statusLabel.setText("Please select a movie first.");
            }
        });

        VBox layout = new VBox(10, new Label("My Watch History:"), historyTable, removeButton, statusLabel);
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

        // Date column - custom cell value factory to get date from movieDateMap
        TableColumn<Movie, String> dateColumn = new TableColumn<>("Watched Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(cellData -> {
            String movieId = cellData.getValue().getId();
            String date = movieDateMap.get(movieId);
            return new SimpleStringProperty(date != null ? date : "Unknown date");
        });

        // Add columns individually to avoid type safety warning
        historyTable.getColumns().add(idColumn);
        historyTable.getColumns().add(titleColumn);
        historyTable.getColumns().add(genreColumn);
        historyTable.getColumns().add(yearColumn);
        historyTable.getColumns().add(ratingColumn);
        historyTable.getColumns().add(dateColumn);

        // Make the table take up available space
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void updateHistoryData() {
        historyData.clear();
        movieDateMap = user.getHistory().getHistoryMap();
        
        for (String movieId : movieDateMap.keySet()) {
            Movie movie = movieDatabase.get(movieId);
            if (movie != null) {
                historyData.add(movie);
            }
        }

        historyTable.setItems(historyData);
    }
}