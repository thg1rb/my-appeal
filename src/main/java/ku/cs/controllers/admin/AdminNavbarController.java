package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

public class AdminNavbarController {
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
    void onUserButtonClicked() {
        try{
            FXRouter.goTo("admin-user-manage",user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onStaffButtonClicked() {
        try{
            FXRouter.goTo("admin-staff-manage",user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onFacultyButtonClicked() {
        try{
            FXRouter.goTo("admin-faculty-manage",user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDashboardButtonClicked() {
        try{
            FXRouter.goTo("admin-dashboard",user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProfileSettingButtonClicked() {
        try{
            FXRouter.goTo("profile-setting",user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProgramSettingButtonClicked() {

    }

    @FXML
    void onLogoutButtonClick() {
        try{
            FXRouter.goTo("login");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
