package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import ku.cs.models.person.User;
import ku.cs.models.collection.UserList;

import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;

import java.io.IOException;

public class LoginController {

    @FXML private TextField giveUsernameTextField;
    @FXML private TextField givePasswordTextField;
    @FXML private Label errorLabel;

    private Datasource<UserList> userListDatasource;
    private UserList userList;
    private User user;

    @FXML
    public void initialize() {
        userList = UserListDatasource.readAllUsers();

        errorLabel.setText("");
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
                            try {
                                FXRouter.goTo("admin-dashboard", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "เจ้าหน้าที่คณะ":
                            try {
                                FXRouter.goTo("faculty-appeal-manage", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "เจ้าหน้าที่ภาควิชา":
                            try {
                                FXRouter.goTo("major-appeal-manage", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "อาจารย์ที่ปรึกษา":
                            try {
                                FXRouter.goTo("professor-student-list", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
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

    // ไปที่หน้าลงทะเบียน (ข้อมูลส่วนบุคคล)
    @FXML
    public void onRegisterButtonClick() {
        try {
            FXRouter.goTo("register-personal-data", userList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
