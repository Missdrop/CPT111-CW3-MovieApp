package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import cpt111.group76.storage.MovieManager;

public class AddNewMovie {
    private MovieManager movieManager;

    public AddNewMovie(MovieManager movieManager) {
        this.movieManager = movieManager;
    }

    public void show(BrowseMovies browseMovies) {
        Stage stage = new Stage();
        stage.setTitle("Add New Movie");
        stage.setOnCloseRequest(e -> {
            browseMovies.refresh();
        });


        // Create form elements
        Label titleLabel = new Label("Add New Movie to Database");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField titleField = new TextField();
        titleField.setPromptText("Movie Title");
        
        // Genre ComboBox instead of TextField
        ComboBox<String> genreComboBox = new ComboBox<>();
        genreComboBox.getItems().addAll(
            "Action", "Adventure", "Animation", "Comedy", "Crime",
            "Documentary", "Drama", "Family", "Fantasy", "History",
            "Horror", "Music", "Mystery", "Romance", "Science Fiction",
            "Thriller", "War", "Western"
        );
        genreComboBox.setPromptText("Select Genre");
        genreComboBox.setStyle("-fx-pref-width: 200px;");
        
        TextField yearField = new TextField();
        yearField.setPromptText("Release Year");
        
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating (0.0 - 10.0)");

        Button addButton = new Button("Add Movie");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px;");
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        addButton.setOnAction(e -> {
            // Validate and add movie
            String title = titleField.getText().trim();
            String genre = genreComboBox.getValue();
            String yearText = yearField.getText().trim();
            String ratingText = ratingField.getText().trim();

            // Validate inputs
            if (title.isEmpty() || genre == null || genre.isEmpty() || yearText.isEmpty() || ratingText.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                return;
            }

            try {
                int year = Integer.parseInt(yearText);
                double rating = Double.parseDouble(ratingText);

                // Validate year and rating ranges
                if (year < 1880 || year > 2030) {
                    statusLabel.setText("Please enter a valid year (1880-2030).");
                    statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    return;
                }

                if (rating < 0.0 || rating > 10.0) {
                    statusLabel.setText("Please enter a valid rating (0.0-10.0).");
                    statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    return;
                }

                // Add movie using MovieManager
                boolean success = movieManager.addMovie(title, genre, year, rating);
                if (success) {
                    statusLabel.setText("Movie added successfully!");
                    statusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    
                    // Clear fields after successful addition
                    titleField.clear();
                    genreComboBox.setValue(null);
                    yearField.clear();
                    ratingField.clear();
                    
                    // Save the movie database
                    movieManager.save();
                } else {
                    statusLabel.setText("Failed to add movie. Movie might already exist.");
                    statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }

            } catch (NumberFormatException ex) {
                statusLabel.setText("Please enter valid numbers for year and rating.");
                statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
        });

        // Create layout
        VBox layout = new VBox(12,
            titleLabel,
            titleField,
            yearField,
            ratingField,
            genreComboBox,
            addButton,
            statusLabel
        );
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Scene scene = new Scene(layout, 400, 400); // Slightly taller to accommodate the label
        stage.setScene(scene);
        stage.show();
    }
}