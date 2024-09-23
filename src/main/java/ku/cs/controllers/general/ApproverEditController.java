package ku.cs.controllers.general;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.persons.Approver;
import ku.cs.models.persons.User;
import ku.cs.services.exceptions.EmptyInputException;

public class ApproverEditController {
    @FXML private TextField nameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField subRoleTextField;
    @FXML private Button confirmButton;
    @FXML private Label topicLabel;
    @FXML private Label nameErrorLabel;
    @FXML private Label lastNameErrorLabel;
    @FXML private Label roleErrorLabel;
    @FXML private Label subRoleErrorLabel;
    @FXML private ComboBox<String> roleComboBox;

    private Stage popupStage;
    private Approver approver;
    private User user;
    private ApproverList approverList;
    private String role;
    private String selectedRole;

    public void initialize() {
        roleComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                selectedRole = newValue;
                if(selectedRole.equals("รองคณะบดี")) {
                    subRoleTextField.setDisable(true);
                }
            }
        });
    }

    public void setStage(Stage stage) {
        this.popupStage = stage;
    }

    public void setApprover(Approver approver) {
        this.approver = approver;
        nameTextField.setText(approver.getFirstName());
        lastNameTextField.setText(approver.getLastName());
    }

    public void setRole(User user) {
        role = user.getRole();
        if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            roleComboBox.getItems().addAll("หัวหน้าภาควิชา", "รองหัวหน้าภาควิชา", "รักษาการณ์แทนหัวหน้าภาควิชา");
        }
        else if (role.equals("เจ้าหน้าท่ี่คณะ")) {
            roleComboBox.getItems().addAll("คณบดี", "รองคณบดี", "รักษาการณ์แทนคณบดี");
        }
    }

    public void setMode(boolean addMode, Approver approver, User user, ApproverList approvers) {
        if(addMode){
            confirmButton.setVisible(false);
            topicLabel.setText("เพิ่มผู้อนุมัติ");
            this.user = user;
            this.approverList = approvers;
        }
        else{
            confirmButton.setVisible(true);
            topicLabel.setText("แก้ไขผู้อนุมัติ");
            setApprover(approver);
        }
    }

    public void onConfirmButtonClick() {
        try{
            if(isEmptyTextField()){
                throw new EmptyInputException();
            }
            approver.setFirstName(nameTextField.getText());
            approver.setLastName(lastNameTextField.getText());
//            approver.setRole(roleTextField.getText());
            onCancleButtonClick();
        } catch(EmptyInputException e){
            System.out.println(e.getMessage());
        }
    }

    public void onCancleButtonClick(){
        if(popupStage!=null){
            popupStage.close();
        }
    }

    public void onAddButtonClick(){
        try{
            if(isEmptyTextField()){
                throw new EmptyInputException();
            }
            approverList.addApprover(nameTextField.getText(),lastNameTextField.getText(),selectedRole,user);
            onCancleButtonClick();
        } catch(EmptyInputException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isEmptyTextField(){
        nameErrorLabel.setVisible(false);
        lastNameErrorLabel.setVisible(false);
        roleErrorLabel.setVisible(false);
        if(!nameTextField.getText().isEmpty() && !lastNameTextField.getText().isEmpty() && selectedRole != null){
            if(subRoleTextField.isVisible() && subRoleTextField.getText().isEmpty()){
                subRoleErrorLabel.setVisible(true);
                return true;
            }
            return false;
        }
        else{
            if(nameTextField.getText().isEmpty()){
                nameErrorLabel.setVisible(true);
            }
            if(lastNameTextField.getText().isEmpty()){
                lastNameErrorLabel.setVisible(true);
            }
            if(selectedRole == null){
                roleErrorLabel.setVisible(true);
            }
            return true;
        }
    }


}
