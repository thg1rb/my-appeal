package ku.cs.controllers.general;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.services.ProgramSetting;
import ku.cs.services.ValidationService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;
import ku.cs.services.exceptions.EmptyInputException;
import ku.cs.services.exceptions.IllegalValidationException;

import java.io.File;

public class FirstTimeLoginNewPasswordController {
    @FXML private AnchorPane mainPane;

    @FXML private TextField newPasswordTextField;
    @FXML private TextField confirmNewPasswordTextField;

    @FXML private Label validateLabel;
    @FXML private Label errorLabel;

    private FacultyStaff staff;
    private String currentPassword;

    @FXML
    public void initialize() {
        ProgramSetting.getInstance().applyStyles(mainPane);

        clearText();

        newPasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(currentPassword)) {
                validateLabel.setVisible(true);
                validateLabel.setText("*รหัสผ่านต้องไม่ตรงกับรหัสผ่านเริ่มต้น");
            }else{
                validateLabel.setVisible(false);
            }
        });
    }

    private void clearText(){
        newPasswordTextField.clear();
        confirmNewPasswordTextField.clear();
        validateLabel.setVisible(false);
        errorLabel.setVisible(false);
    }

    public void initPopUp(FacultyStaff staff, String password){
        this.currentPassword = password;
        this.staff = staff;
    }

    @FXML
    private void onConfirmButtonClicked(){
        try {
            String password = newPasswordTextField.getText();

            ValidationService validationService = new ValidationService();

            if (!validationService.validatePassword(password)) {
                throw new IllegalValidationException("รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร และ ไม่เป็นภาษาไทย");
            }
            if (password.isEmpty() || confirmNewPasswordTextField.getText().isEmpty()) {
                throw new EmptyInputException("โปรดกรอกข้อมูลให้ครบถ้วน");
            }

            if (!password.equals(confirmNewPasswordTextField.getText())) {
                errorLabel.setVisible(true);
                errorLabel.setText("กรุณากรอกรหัสผ่านให้ตรงกัน");
            } else {
                savePassword(password);
                closePopup();
            }
        }catch (EmptyInputException e){
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        } catch (IllegalValidationException e) {
            validateLabel.setText(e.getMessage());
            validateLabel.setVisible(true);
        }
    }

    private void savePassword(String password){

        String roleUpdated = staff.getRoleInEnglish();
        Datasource<UserList> userUpdatedDatasource = new UserListDatasource("data" + File.separator + "users", roleUpdated + ".csv");

        UserList userList = userUpdatedDatasource.readData();
        User updateUser = userList.findUserByUUID(staff.getUuid());

        updateUser.setPasswordHash(password);
        staff.setPasswordHash(password);

        userUpdatedDatasource.writeData(userList);
    }

    private void closePopup(){
        Stage stage = (Stage) newPasswordTextField.getScene().getWindow();
        stage.close();
    }
}
