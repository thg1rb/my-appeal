package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import ku.cs.models.collections.StudentList;
import ku.cs.models.collections.UserList;

import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentRosterListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class RegisterUsernamePasswordController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;

    @FXML private Label errorLabel;

    private Datasource<UserList> datasource;
    private UserList userList;

    private StudentList studentList;

    private Student studentInRoster;

    @FXML
    private void initialize() {
        studentInRoster = (Student) FXRouter.getData();

        datasource = new UserListFileDatasource("data", "user.csv");

        errorLabel.setText("");
    }

    @FXML
    private void onConfirmButtonClicked() {
        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();
        String confirmPassword = this.confirmPasswordTextField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("กรุณาใส่ข้อมูลให้ครบถ้วน");
        }else {
            if (password.equals(confirmPassword)) {
                ((UserListFileDatasource) datasource).addNewUser(new User(username, password, studentInRoster));
                try{
                    FXRouter.goTo("login", datasource);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }else {
                errorLabel.setText("กรุณาใส่รหัสผ่านให้ตรงกัน");
            }
        }
    }
}
