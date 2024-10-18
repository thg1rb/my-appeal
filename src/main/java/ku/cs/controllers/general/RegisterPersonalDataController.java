package ku.cs.controllers.general;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;

import ku.cs.services.Animation;
import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RegisterPersonalDataController {
    @FXML private AnchorPane mainPane;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField idTextField;

    @FXML private Label errorLabel;

    @FXML private Button nextButton;

    @FXML private ImageView backImageView;

    private Datasource<UserList> studentDatasource;
    private UserList studentList;

    @FXML
    public void initialize() {
        studentDatasource = new UserListDatasource("data"+ File.separator+"users", "student.csv");
        studentList = studentDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        if (ProgramSetting.getInstance().getTheme().equals("สว่าง"))
            backImageView.setImage(new Image(getClass().getResource("/icons/back-dark.png").toString()));
        else
            backImageView.setImage(new Image(getClass().getResource("/icons/back-light.png").toString()));

        errorLabel.setText("");

        mainPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                nextButton.fire();
        });
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

    @FXML
    private void onBackButtonClick() {
        Animation.getInstance().switchSceneWithFade(mainPane, "login", null);
    }
}