package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import cpt111.group76.user.User;
import cpt111.group76.storage.UserManager;

public class Register extends Application{
    private UserManager userManager;

    public Register(UserManager userManager){
        this.userManager = userManager;
    }


    @Override
    public void start(Stage primaryStage){
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userManager.addUser(username, password)){
                User user = userManager.getUser(username);
                openMenu(user);
                primaryStage.close();
            }
        });

        javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(10, usernameField, passwordField, registerButton);
        vbox.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
        primaryStage.show();
    }


    private void openMenu(User user) {
        Menu menu = new Menu(user);
        menu.start(new Stage());
    }
}
