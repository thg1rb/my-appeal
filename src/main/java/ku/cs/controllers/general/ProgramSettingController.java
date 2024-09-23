package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import ku.cs.cs211671project.MainApplication;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class ProgramSettingController {
    @FXML private ChoiceBox<String> themeChoiceBox;

    @FXML private ChoiceBox<String> fontChoiceBox;

    @FXML private ChoiceBox<String> fontSizeChoiceBox;

    @FXML private Pane navbarAnchorPane;
    private User user;
    private Scene scene; // The main scene of the app

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        themeChoiceBox.getItems().addAll("Dark", "Light");

        fontChoiceBox.getItems().addAll();

        fontSizeChoiceBox.getItems().addAll("Small", "Medium", "Large");


    }



    @FXML
    public void onAboutUsButtonClick() {
        try {
            FXRouter.goTo("about-us", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


