package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyNavbarController {
    @FXML private Label roleLabel;
    @FXML private Label usernameLabel;

    private User user;

    @FXML
    private void initialize(){
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());
    }

    @FXML
    void onAppealButtonClick(){
        try {
            FXRouter.goTo("faculty-appeal-manage", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onApproverButtonClick(){
        try {
            FXRouter.goTo("faculty-approver-manage",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProgramSettingClicked(){

    }

    @FXML
    void onLogoutButtonClicked(){
        try{
            FXRouter.goTo("login");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProfileSettingButtonClicked(){
        try{
            FXRouter.goTo("profile-setting",user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
