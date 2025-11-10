package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import cpt111.group76.user.User;
import cpt111.group76.App;
import cpt111.group76.storage.UserManager;

public class ChangePassword extends Application{
    private User user;


    public ChangePassword(User user){
        this.user = user;
    }


    @Override
    public void start(Stage primaryStage){
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField repeatNewPasswordField = new PasswordField();
        repeatNewPasswordField.setPromptText("Repeat New Password");

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> {
            String password = passwordField.getText();
            String newPassword = newPasswordField.getText();
            String repeatNewPassword = repeatNewPasswordField.getText();
            if (UserManager.authenticate(user.getUsername(), password)
                    && !password.equals(newPassword)
                    && newPassword.equals(repeatNewPassword)
                    && UserManager.checkPassword(newPassword) == null) {
                user.setPassword(newPassword);
                new UserManager().updateUser(user);
                new App().start(new Stage());
                primaryStage.close();
            }
        });

        VBox vbox = new VBox(10, passwordField, newPasswordField, repeatNewPasswordField, changePasswordButton);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
