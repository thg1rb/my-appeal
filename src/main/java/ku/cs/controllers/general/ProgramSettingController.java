package ku.cs.controllers.general;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

public class ProgramSettingController {
    @FXML private ChoiceBox<String> themeChoiceBox;

    @FXML private ChoiceBox<String> fontChoiceBox;

    @FXML private ChoiceBox<String> fontSizeChoiceBox;

    @FXML private Pane navbarAnchorPane;
    private User user;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        themeChoiceBox.getItems().addAll("Dark Theme", "Light Theme");

        fontChoiceBox.getItems().addAll("Arial", "Times New Roman", "Verdana");

        fontSizeChoiceBox.getItems().addAll("12px", "16px", "20px", "24px");


    }


}


