package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;
import ku.cs.services.Animation;
import ku.cs.services.FXRouter;

import java.io.File;
import java.io.IOException;

public class AdminNavbarController {
    @FXML private Circle profileImageCircle;

    @FXML private Label roleLabel;
    @FXML private Label usernameLabel;

    private User user;
    private AdminDashboardController adminDashboardController;
    private AnchorPane currentScene;

    @FXML
    private void initialize(){
        user = (AdminUser) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
        profileImageCircle.setFill(new ImagePattern(image));

        Platform.runLater(() -> {
            currentScene = (AnchorPane) FXRouter.getStage().getScene().getRoot();
        });
    }

    public void setAdminDashboardController(AdminDashboardController adminDashboardController) {
        this.adminDashboardController = adminDashboardController;
    }

    @FXML
    void onUserButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "admin-user-manage", user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onStaffButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "admin-staff-manage", user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onFacultyButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "admin-faculty-manage", user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDashboardButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "admin-dashboard", user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProfileSettingButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "profile-setting", user);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onProgramSettingButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "program-setting", user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onLogoutButtonClicked() {
        if (adminDashboardController != null) {
            adminDashboardController.stopWatchingFilesThread();
        }
        try{
            Animation.getInstance().switchSceneWithFade(currentScene, "login", null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
