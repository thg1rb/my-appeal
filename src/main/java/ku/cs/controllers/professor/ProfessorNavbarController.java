package ku.cs.controllers.professor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.persons.Advisor;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

import java.io.File;
import java.io.IOException;

public class ProfessorNavbarController {
    @FXML private Circle profileImageCircle;

    @FXML private Label roleLabel;
    @FXML private Label usernameLabel;

    private User user;

    @FXML
    private void initialize(){
        user = (Advisor) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
        profileImageCircle.setFill(new ImagePattern(image));
    }

    @FXML
    void onStudentListButtonClick(){
        try {
            FXRouter.goTo("professor-student-list", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onStudentAppealButtonClick(){
        try {
            FXRouter.goTo("professor-student-appeal", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProgramSettingButtonClicked(){
        try {
            FXRouter.goTo("program-setting",user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
