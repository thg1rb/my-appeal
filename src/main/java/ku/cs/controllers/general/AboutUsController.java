package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;

public class AboutUsController {

    @FXML private AnchorPane mainPane;

    private User user;
    @FXML private Pane navbarAnchorPane;


    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }
}
