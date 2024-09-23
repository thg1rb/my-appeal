package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

import java.io.File;
import java.io.IOException;

public class AdminNavbarController {
    @FXML private Circle profileImageCircle;

    @FXML private Label roleLabel;
    @FXML private Label usernameLabel;

    private User user;

    @FXML
    private void initialize(){
        user = (AdminUser) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
        profileImageCircle.setFill(new ImagePattern(image));
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
        try{
            FXRouter.goTo("program-setting", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
