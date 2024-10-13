package ku.cs.services;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Animation {

    // Singleton instance
    private static Animation instance;

    // Private constructor to prevent instantiation
    private Animation() {}

    // Static method to get the single instance of the class
    public static Animation getInstance() {
        if (instance == null) {
            instance = new Animation();
        }
        return instance;
    }

    // Method to switch scene with fade animation
    public void switchSceneWithFade(AnchorPane mainPane, String route, Object data) {
        // Fade out current Scene
        FXRouter.setAnimationType("fade", 0.2);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.2), mainPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();

        // Wait for Fade out finished
        fadeOut.setOnFinished(event -> {
            try {
                if (data!=null){
                    FXRouter.goTo(route, data);
                }
                else {
                    FXRouter.goTo(route);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void showPopUpWithEffect(Stage stage, Parent root) {
        // Set initial scale for the root node
        root.setScaleX(0);
        root.setScaleY(0);

        // Create scale transition for the pop-up
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), root);
        scaleTransition.setFromX(0);  // Start scale
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);    // End scale
        scaleTransition.setToY(1);

        // Show the stage before starting the animation
        stage.setScene(new Scene(root));
        stage.show();

        // Play the scale animation
        scaleTransition.play();
    }
}
