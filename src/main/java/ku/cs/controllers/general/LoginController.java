package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.models.persons.User;
import ku.cs.models.collections.UserList;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserListHardCodeDatasource;

import java.io.IOException;

public class LoginController {

    @FXML private TextField giveUsernameTextField;
    @FXML private TextField givePasswordTextField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private Datasource<UserList> userListDatasource;
    private UserList userList;
    private User user;

    @FXML
    public void initialize() {
//        userListDatasource = new UserListFileDatasource("data", "user.csv");
        userListDatasource = FXRouter.getData() == null ? new UserListFileDatasource("data", "user.csv") : (UserListFileDatasource) FXRouter.getData();
        userList = userListDatasource.readData();
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
            if (!user.isBan()){
                if (user.validatePassword(password)) {
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
                    errorLabel.setText("รหัสผ่านไม่ถูกต้อง");
                }
            }else{
                errorLabel.setText("บัญชีของท่านถูกระงับการใช้งาน");
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
