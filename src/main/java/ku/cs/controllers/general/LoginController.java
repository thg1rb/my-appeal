package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.models.collections.UserList;

import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;
import java.io.IOException;

public class LoginController {
    @FXML AnchorPane mainPane;

    @FXML private TextField giveUsernameTextField;
    @FXML private TextField givePasswordTextField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private UserList userList;
    private User user;

    @FXML
    public void initialize() {
        userList = UserListDatasource.readAllUsers().getActiveUser();

        errorLabel.setText("");

        giveUsernameTextField.setOnKeyPressed(this::handleKeyPressed);
        givePasswordTextField.setOnKeyPressed(this::handleKeyPressed);
    }

    // กดปุ่ม Enter บนคีย์บอร์ดเพื่อเข้าสู่ระบบ
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    // ไปที่หน้าประจำของแต่ละตำแหน่ง
    @FXML
    public void onLoginButtonClick() {
        String username = giveUsernameTextField.getText();
        String password = givePasswordTextField.getText();
        errorLabel.setText("");
        user = userList.findUserByUsername(username);
        errorLabel.setText("");
        if(user == null) {
            errorLabel.setText("ชื่อผู้ใช้งานไม่ถูกต้อง");
        }
        else{
            if (user.validatePassword(password)){
                if (user.hasAccessibility()) {
                    switch (user.getRole()){
                        case "ผู้ดูแลระบบ":
                            updateLoginTime(user);
                            try {
                                FXRouter.goTo("admin-dashboard", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "เจ้าหน้าที่คณะ":
                            checkIfFirstTimeLoginForStaff(user, password);
                            updateLoginTime(user);
                            try {
                                FXRouter.goTo("faculty-appeal-manage", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "เจ้าหน้าที่ภาควิชา":
                            checkIfFirstTimeLoginForStaff(user, password);
                            updateLoginTime(user);
                            try {
                                FXRouter.goTo("major-appeal-manage", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "อาจารย์ที่ปรึกษา":
                            checkIfFirstTimeLoginForStaff(user, password);
                            updateLoginTime(user);
                            try {
                                FXRouter.goTo("professor-student-list", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            updateLoginTime(user);
                            try {
                                FXRouter.goTo("student-track-appeal", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                    }
                }else{
                    errorLabel.setText("บัญชีของท่านถูกระงับการใช้งาน");
                }
            }else{
                errorLabel.setText("รหัสผ่านไม่ถูกต้อง");
            }
        }
    }

    private void updateLoginTime(User user){
        String roleUpdated = user.getRoleInEnglish();
        Datasource<UserList> userUpdatedDatasource = new UserListDatasource("data" + File.separator + "users", roleUpdated + ".csv");

        UserList userList = userUpdatedDatasource.readData();
        User updateUser = userList.findUserByUUID(user.getUuid());

        user.setLoginDate(DateTimeService.updateTime());
        updateUser.setLoginDate(DateTimeService.updateTime());
        userUpdatedDatasource.writeData(userList);
    }

    private void checkIfFirstTimeLoginForStaff(User user, String currentPassword){
        if (user instanceof FacultyStaff staff){
            if (staff.getInitialPasswordText().equals(currentPassword)){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/set-password.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    GaussianBlur blur = new GaussianBlur(10);

                    FirstTimeLoginNewPasswordController controller = fxmlLoader.getController();
                    controller.initPopUp(staff, currentPassword);

                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    mainPane.setEffect(blur);
                    stage.showAndWait();
                    mainPane.setEffect(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // ไปที่หน้าลงทะเบียน (ข้อมูลส่วนบุคคล)
    @FXML
    public void onRegisterButtonClick() {
        try {
            FXRouter.goTo("register-personal-data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
