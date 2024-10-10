package ku.cs.controllers.faculty;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.services.Animation;
import ku.cs.services.FXRouter;

import java.io.File;
import java.io.IOException;

public class FacultyNavbarController {
    @FXML private Circle profileImageCircle;

    @FXML private Label roleLabel;
    @FXML private Label usernameLabel;

    private User user;
    private AnchorPane currentScene;

    @FXML
    private void initialize(){
        user = (FacultyStaff) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
        profileImageCircle.setFill(new ImagePattern(image));

        Platform.runLater(() -> {
            currentScene = (AnchorPane) FXRouter.getStage().getScene().getRoot();
        });
    }


    @FXML
    void onAppealButtonClick(){
        try {
            Animation.getInstance().switchSceneWithFade(currentScene, "faculty-appeal-manage", user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onApproverButtonClick(){
        try {
            Animation.getInstance().switchSceneWithFade(currentScene, "faculty-approver-manage", user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProgramSettingButtonClicked(){
        try {
            Animation.getInstance().switchSceneWithFade(currentScene, "program-setting", user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onLogoutButtonClicked(){
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "login", null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProfileSettingButtonClicked(){
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "profile-setting", user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
