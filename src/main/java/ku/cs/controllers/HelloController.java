package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HelloController {
    @FXML private VBox root;
    @FXML private Label welcomeText;
    @FXML private Pane imagePane;

    private boolean clicked;

    @FXML
    protected void onHelloButtonClick() {
        if (!clicked) {
            clicked = true;
            root.getChildren().add(createPane());
            welcomeText.setText("Happy Coding!");
        }
    }

    private Pane createPane() {
        StackPane pane = new StackPane();
        pane.setPrefWidth(800);
        pane.setAlignment(Pos.CENTER);

        Image image = new Image(getClass().getResourceAsStream("/images/happy-coding.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(450);

        pane.getChildren().add(imageView);
        return pane;
    }
}