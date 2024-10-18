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
import ku.cs.services.ValidationService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;
import ku.cs.services.exceptions.DuplicateItemsException;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RegisterUsernamePasswordController {
    @FXML private AnchorPane mainPane;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;

    @FXML private Label errorLabel;

    @FXML private Button confirmButton;

    @FXML private ImageView backImageView;

    private Datasource<UserList> studentDatasource;
    private HashMap<String, Object> data;
    private User student;
    private UserList studentList;

    @FXML
    private void initialize() {
        studentDatasource = new UserListDatasource("data"+ File.separator+"users", "student.csv");
        data = FXRouter.getData() instanceof HashMap<?, ?> ? (HashMap<String, Object>) FXRouter.getData() : null;
        studentList = (UserList) data.get("studentsList");
        student = (User) data.get("studentRegistering");

        ProgramSetting.getInstance().applyStyles(mainPane);

        if (ProgramSetting.getInstance().getTheme().equals("สว่าง"))
            backImageView.setImage(new Image(getClass().getResource("/icons/back-dark.png").toString()));
        else
            backImageView.setImage(new Image(getClass().getResource("/icons/back-light.png").toString()));

        errorLabel.setText("");

        mainPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                confirmButton.fire();
        });
    }

    @FXML
    private void onConfirmButtonClicked() {
        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();
        String confirmPassword = this.confirmPasswordTextField.getText();

        try{
            ValidationService validationService = new ValidationService();
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                throw new EmptyInputException("กรุณาใส่ข้อมูลให้ครบถ้วน");
            }else if (studentList.findUserByUsername(username) != null){
                throw new DuplicateItemsException("ชื่อผู้ใช้งานระบบนี้ถูกใช้ไปแล้ว กรุณาใช้ชื่ออื่น");
            }else if (!validationService.validateUsername(username)){
                errorLabel.setText("ชื่อผู้ใช้งานต้องมีความยาวอย่างน้อย 6 ตัวอักษร และ ไม่เป็นภาษาไทย");
            } else if (!validationService.validatePassword(password)){
                errorLabel.setText("รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร และ ไม่เป็นภาษาไทย");
            }
            else {
                if (password.equals(confirmPassword)) {
                    ((Student) student).registration(username, password);
                    studentDatasource.writeData(studentList);
                    try {
                        FXRouter.goTo("login");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    errorLabel.setText("กรุณาใส่รหัสผ่านให้ตรงกัน");
                }
            }
        } catch (EmptyInputException | DuplicateItemsException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onBackButtonClick() {
        Animation.getInstance().switchSceneWithFade(mainPane, "register-personal-data", null);
    }
}
