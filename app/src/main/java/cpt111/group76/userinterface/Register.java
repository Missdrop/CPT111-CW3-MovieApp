package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String repeatPassword = repeatPasswordField.getText();
            if (password.equals(repeatPassword)
                    && userManager.addUser(username, password)
                    && User.checkPassword(password) == null){
                User user = new BasicUser(userManager.getUser(username));
                openMenu(user);
                primaryStage.close();
            }
        });

        VBox vbox = new VBox(10, usernameField, passwordField, repeatPasswordField, registerButton);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
        primaryStage.show();
    }


    private void openMenu(User user) {
        Menu menu = new Menu(user);
        this.openedMenu = true;
        menu.start(new Stage());
    }


    public boolean openedMenu() {
        return this.openedMenu;
    }
}
