package ku.cs.controllers.major;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorApproverManageController {

    private User user;

    @FXML Label usernameLabel;
    @FXML Label roleLabel;

    public void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());
    }

    @FXML
    protected void onApproverManageButtonClick() {
        try {
            FXRouter.goTo("major-approver-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNisitManageButtonClick() {
        try {
            FXRouter.goTo("major-nisit-manage", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAppealManageButtonClick() {
        try {
            FXRouter.goTo("major-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onLogoutButtonClick(){
        try{
            FXRouter.goTo("login");
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
