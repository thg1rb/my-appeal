package ku.cs.controllers.general;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import ku.cs.models.collections.StudentList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Student;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentRosterListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class RegisterPersonalDataController {
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField idTextField;

    @FXML private Label errorLabel;

    private Datasource<UserList> userDatasource;
    private UserList userList;

    private Datasource<StudentList> studentListDatasource;
    private StudentList studentList;

    @FXML
    public void initialize() {
        userDatasource = new UserListFileDatasource("data", "user.csv");
        userList = userDatasource.readData();

        studentListDatasource = new StudentRosterListFileDatasource("data", "student-roster.csv");
        studentList = studentListDatasource.readData();

        errorLabel.setText("");
    }

    // ไปที่หน้าลงทะเบียนถัดไป
    @FXML
    public void onNextButtonClick() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String id = idTextField.getText();

        Student exist = studentList.findStudentByInformation(firstName, lastName, id, email);
        if (exist != null) {
            if (userList.findUserByPersonalInformation(firstName, lastName, id,email) == null){
                try {
                    FXRouter.goTo("register-username-password", exist);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                errorLabel.setText("ท่านได้ทำการลงทะเบียนไปแล้ว");
            }
        }else {
            errorLabel.setText("*ข้อมูลของท่านไม่อยู่ในระบบ กรุณาติดต่อเจ้าหน้าที่");
        }
    }

}