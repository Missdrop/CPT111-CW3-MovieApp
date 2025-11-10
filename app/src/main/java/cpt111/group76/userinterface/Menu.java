package cpt111.group76.userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.layout.Priority;

import cpt111.group76.App;
import cpt111.group76.storage.MovieManager;
import cpt111.group76.storage.UserManager;
import cpt111.group76.user.User;

public class Menu extends Application{
    private User user;
    private static MovieManager movieManager = new MovieManager();


    public Menu(User user){
        this.user = user;
    }


    @Override
    public void start(Stage primaryStage) {
        // when the window is closed, save and close movieManager
        primaryStage.setOnCloseRequest(e -> {
            movieManager.save();
            movieManager.close();
            new UserManager().updateUser(user);
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            movieManager.save();
            movieManager.close();
            new App().start(new Stage());
            primaryStage.close();
        });

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> {
            movieManager.save();
            movieManager.close();
            new ChangePassword(user).start(new Stage());
            primaryStage.close();
        });

        Text usernameText = new Text("User: " + user.getUsername() + " Type: " + (user.isPremium() ? "[Premium]" : "[Non-Premium]"));

        HBox topBar = new HBox();
        topBar.getChildren().addAll(usernameText, changePasswordButton, logoutButton);
        HBox.setHgrow(usernameText, Priority.ALWAYS);
        HBox.setMargin(usernameText, new Insets(10));
        HBox.setMargin(changePasswordButton, new Insets(10));
        HBox.setMargin(logoutButton, new Insets(10));

        Scene scene = new Scene(topBar, 500, 300);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }
}
