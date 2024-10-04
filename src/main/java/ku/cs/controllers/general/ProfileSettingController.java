package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javafx.stage.Stage;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;
import ku.cs.services.ValidationService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;
import ku.cs.services.exceptions.EmptyInputException;
import ku.cs.services.exceptions.IllegalValidationException;
import ku.cs.services.fileuploaders.ImageFileUploader;

import java.io.File;

public class ProfileSettingController {
    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private Circle profileImageCircle;

    @FXML private Label fullNameLabel;
    @FXML private Label roleLabel;

    @FXML private PasswordField oldPasswordTextField;
    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField confirmPasswordTextField;

    @FXML private Label oldPasswordErrorLabel;
    @FXML private Label confirmPasswordErrorLabel;
    @FXML private Label confirmEditErrorLabel;
    @FXML private Label successLabel;
    @FXML private Label validationErrorLabel;

    private User user;

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
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        fullNameLabel.setText(user.getFullName());
        roleLabel.setText(user.getRole());

        Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
        profileImageCircle.setFill(new ImagePattern(image));

        clearText();
    }

    private void changeProfileImage(){
        String roleUpdated = user.getRoleInEnglish();
        Datasource<UserList> userUpdatedDatasource = new UserListDatasource("data" + File.separator + "users", roleUpdated + ".csv");

        UserList userList = userUpdatedDatasource.readData();
        User updateUser = userList.findUserByUUID(user.getUuid());

        updateUser.setProfile(user.getProfileUrl());
        userUpdatedDatasource.writeData(userList);
    }

    private void changePassword(User user, String password){
        String roleUpdated = user.getRoleInEnglish();
        Datasource<UserList> userUpdatedDatasource = new UserListDatasource("data" + File.separator + "users", roleUpdated + ".csv");

        UserList userList = userUpdatedDatasource.readData();
        User updateUser = userList.findUserByUUID(user.getUuid());

        updateUser.setPasswordHash(password);
        userUpdatedDatasource.writeData(userList);
    }

    @FXML
    private void clearText(){
        oldPasswordErrorLabel.setText("");
        confirmPasswordErrorLabel.setText("");
        confirmEditErrorLabel.setText("");
        successLabel.setText("");
        validationErrorLabel.setText("");
    }

    @FXML
    private void clearTextField(){
        oldPasswordTextField.clear();
        newPasswordTextField.clear();
        confirmPasswordTextField.clear();
    }

    @FXML
    public void onConfirmButtonClicked(){
        try{
            ValidationService validationService = new ValidationService();

            String oldPassword = oldPasswordTextField.getText();
            String newPassword = newPasswordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            clearText();
            clearTextField();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                throw new EmptyInputException("โปรดใส่ข้อมูลให้ครบถ้วน");
            } else if (!validationService.validatePassword(newPassword)) {
                throw new IllegalValidationException("รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร และ ไม่เป็นภาษาไทย");
            } else if (!newPassword.equals(confirmPassword)) {
                confirmPasswordErrorLabel.setText("รหัสผ่านไม่ตรงกัน");
            } else if (!user.validatePassword(oldPassword)) {
                oldPasswordErrorLabel.setText("รหัสผ่านเดิมไม่ถูกต้อง");
            } else {
                changePassword(user, newPassword);

                clearText();
                clearTextField();

                successLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");
            }
        }catch (EmptyInputException e){
            confirmEditErrorLabel.setText(e.getMessage());
        } catch (IllegalValidationException e){
            validationErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onUploadProfileButtonClicked(){
        ImageFileUploader imageFileUploader = new ImageFileUploader(profileImageCircle, user, "data" + File.separator + "profile-images");
        imageFileUploader.upload((Stage) profileImageCircle.getScene().getWindow());

        changeProfileImage();

        //refresh Navbar
        navbarAnchorPane.getChildren().clear();
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
