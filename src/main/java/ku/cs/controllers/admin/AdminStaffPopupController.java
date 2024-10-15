package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ku.cs.controllers.general.ConfirmationDeleteAlertController;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.DepartmentList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Advisor;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.services.ProgramSetting;
import ku.cs.services.ValidationService;
import ku.cs.services.exceptions.EmptyInputException;
import ku.cs.services.exceptions.IllegalValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AdminStaffPopupController {
    @FXML private AnchorPane mainPane;

    @FXML private Text optionText;
    @FXML private Label departmentLabel;
    @FXML private Label idLabel;
    @FXML private Text emptyInputText;
    @FXML private Text usernameValidationText;
    @FXML private Text passwordValidationText;

    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private ChoiceBox<String> facultyChoiceBox;
    @FXML private ChoiceBox<String> departmentChoiceBox;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField initPasswordTextField;
    @FXML private TextField idTextField;

    private HashMap<String, UserList> staffMap;
    private User user;

    private final String[] staffChoice = {"เจ้าหน้าที่คณะ", "เจ้าหน้าที่ภาควิชา", "อาจารย์ที่ปรึกษา"};

    private FacultyList facultyList;
    private DepartmentList departmentList;
    private ArrayList<String> departmentChoices;

    private String selectedRole;
    private boolean deleted;

    @FXML
    private void initialize() {
        ProgramSetting.getInstance().applyStyles(mainPane);

        roleChoiceBox.getItems().addAll(staffChoice);

        emptyInputText.setVisible(false);
        usernameValidationText.setVisible(false);
        passwordValidationText.setVisible(false);
        this.deleted = false;

        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.equals("เจ้าหน้าที่คณะ")){
                    selectedRole = newValue;
                    departmentLabel.setVisible(false);
                    idLabel.setVisible(false);
                    departmentChoiceBox.setVisible(false);
                    idTextField.setVisible(false);
                }else if (newValue.equals("เจ้าหน้าที่ภาควิชา")){
                    selectedRole = newValue;
                    departmentLabel.setVisible(true);
                    idLabel.setVisible(false);
                    departmentChoiceBox.setVisible(true);
                    idTextField.setVisible(false);
                }else{
                    selectedRole = newValue;
                    departmentLabel.setVisible(true);
                    idLabel.setVisible(true);
                    departmentChoiceBox.setVisible(true);
                    idTextField.setVisible(true);
                }
            }
        });

        facultyChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue != null){
                    UUID facultyUUID = facultyList.findFacultyByName(newValue).getUuid();
                    updateDepartmentChoiceBox(facultyUUID);
                }
            }
        });
    }

    public void initPopup(boolean editMode, User user, FacultyList facultyList, DepartmentList departmentList, String selectedRole, HashMap<String, UserList> staffMap) {
        this.staffMap = staffMap;

        this.selectedRole = selectedRole;
        roleChoiceBox.getSelectionModel().select(selectedRole);

        this.facultyList = facultyList;
        this.departmentList = departmentList;
        facultyChoiceBox.getItems().addAll(facultyList.getAllFacultiesName());

        if (editMode){
            updateDepartmentChoiceBox(((FacultyStaff)user).getFacultyUUID());
            setPerson(user);
        }
        setMode(editMode);
    }

    private void setMode(boolean editMode) {
        if (editMode){
            optionText.setText("แก้ไขข้อมูลเจ้าหน้าที่");
            editButton.setVisible(true);
            confirmButton.setVisible(false);
            confirmButton.setDisable(true);
            editButton.setDisable(false);
            roleChoiceBox.setDisable(true);
            deleteButton.setVisible(true);
            deleteButton.setDisable(false);
        }else{
            optionText.setText("เพิ่มข้อมูลเจ้าหน้าที่");
            editButton.setVisible(false);
            confirmButton.setVisible(true);
            confirmButton.setDisable(false);
            editButton.setDisable(true);
            roleChoiceBox.setDisable(false);
            deleteButton.setVisible(false);
            deleteButton.setDisable(true);
        }
    }

    private void setPerson(User user) {
        this.user = user;
        if (user != null) {
            firstNameTextField.setText(user.getFirstName());
            lastNameTextField.setText(user.getLastName());
            usernameTextField.setText(user.getUsername());
            initPasswordTextField.setText(((FacultyStaff) user).getInitialPasswordText());
            facultyChoiceBox.setValue(facultyList.findFacultyByUUID(((FacultyStaff)user).getFacultyUUID()).getFacultyName());
            if (user instanceof DepartmentStaff) departmentChoiceBox.setValue(departmentList.findDepartmentByUUID(((DepartmentStaff)user).getDepartmentUUID()).getDepartmentName());
            if (user instanceof Advisor) idTextField.setText(((Advisor)user).getAdvisorId());
        }
    }

    private void updateDepartmentChoiceBox(UUID faculty){
        this.departmentChoices = departmentList.getDepartmentsNameByFaculty(faculty);
        departmentChoiceBox.getItems().clear();
        departmentChoiceBox.getItems().addAll(departmentChoices);
    }

    @FXML
    public void onCancelButtonClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onConfirmButtonClicked() {
        usernameValidationText.setVisible(false);
        passwordValidationText.setVisible(false);

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String password = initPasswordTextField.getText();
        UUID faculty = facultyList.findFacultyByName(facultyChoiceBox.getValue()).getUuid();
        UUID department = null;
        String id = "";

        try {

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || faculty == null) {
                throw new EmptyInputException("กรุณากรอกข้อมูลให้ครบถ้วน");
            }
            
            if (!validateUsername(username) || !validatePassword(password)){
                throw new IllegalValidationException();
            }

            if (departmentChoiceBox.isVisible()) {
                department = departmentList.findDepartmentByName(departmentChoiceBox.getValue()).getUuid();
                if (department == null) {
                    throw new EmptyInputException("กรุณากรอกข้อมูลให้ครบถ้วน");
                }
            }

            if (idTextField.isVisible()) {
                id = idTextField.getText();
                if (id.isEmpty()) {
                    throw new EmptyInputException("กรุณากรอกข้อมูลให้ครบถ้วน");
                }
            }

            switch (selectedRole) {
                case "เจ้าหน้าที่คณะ":
                    staffMap.get(selectedRole).addUser(new FacultyStaff(selectedRole, username, password, firstName, lastName, faculty));
                    break;
                case "เจ้าหน้าที่ภาควิชา":
                    staffMap.get(selectedRole).addUser(new DepartmentStaff(selectedRole, username, password, firstName, lastName, faculty, department));
                    break;
                case "อาจารย์ที่ปรึกษา":
                    staffMap.get(selectedRole).addUser(new Advisor(selectedRole, username, password, firstName, lastName, faculty, department, id));
                    break;
            }

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (EmptyInputException e) {
            emptyInputText.setText(e.getMessage());
            emptyInputText.setVisible(true);
        } catch (IllegalValidationException e) {
            if (!validateUsername(username))
                usernameValidationText.setVisible(true);
            if (!validatePassword(password))
                passwordValidationText.setVisible(true);
        }
    }

    @FXML
    public void onEditButtonClicked() {
        usernameValidationText.setVisible(false);
        passwordValidationText.setVisible(false);

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String password = initPasswordTextField.getText();
        UUID faculty = facultyList.findFacultyByName(facultyChoiceBox.getValue()).getUuid();
        UUID department = null;
        String id = "";
        try {
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || faculty == null) {
                throw new EmptyInputException();
            }
            if (!validateUsername(username) || !validatePassword(password)){
                throw new IllegalValidationException();
            }

            if (departmentChoiceBox.isVisible()) {
                department = departmentList.findDepartmentByName(departmentChoiceBox.getValue()).getUuid();
                if (department == null) {
                    throw new EmptyInputException();
                }
            }

            if (idTextField.isVisible()) {
                id = idTextField.getText();
                if (id.isEmpty()) {
                    throw new EmptyInputException();
                }
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            ((FacultyStaff)user).setInitialPassword(password);
            ((FacultyStaff)user).setFacultyUUID(faculty);

            if (user instanceof DepartmentStaff){
                ((DepartmentStaff) user).setDepartmentUUID(department);
            }
            if (user instanceof Advisor){
                ((Advisor) user).setAdvisorId(id);
            }
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.close();
        } catch (EmptyInputException e){
            emptyInputText.setVisible(true);
        } catch (IllegalValidationException e) {
            if (!validateUsername(username))
                usernameValidationText.setVisible(true);
            if (!validatePassword(password))
                passwordValidationText.setVisible(true);
        }
    }

    @FXML
    private void onDeleteButtonClicked() {
        try {
            FXMLLoader alertLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/confirmation-delete.fxml"));
            Parent root = alertLoader.load();
            ConfirmationDeleteAlertController controller = alertLoader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("ยืนยันการลบ");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.showAndWait();
            if (controller.isConfirmed()){
                user = null;
                Stage mainstage = (Stage) deleteButton.getScene().getWindow();
                mainstage.close();
                this.deleted = true;
            }else {
                this.deleted = false;
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validatePassword(String password) {
        ValidationService validationService = new ValidationService();
        return validationService.validatePassword(password);
    }

    private boolean validateUsername(String username) {
        ValidationService validationService = new ValidationService();
        return validationService.validateUsername(username);
    }

    public boolean isDeleted(){
        return this.deleted;
    }
}
