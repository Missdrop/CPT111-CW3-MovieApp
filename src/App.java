package Group76;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        File file = new File("movies.csv");
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                System.out.println(line);
            }
            input.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
