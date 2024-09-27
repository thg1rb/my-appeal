package ku.cs.controllers.general;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RegisterPersonalDataController {
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField idTextField;

    @FXML private Label errorLabel;

    private Datasource<UserList> studentDatasource;
    private UserList studentList;

    @FXML
    public void initialize() {
        studentDatasource = new UserListDatasource("data"+ File.separator+"users", "student.csv");
        studentList = studentDatasource.readData();

        errorLabel.setText("");
    }

    // ไปที่หน้าลงทะเบียนถัดไป
    @FXML
    public void onNextButtonClick() {
        try{
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String id = idTextField.getText();
            User exist = studentList.findStudentUserByInformation(firstName, lastName, id, email);
            if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || id.isEmpty()){
                throw new EmptyInputException();
            }
            if (exist != null) {
                if (!((Student) exist).isRegistered()) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("studentsList", studentList);
                    data.put("studentRegistering", exist);
                    try {
                        FXRouter.goTo("register-username-password", data);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    errorLabel.setText("*ท่านได้ทำการลงทะเบียนไปแล้ว");
                }
            } else {
                errorLabel.setText("*ข้อมูลของท่านไม่อยู่ในระบบ กรุณาติดต่อเจ้าหน้าที่");
            }
        }catch (EmptyInputException e){
            errorLabel.setText("*กรุณกรอกข้อมูลให้ครบถ้วน");
        }
    }

}