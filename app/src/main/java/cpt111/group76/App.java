package cpt111.group76;

import cpt111.group76.storage.MovieManager;
import cpt111.group76.storage.UserManager;

public class App {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        MovieManager movieManager = new MovieManager();


        userManager.close();
        movieManager.close();
    }
}
