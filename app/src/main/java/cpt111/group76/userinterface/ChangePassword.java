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
    private User user;

    public ChangePassword(User user){
        this.user = user;
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
        Label resultLabel = new Label();

        changePasswordButton.setOnAction(e -> {
            String password = passwordField.getText();
            String newPassword = newPasswordField.getText();
            String repeatNewPassword = repeatNewPasswordField.getText();
            
            if (!user.verifyPassword(password)) {
                resultLabel.setText("Current password is incorrect.");
                return;
            }
            
            if (password.equals(newPassword)) {
                resultLabel.setText("New password must be different from current password.");
                return;
            }
            
            if (!newPassword.equals(repeatNewPassword)) {
                resultLabel.setText("New passwords do not match.");
                return;
            }
            
            String passwordCheck = UserManager.checkPassword(newPassword);
            if (passwordCheck != null) {
                resultLabel.setText(passwordCheck);
                return;
            }
            
            user.setPassword(newPassword);
            new UserManager().updateUser(user);
            resultLabel.setText("Password changed successfully!");
        });

        VBox vbox = new VBox(10, passwordField, newPasswordField, repeatNewPasswordField, changePasswordButton, resultLabel);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Password");
        primaryStage.show();
    }
}