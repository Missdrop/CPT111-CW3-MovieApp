package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;

// this class is a login gui with javafx, which has a username and password field and a login button
public class Login extends Application{
    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        primaryStage.setTitle("Login");
        // Create a button for logging in
        Button loginButton = new Button("Login");

        // Create a scene and add the button to it
        Scene scene = new Scene(loginButton, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


}
