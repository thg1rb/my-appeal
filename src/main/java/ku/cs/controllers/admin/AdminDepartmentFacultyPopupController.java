package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.ConfirmationDeleteAlertController;
import ku.cs.models.Department;
import ku.cs.models.Faculty;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.DepartmentList;
import ku.cs.services.ProgramSetting;
import ku.cs.services.exceptions.DuplicateItemsException;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdminDepartmentFacultyPopupController {
    @FXML private AnchorPane mainPane;

    @FXML private Text modeText;

    @FXML private Label belongFacultyLabel;
    @FXML private Label departmentIdLabel;
    @FXML private TextField departmentIdTextField;
    @FXML private Label departmentNameLabel;
    @FXML private TextField departmentNameTextField;

    @FXML private Text emptyInputText;

    @FXML private Label facultyNameLabel;
    @FXML private Label facultyIdLabel;
    @FXML private TextField facultyNameTextField;
    @FXML private TextField facultyIdTextField;

    @FXML private ChoiceBox<String> optionChoiceBox;
    @FXML private ChoiceBox<String> facultyChoiceBox;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Object data;
    private FacultyList facultyList;
    private DepartmentList departmentList;

    private String[] optionChoice = {"คณะ", "ภาควิชา"};
    private ArrayList<String> facultyChoice;

    private boolean editMode;
    private boolean deleted;

    @FXML
    private void initialize() {
        ProgramSetting.getInstance().applyStyles(mainPane);

        optionChoiceBox.getItems().addAll(optionChoice);
        emptyInputText.setVisible(false);

        optionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setUi(newValue);
                setMode(editMode, newValue);
            }
        });

        facultyChoiceBox.setOnAction(e->{
            departmentIdTextField.setPromptText(facultyList.findFacultyByName(facultyChoiceBox.getValue()).getFacultyId() + "...");
        });
    }

    public void initPopup(boolean editMode, Object data, FacultyList facultyList, DepartmentList departmentList, String tabSelected){
        setMode(editMode, tabSelected);

        this.facultyList = facultyList;
        this.departmentList = departmentList;
        facultyChoice = facultyList.getAllFacultiesName();

        facultyChoiceBox.getItems().addAll(facultyChoice);
        if (data != null){
            setData(data);
        }
    }

    private void setMode(boolean editMode, String tabSelected) {
        this.editMode = editMode;
        if (editMode){
            modeText.setText("แก้ไขข้อมูล" + tabSelected);
            optionChoiceBox.setDisable(true);
            editButton.setDisable(false);
            confirmButton.setDisable(true);
            deleteButton.setDisable(false);
            deleteButton.setVisible(true);
        }else{
            modeText.setText("เพิ่มข้อมูล" + tabSelected);
            editButton.setDisable(true);
            confirmButton.setDisable(false);
            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
        }
        setUi(tabSelected);
    }

    private void setData(Object data) {
        this.data = data;
        if (data != null){
            if (data instanceof Faculty){
                facultyNameTextField.setText(((Faculty) data).getFacultyName());
                facultyIdTextField.setText(((Faculty) data).getFacultyId());
            }else if (data instanceof Department){
                departmentNameTextField.setText(((Department) data).getDepartmentName());
                departmentIdTextField.setText(((Department) data).getDepartmentId());
                facultyChoiceBox.getSelectionModel().select(facultyList.findFacultyByUUID(((Department) data).getFacultyUUID()).getFacultyName());
            }
        }
    }

    private void setUi(String option){
        optionChoiceBox.setValue(option);
        if (option.equals("คณะ")) {
            belongFacultyLabel.setVisible(false);
            facultyChoiceBox.setVisible(false);
            departmentNameLabel.setVisible(false);
            departmentNameTextField.setVisible(false);
            departmentIdLabel.setVisible(false);
            departmentIdTextField.setVisible(false);

            facultyNameLabel.setVisible(true);
            facultyNameTextField.setVisible(true);
            facultyIdLabel.setVisible(true);
            facultyIdTextField.setVisible(true);
        } else {
            belongFacultyLabel.setVisible(true);
            facultyChoiceBox.setVisible(true);
            departmentNameLabel.setVisible(true);
            departmentNameTextField.setVisible(true);
            departmentIdLabel.setVisible(true);
            departmentIdTextField.setVisible(true);

            facultyNameLabel.setVisible(false);
            facultyNameTextField.setVisible(false);
            facultyIdLabel.setVisible(false);
            facultyIdTextField.setVisible(false);
        }
    }

    @FXML
    public void onConfirmButtonClicked(){
        try{
            String name;
            String id;

            if (optionChoiceBox.getValue().equals("คณะ")){
                name = facultyNameTextField.getText();
                id = facultyIdTextField.getText();

                if (name.isEmpty() || id.isEmpty()){
                    throw new EmptyInputException("*กรุณากรอกข้อมูลให้ครบถ้วน");
                }

                for (String faculty : facultyChoice){
                    if (faculty.equals(name)){
                        throw new DuplicateItemsException("*คณะนี้มีในระบบอยู่แล้ว");
                    }
                }
                facultyList.addFaculty(name, id);

            }else{
                name = departmentNameTextField.getText();
                id = departmentIdTextField.getText();
                UUID faculty = facultyList.findFacultyByName(facultyChoiceBox.getValue()).getUuid();

                if (name.isEmpty() || id.isEmpty() || faculty == null){
                    throw new EmptyInputException("*กรุณากรอกข้อมูลให้ครบถ้วน");
                }

                for (Department department : departmentList.getDepartments()){
                    if (department.getDepartmentName().equals(name)){
                        throw new DuplicateItemsException("*ภาควิชานี้มีในระบบอยู่แล้ว");
                    }
                }
                departmentList.addDepartment(name, faculty, id);
            }

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        }catch (EmptyInputException | DuplicateItemsException e){
            emptyInputText.setText(e.getMessage());
            emptyInputText.setVisible(true);
        }
    }
    @FXML
    public void onCancelButtonClicked(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onEditButtonClicked(){
        try {
            String name;
            String id;

            if (optionChoiceBox.getValue().equals("คณะ")) {
                name = facultyNameTextField.getText();
                id = facultyIdTextField.getText();
                if (name.isEmpty() || id.isEmpty()){
                    throw new EmptyInputException();
                }
                ((Faculty) data).setFacultyName(name);
                ((Faculty) data).setFacultyId(id);
            } else {
                name = departmentNameTextField.getText();
                id = departmentIdTextField.getText();
                UUID faculty = facultyList.findFacultyByName(facultyChoiceBox.getValue()).getUuid();

                if (name.isEmpty() || id.isEmpty() || faculty == null){
                    throw new EmptyInputException();
                }
                ((Department) data).setDepartmentName(name);
                ((Department) data).setDepartmentId(id);
                ((Department) data).setFacultyUUID(faculty);
            }

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.close();
        }catch (EmptyInputException e){
            emptyInputText.setVisible(true);
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

    public boolean isDeleted(){
        return this.deleted;
    }
}
