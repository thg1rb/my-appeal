package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;

public class AdminDashboardController {
    @FXML private Pane navbarAnchorPane;

    private User user;

    public void initialize() {
        user = (AdminUser) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
