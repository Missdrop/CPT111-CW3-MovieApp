package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import cpt111.group76.user.PremiumUser;
import cpt111.group76.user.BasicUser;
import cpt111.group76.user.User;
import cpt111.group76.storage.UserManager;

public class Login extends Application{
    private UserManager userManager;
    private boolean openedMenu = false;


    public Login(UserManager userManager){
        this.userManager = userManager;
    }


    @Override
    public void start(Stage primaryStage){
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (UserManager.authenticate(username, password)){
                User user = userManager.getUser(username);
                if (user.isPremium()) {
                    user = new PremiumUser(user);
                } else {
                    user = new BasicUser(user);
                }

                openMenu(user);
                primaryStage.close();
            };
        });

        VBox vbox = new VBox(10, usernameField, passwordField, loginButton);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
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
