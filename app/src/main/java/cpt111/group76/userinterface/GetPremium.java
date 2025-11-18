package cpt111.group76.userinterface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import cpt111.group76.storage.UserManager;
import cpt111.group76.user.*;

public class GetPremium {
    private User user;
    private Stage menuStage;
    private UserManager userManager;


    public GetPremium(User user, Stage menuStage, UserManager userManager) {
        this.user = user;
        this.menuStage = menuStage;
        this.userManager = userManager;
    }


    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Get Premium");

        // Create the premium upgrade page
        Label titleLabel = new Label("Get Premium!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #f39c12;");

        Label descriptionLabel = new Label(
                "Premium Users is much better than Basic Users! Upgrade now to unlock exclusive features and enhance your movie experience.");
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d; -fx-wrap-text: true;");
        descriptionLabel.setMaxWidth(400);

        // Add more premium benefits
        Label benefitsLabel = new Label("Premium Benefits:");
        benefitsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox benefitsList = new VBox(5);
        benefitsList.getChildren().addAll(createBenefitItem("✓ Watchlist capacity: 100 movies (vs 20 for Basic)"),
                createBenefitItem("✓ Add Movies to Movie Database"),
                createBenefitItem("✓ Enhanced recommendation algorithms"));

        Button upgradeButton = new Button("Get Premium");
        upgradeButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 16px; "
                + "-fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 40px;");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        upgradeButton.setOnAction(e -> {
            // Convert user to Premium
            user = new PremiumUser(user);

            // Update user in UserManager
            userManager.updateUser(user);

            stage.close();
            menuStage.close();

            // Create new menu with updated user
            Menu newMenu = new Menu(user, userManager);
            newMenu.start(new Stage());
        });

        VBox layout = new VBox(20, titleLabel, descriptionLabel, benefitsLabel, benefitsList, upgradeButton,
                statusLabel);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }


    private Label createBenefitItem(String text) {
        Label benefit = new Label(text);
        benefit.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        return benefit;
    }
}