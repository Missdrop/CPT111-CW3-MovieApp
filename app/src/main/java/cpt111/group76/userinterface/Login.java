package cpt111.group76.userinterface;

import javafx.application.Application;
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

public class Login extends Application {
    private UserManager userManager;
    private boolean openedMenu = false;


    public Login(UserManager userManager) {
        this.userManager = userManager;
    }


    @Override
    public void start(Stage primaryStage) {
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
        this.openedMenu = true;
        menu.start(new Stage());
    }


    public boolean openedMenu() {
        return this.openedMenu;
    }
}