package cpt111.group76;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Modality;

import cpt111.group76.userinterface.Login;
import cpt111.group76.userinterface.Register;
import cpt111.group76.storage.UserManager;

public class App extends Application{
    private static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        launch(args);
        userManager.save();
        userManager.close();
    }

    @Override
    public void start(Stage primaryStage) {
        // Create main title
        Label titleLabel = new Label("Movie Recommendation & Tracker");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Create subtitle
        Label subtitleLabel = new Label("Your Personal Movie Companion");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        // Create login and register buttons
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 120px; -fx-pref-height: 35px;");
        
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 120px; -fx-pref-height: 35px;");

        // Set button actions (same as before)
        loginButton.setOnAction(e -> {
            Login login = new Login(userManager);
            Stage loginStage = new Stage();
            loginStage.initOwner(primaryStage);
            loginStage.initModality(Modality.WINDOW_MODAL);
            loginStage.setOnHidden(ev -> {
                if (login.openedMenu()) {
                    primaryStage.close();
                } else {
                    primaryStage.show();
                }
            });
            primaryStage.hide();
            login.start(loginStage);
        });

        registerButton.setOnAction(e -> {
            Register register = new Register(userManager);
            Stage registerStage = new Stage();
            registerStage.initOwner(primaryStage);
            registerStage.initModality(Modality.WINDOW_MODAL);
            registerStage.setOnHidden(ev -> {
                if (register.openedMenu()) {
                    primaryStage.close();
                } else {
                    primaryStage.show();
                }
            });
            primaryStage.hide();
            register.start(registerStage);
        });

        // Create button container
        HBox buttonBox = new HBox(20, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Create main container with title, subtitle and buttons
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40, 20, 40, 20));
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Add all elements to main container
        mainContainer.getChildren().addAll(titleLabel, subtitleLabel, buttonBox);

        Scene scene = new Scene(mainContainer, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movie Recommendation & Tracker");
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }
}