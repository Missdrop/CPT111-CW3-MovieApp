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
import cpt111.group76.user.BasicUser;
import cpt111.group76.storage.UserManager;

public class Register extends Application{
    private UserManager userManager;
    private boolean openedMenu = false;


    public Register(UserManager userManager){
        this.userManager = userManager;
    }


    @Override
    public void start(Stage primaryStage){
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField repeatPasswordField = new PasswordField();
        repeatPasswordField.setPromptText("Repeat Password");

        Button registerButton = new Button("Register");
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setWrapText(true);

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String repeatPassword = repeatPasswordField.getText();
            
            // Clear previous status
            statusLabel.setText("");
            
            // Validate inputs
            if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                return;
            }
            
            // Check username requirements
            String usernameCheck = userManager.checkUsername(username);
            if (usernameCheck != null) {
                statusLabel.setText(usernameCheck);
                return;
            }
            
            // Check password requirements
            String passwordCheck = userManager.checkPassword(password);
            if (passwordCheck != null) {
                statusLabel.setText(passwordCheck);
                return;
            }
            
            // Check if passwords match
            if (!password.equals(repeatPassword)) {
                statusLabel.setText("Passwords do not match.");
                return;
            }
            
            // Try to add user
            if (userManager.addUser(username, password)) {
                User user = new BasicUser(userManager.getUser(username));

                openMenu(user, userManager);
                primaryStage.close();

            } else {
                statusLabel.setText("Username already exists.");
            }
        });

        // Add Enter key support
        usernameField.setOnAction(e -> registerButton.fire());
        passwordField.setOnAction(e -> registerButton.fire());
        repeatPasswordField.setOnAction(e -> registerButton.fire());

        VBox vbox = new VBox(10, usernameField, passwordField, repeatPasswordField, registerButton, statusLabel);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
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