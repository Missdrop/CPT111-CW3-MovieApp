package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;

import cpt111.group76.user.User;
import cpt111.group76.user.BasicUser;
import cpt111.group76.storage.UserManager;
import cpt111.group76.exception.*;

public class Register {
    private UserManager userManager;
    private Stage appStage;



    public Register(UserManager userManager, Stage appStage){
        this.userManager = userManager;
        this.appStage = appStage;
    }


    public void show(){
        Stage primaryStage = new Stage();

        // When this login stage is closed, show the main app stage again
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                appStage.show();
            }
        });

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

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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

                // Check if passwords match
                if (!password.equals(repeatPassword)) {
                    statusLabel.setText("Passwords do not match.");
                    return;
                }

                // Try to add user
                try {
                    userManager.addUser(username, password);
                } catch (UsernameValidationException ex) {
                    statusLabel.setText(ex.getMessage());
                    return;
                } catch (PasswordValidationException ex) {
                    statusLabel.setText(ex.getMessage());
                    return;
                }

                User user = new BasicUser(userManager.getUser(username));

                openMenu(user, userManager);
                appStage.close();
                primaryStage.close();
            }
        });

        // Add Enter key support
        usernameField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                registerButton.fire();
            }
        });
        passwordField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                registerButton.fire();
            }
        });
        repeatPasswordField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                registerButton.fire();
            }
        });

        VBox vbox = new VBox(10, usernameField, passwordField, repeatPasswordField, registerButton, statusLabel);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
        primaryStage.show();
    }


    private void openMenu(User user, UserManager userManager) {
        Menu menu = new Menu(user, userManager);
        menu.start(new Stage());
    }
}