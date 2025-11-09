package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

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

        BorderPane borderPane = new BorderPane();
        //右上角
        borderPane.setTop(logoutButton);
        BorderPane.setAlignment(logoutButton, Pos.TOP_RIGHT);
        BorderPane.setMargin(logoutButton, new Insets(10));

        Scene scene = new Scene(borderPane, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }
}
