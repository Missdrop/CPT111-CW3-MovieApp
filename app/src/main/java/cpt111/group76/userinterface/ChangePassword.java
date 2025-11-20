package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;

import cpt111.group76.user.User;
import cpt111.group76.storage.UserManager;
import cpt111.group76.App;
import cpt111.group76.exception.PasswordValidationException;

public class ChangePassword{
    private UserManager userManager;
    private User user;
    private Stage menuStage;


    public ChangePassword(User user, UserManager userManager, Stage menuStage){
        this.user = user;
        this.userManager = userManager;
        this.menuStage = menuStage;
    }


    public void show(){
        Stage primaryStage = new Stage();
        // When this change password stage is closed, show the menu stage again
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                menuStage.show();
            }
        });

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Current Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField repeatNewPasswordField = new PasswordField();
        repeatNewPasswordField.setPromptText("Repeat New Password");

        Button changePasswordButton = new Button("Change Password");
        Label statusLabel = new Label();
        statusLabel.setWrapText(true);

        changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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

                try {
                    user.setPassword(newPassword);
                } catch (PasswordValidationException ex) {
                    statusLabel.setText(ex.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                userManager.updateUser(user);
                menuStage.close();
                primaryStage.close();
                try {
                    new App().start(new Stage());
                } catch (Exception ex) {
                    System.out.println("Error starting app: " + ex.getMessage());
                }
            }
        });

        VBox vbox = new VBox(10, passwordField, newPasswordField, repeatNewPasswordField, changePasswordButton, statusLabel);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Password");
        primaryStage.show();
    }
}