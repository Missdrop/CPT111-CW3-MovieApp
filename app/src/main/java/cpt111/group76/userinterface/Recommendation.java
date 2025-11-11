package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;
import cpt111.group76.movie.Movie;
import cpt111.group76.recommendation.Engine;

public class Recommendation {
    private User user;
    private HashMap<String, Movie> movieDatabase;
    private TableView<Movie> recommendationsTable;
    private ObservableList<Movie> recommendationsData;


    public Recommendation(User user, HashMap<String, Movie> movieDatabase) {
        this.user = user;
        this.movieDatabase = movieDatabase;
        this.recommendationsData = FXCollections.observableArrayList();
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

        // Create TableView for recommendations
        recommendationsTable = new TableView<>();
        setupTableColumns();

        // Add to watchlist button for recommendations
        Button addToWatchlistButton = new Button("Add Selected to Watchlist");
        Label statusLabel = new Label();

        recommendButton.setOnAction(e -> {
            try {
                String type = typeComboBox.getValue();
                int count = Integer.parseInt(countField.getText());

                Engine engine = new Engine(movieDatabase, user);
                String[] recommendations = engine.recommendation(type, count);

                recommendationsData.clear();
                for (String movieId : recommendations) {
                    Movie movie = movieDatabase.get(movieId);
                    if (movie != null) {
                        recommendationsData.add(movie);
                    }
                }

                recommendationsTable.setItems(recommendationsData);
                statusLabel.setText("Found " + recommendationsData.size() + " recommendations.");

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a valid number for recommendations count.");
                alert.showAndWait();
            }
        });

        addToWatchlistButton.setOnAction(e -> {
            Movie selected = recommendationsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String movieId = selected.getId();
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
            } else {
                statusLabel.setText("Please select a recommendation first.");
            }
        });

        HBox recommendationButtons = new HBox(10, recommendButton, addToWatchlistButton);

        layout.getChildren().addAll(typeLabel, typeComboBox, countLabel, countField, recommendationButtons,
                new Label("Recommendations:"), recommendationsTable, statusLabel);

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
        recommendationsTable.getColumns().add(idColumn);
        recommendationsTable.getColumns().add(titleColumn);
        recommendationsTable.getColumns().add(genreColumn);
        recommendationsTable.getColumns().add(yearColumn);
        recommendationsTable.getColumns().add(ratingColumn);

        // Make the table take up available space
        recommendationsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
}