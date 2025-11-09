package cpt111.group76.userinterface;

import javafx.application.Application;

import cpt111.group76.user.User;

public class Menu extends Application{
    private User user;


    public Menu(User user){
        this.user = user;
    }


    @Override
    public void start(javafx.stage.Stage primaryStage) {
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }
}
