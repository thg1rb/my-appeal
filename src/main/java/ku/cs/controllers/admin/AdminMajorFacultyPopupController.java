package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.ConfirmationDeleteAlertController;
import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;
import ku.cs.services.exceptions.DuplicateItemsException;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.IOException;
import java.util.ArrayList;

public class AdminMajorFacultyPopupController {
    @FXML private Text modeText;
    @FXML private Text nameText;
    @FXML private Text idText;
    @FXML private Text belongFacultyText;
    @FXML private Text emptyInputText;

    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;

    @FXML private ChoiceBox<String> optionChoiceBox;
    @FXML private ChoiceBox<String> facultyChoiceBox;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Object data;
    private FacultyList facultyList;
    private MajorList majorList;

    private String[] optionChoice = {"คณะ", "ภาควิชา"};
    private ArrayList<String> facultyChoice;

    private boolean editMode;
    private boolean deleted;

    @FXML
    private void initialize() {
        optionChoiceBox.getItems().addAll(optionChoice);
        emptyInputText.setVisible(false);

        optionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setUi(newValue);
                setMode(editMode, newValue);
            }
        });
    }

    public void initPopup(boolean editMode, Object data, FacultyList facultyList, MajorList majorList, String tabSelected){
        setMode(editMode, tabSelected);

        this.facultyList = facultyList;
        this.majorList = majorList;
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
                nameTextField.setText(((Faculty) data).getFacultyName());
                idTextField.setText(((Faculty) data).getFacultyId());
            }else if (data instanceof Major){
                nameTextField.setText(((Major) data).getMajorName());
                idTextField.setText(((Major) data).getMajorId());
                facultyChoiceBox.getSelectionModel().select(((Major) data).getFaculty());
            }
        }
    }

    private void setUi(String option){
        optionChoiceBox.setValue(option);
        if (option.equals("คณะ")) {
            belongFacultyText.setVisible(false);
            facultyChoiceBox.setVisible(false);
            nameText.setText("ชื่อคณะ :");
            idText.setText("รหัสคณะ :");
        }else{
            belongFacultyText.setVisible(true);
            facultyChoiceBox.setVisible(true);
            nameText.setText("ชื่อภาควิชา :");
            idText.setText("รหัสภาควิชา :");
        }
    }

    @FXML
    public void onConfirmButtonClicked(){
        try{
            String name = nameTextField.getText();
            String id = idTextField.getText();
            if (name.isEmpty() || id.isEmpty()){
                throw new EmptyInputException();
            }

            if (optionChoiceBox.getValue().equals("คณะ")) {
                for (String faculty : facultyChoice){
                    if (faculty.equals(name)){
                        throw new DuplicateItemsException("*คณะนี้มีในระบบอยู่แล้ว");
                    }
                }
                facultyList.addFaculty(name, id);
            } else {
                for (Major major : majorList.getMajors()){
                    if (major.getMajorName().equals(name)){
                        throw new DuplicateItemsException("*ภาควิชานี้มีในระบบอยู่แล้ว");
                    }
                }
                String faculty = facultyChoiceBox.getValue();
                majorList.addMajor(name, faculty, id, facultyList);
            }

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        }catch (EmptyInputException e){
            emptyInputText.setText("*กรุณากรอกข้อมูลให้ครบถ้วน");
            emptyInputText.setVisible(true);
        }catch (DuplicateItemsException e){
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
            String name = nameTextField.getText();
            String id = idTextField.getText();
            if (name.isEmpty() || id.isEmpty()){
                throw new EmptyInputException();
            }
            if (optionChoiceBox.getValue().equals("คณะ")) {
                ((Faculty) data).setFacultyName(nameTextField.getText());
                ((Faculty) data).setFacultyId(idTextField.getText());
            } else {
                String faculty = facultyChoiceBox.getValue();
                if (faculty == null){
                    throw new EmptyInputException();
                }
                ((Major) data).setMajorName(nameTextField.getText());
                ((Major) data).setMajorId(idTextField.getText());
                ((Major) data).setFaculty(facultyChoiceBox.getValue());
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
