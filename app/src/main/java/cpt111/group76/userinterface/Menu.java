package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import cpt111.group76.App;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.user.User;

public class Menu extends Application{
    private User user;
    private static MovieManager movieManager = new MovieManager();


    public Menu(User user){
        this.user = user;
    }


    @Override
    public void start(javafx.stage.Stage primaryStage) {
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            movieManager.save();
            movieManager.close();
            new App().start(new Stage());
            primaryStage.close();
        });

        VBox vbox = new VBox(10, logoutButton);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }
}
