package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class LoginController {

    @FXML private TextField giveUsernameTextField;
    @FXML private TextField givePasswordTextField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    // ไปที่หน้าประจำของแต่ละตำแหน่ง
    @FXML
    public void onLoginButtonClick() {
        String username = giveUsernameTextField.getText();
        String password = givePasswordTextField.getText();

        // Admin
        if (username.equals("admin") && password.equals("admin")) {
            try {
                FXRouter.goTo("admin-dashboard");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Faculty
        else if (username.equals("faculty") && password.equals("faculty")) {
            try {
                FXRouter.goTo("faculty-appeal-manage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Major
        else if (username.equals("major") && password.equals("major")) {
            try {
                FXRouter.goTo("major-appeal-manage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Professor
        else if (username.equals("professor") && password.equals("professor")) {
            try {
                FXRouter.goTo("professor-student-appeal");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Student
        else if (username.equals("student") && password.equals("student")) {
            try {
                FXRouter.goTo("student-track-appeal");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Username or Password doesn't exist in the data
        else {
            errorLabel.setText("*ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
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
