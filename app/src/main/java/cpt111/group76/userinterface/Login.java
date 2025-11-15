package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import cpt111.group76.user.User;
import cpt111.group76.storage.UserManager;

public class Login {
    private UserManager userManager;
    private Stage appStage;


    public Login(UserManager userManager, Stage appStage) {
        this.userManager = userManager;
        this.appStage = appStage;
    }


    public void show() {
        Stage primaryStage = new Stage();

        // When this login stage is closed, show the main app stage again
        primaryStage.setOnCloseRequest(e ->{
            appStage.show();
        });

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setWrapText(true);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both username and password.");
                return;
            }

            if (userManager.authenticate(username, password)) {
                User user = userManager.getUser(username);

                openMenu(user, userManager);
                appStage.close();
                primaryStage.close();

            } else {
                statusLabel.setText("Invalid username or password.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // Add Enter key support
        usernameField.setOnAction(e -> loginButton.fire());
        passwordField.setOnAction(e -> loginButton.fire());

        VBox vbox = new VBox(10, usernameField, passwordField, loginButton, statusLabel);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }


    private void openMenu(User user, UserManager userManager) {
        Menu menu = new Menu(user, userManager);
        menu.start(new Stage());
    }
}