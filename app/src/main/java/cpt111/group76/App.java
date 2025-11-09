package cpt111.group76;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Modality;

import cpt111.group76.userinterface.Login;
import cpt111.group76.userinterface.Register;
import cpt111.group76.storage.UserManager;


public class App extends Application{
    private static UserManager userManager = new UserManager();


    public static void main(String[] args) {
        launch(args);
        userManager.save();
        userManager.close();
    }


    @Override
    public void start(Stage primaryStage) {
        // main page, which has two buttons: login and register, title is Movie Recommendation & Tracker
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // open the Login UI in a new Stage (modal). hide primaryStage while login window is open,
        // and show it again when the login window is closed
        loginButton.setOnAction(e -> {
            Login login = new Login(userManager);
            Stage loginStage = new Stage();
            loginStage.initOwner(primaryStage);
            loginStage.initModality(Modality.WINDOW_MODAL);
            // when login window is closed, show main window again only if login did NOT open Menu
            loginStage.setOnHidden(ev -> {
                if (login.openedMenu()) {
                    // login opened the Menu, close the main stage permanently
                    primaryStage.close();
                } else {
                    primaryStage.show();
                }
            });
            // hide main window and show login
            primaryStage.hide();
            login.start(loginStage);
        });

        registerButton.setOnAction(e -> {
            Register register = new Register(userManager);
            Stage registerStage = new Stage();
            registerStage.initOwner(primaryStage);
            registerStage.initModality(Modality.WINDOW_MODAL);
            registerStage.setOnHidden(ev -> {
                if (register.openedMenu()) {
                    primaryStage.close();
                } else {
                    primaryStage.show();
                }
            });
            primaryStage.hide();
            register.start(registerStage);
        });

        HBox hbox = new HBox(20, loginButton, registerButton);
        hbox.setAlignment(Pos.CENTER);

        VBox root = new VBox(hbox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movie Recommendation & Tracker");
        primaryStage.show();
    }
}