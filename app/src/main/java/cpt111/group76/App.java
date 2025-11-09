package cpt111.group76;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

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

        // open the Login UI in a new Stage
        loginButton.setOnAction(e -> {
            Login login = new Login(userManager);
            login.start(new Stage());
            primaryStage.close();
        });

        // open the Register UI in a new Stage
        registerButton.setOnAction(e -> {
            Register register = new Register(userManager);
            register.start(new Stage());
            primaryStage.close();
        });

        HBox hbox = new HBox(10, loginButton, registerButton);
        hbox.setPadding(new Insets(20));

        Scene scene = new Scene(hbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movie Recommendation & Tracker");
        primaryStage.show();
    }
}