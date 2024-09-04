package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;

public class ProfileSettingController {
    @FXML private Circle profileImageCircle;

    @FXML private Label fullNameLabel;
    @FXML private Label roleLabel;

    @FXML private PasswordField oldPasswordTextField;
    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField confirmPasswordTextField;

    @FXML private Label oldPasswordErrorLabel;
    @FXML private Label confirmPasswordErrorLabel;
    @FXML private Label confirmEditErrorLabel;
    @FXML private Label successLabel;

    private User user;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        fullNameLabel.setText(user.getFullName());
        roleLabel.setText(user.getRole());

        Image image = new Image(getClass().getResource(user.getImageUrl()).toString());
        profileImageCircle.setFill(new ImagePattern(image));

        clearText();
    }

    @FXML
    private void clearText(){
        oldPasswordErrorLabel.setText("");
        confirmPasswordErrorLabel.setText("");
        confirmEditErrorLabel.setText("");
        successLabel.setText("");
    }

    @FXML
    private void clearTextField(){
        oldPasswordTextField.clear();
        newPasswordTextField.clear();
        confirmPasswordTextField.clear();
    }

    @FXML
    public void onConfirmButtonClicked(){
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        clearText();
        clearTextField();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            confirmEditErrorLabel.setText("โปรดใส่ข้อมูลให้ครบถ้วน");
        } else if (!newPassword.equals(confirmPassword)) {
            confirmPasswordErrorLabel.setText("รหัสผ่านไม่ตรงกัน");
        } else if (!user.validatePassword(oldPassword)) {
            oldPasswordErrorLabel.setText("รหัสผ่านเดิมไม่ถูกต้อง");
        } else {
            user.setPassword(newPassword);
            clearText();
            clearTextField();
            successLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");
        }
    }

    @FXML
    public void onUploadProfileButtonClicked(){

    }
}
