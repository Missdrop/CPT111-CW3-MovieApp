package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import cpt111.group76.user.User;
import cpt111.group76.storage.UserManager;

public class ChangePassword extends Application{
    private UserManager userManager;
    private User user;


    public ChangePassword(User user, UserManager userManager){
        this.user = user;
        this.userManager = userManager;
    }


    @Override
    public void start(Stage primaryStage){
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Current Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField repeatNewPasswordField = new PasswordField();
        repeatNewPasswordField.setPromptText("Repeat New Password");

        Button changePasswordButton = new Button("Change Password");
        Label statusLabel = new Label();
        statusLabel.setWrapText(true);

        changePasswordButton.setOnAction(e -> {
            String password = passwordField.getText();
            String newPassword = newPasswordField.getText();
            String repeatNewPassword = repeatNewPasswordField.getText();
            
            if (!user.verifyPassword(password)) {
                statusLabel.setText("Current password is incorrect.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (password.equals(newPassword)) {
                statusLabel.setText("New password must be different from current password.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (!newPassword.equals(repeatNewPassword)) {
                statusLabel.setText("New passwords do not match.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            String passwordCheck = userManager.checkPassword(newPassword);
            if (passwordCheck != null) {
                statusLabel.setText(passwordCheck);
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            user.setPassword(newPassword);
            userManager.updateUser(user);
            statusLabel.setText("Password changed successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        });

        VBox vbox = new VBox(10, passwordField, newPasswordField, repeatNewPasswordField, changePasswordButton, statusLabel);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Password");
        primaryStage.show();
    }
}