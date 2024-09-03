package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;
import ku.cs.models.persons.User;

import java.util.ArrayList;

public class StaffPopupController {
    @FXML private Text optionText;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button editButton;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField initPasswordTextField;
    @FXML private ChoiceBox<String> facultyChoiceBox;
    @FXML private ChoiceBox<String> majorChoiceBox;
    @FXML private TextField idTextField;

    @FXML private Text majorText;
    @FXML private Text idText;

    private User user;
    private AdminStaffManagementController mainController;
    private final String[] staffChoice = {"เจ้าหน้าที่คณะ", "เจ้าหน้าที่ภาควิชา", "อาจารย์ที่ปรึกษา"};
    private final ArrayList<String> facultyChoice = FacultyList.getAllFacultiesName();
    private ArrayList<String> majorChoice;

    private String selectedRole;

    @FXML private void initialize() {
        roleChoiceBox.getItems().addAll(staffChoice);
        facultyChoiceBox.getItems().addAll(facultyChoice);

        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.equals("เจ้าหน้าที่คณะ")){
                    selectedRole = newValue;
                    majorText.setVisible(false);
                    idText.setVisible(false);
                    majorChoiceBox.setVisible(false);
                    idTextField.setVisible(false);
                }else if (newValue.equals("เจ้าหน้าที่ภาควิชา")){
                    selectedRole = newValue;
                    majorText.setVisible(true);
                    idText.setVisible(false);
                    majorChoiceBox.setVisible(true);
                    idTextField.setVisible(false);
                }else{
                    selectedRole = newValue;
                    majorText.setVisible(true);
                    idText.setVisible(true);
                    majorChoiceBox.setVisible(true);
                    idTextField.setVisible(true);
                }
            }
        });

        facultyChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue != null){
                    majorChoice = MajorList.findMajorsByFaculty(newValue);
                    majorChoiceBox.getItems().clear();
                    majorChoiceBox.getItems().addAll(majorChoice);
                }
            }
        });
    }

    public void setMainController(AdminStaffManagementController mainController) {
        this.mainController = mainController;
        if (mainController.getPopupEditMode()){
            optionText.setText("แก้ไขข้อมูลเจ้าหน้าที่");
            editButton.setVisible(true);
            confirmButton.setVisible(false);
            confirmButton.setDisable(true);
            editButton.setDisable(false);
            roleChoiceBox.setDisable(true);
        }else{
            optionText.setText("เพิ่มข้อมูลเจ้าหน้าที่");
            editButton.setVisible(false);
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            editButton.setDisable(true);
            roleChoiceBox.setDisable(false);
        }
    }
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
        roleChoiceBox.getSelectionModel().select(selectedRole);
    }
    public void setPerson(User user) {
        this.user = user;
        if (user != null) {
            firstNameTextField.setText(user.getFirstName());
            lastNameTextField.setText(user.getLastName());
            usernameTextField.setText(user.getUsername());
            initPasswordTextField.setText(user.getInitialPasswordText());
            facultyChoiceBox.setValue(user.getFaculty());
            majorChoiceBox.setValue(user.getMajor());
            idTextField.setText(user.getId());
        }
    }

    @FXML
    public void onCancelButtonClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onConfirmButtonClicked(){
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String faculty = facultyChoiceBox.getValue();
        String password = initPasswordTextField.getText();
        String major = majorChoiceBox.getValue();
        String id = idTextField.getText();

        mainController.addStaff(new User(selectedRole, username, password, firstName, lastName, faculty, major, id));

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onEditButtonClicked() {
        user.setFirstName(firstNameTextField.getText());
        user.setLastName(lastNameTextField.getText());
        user.setUsername(usernameTextField.getText());
        user.setInitialPasswordText(initPasswordTextField.getText());
        user.setFaculty(facultyChoiceBox.getValue());
        user.setMajor(majorChoiceBox.getValue());
        user.setId(idTextField.getText());
        user.setFullName();

        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}
