package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyAppealManageController {

    private User user;

    @FXML
    public void initialize(){
        user = (User) FXRouter.getData();
    }

    @FXML
    public void onApproverButtonClick() {
        try {
            FXRouter.goTo("faculty-approver-manage",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutButtonClick(){

        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
