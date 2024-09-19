package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;

import java.util.ArrayList;

public class AdminMajorFacultyPopupController {
    @FXML private Text modeText;
    @FXML private Text nameText;
    @FXML private Text idText;
    @FXML private Text belongFacultyText;

    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;

    @FXML private ChoiceBox<String> optionChoiceBox;
    @FXML private ChoiceBox<String> facultyChoiceBox;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button editButton;

    private Object data;
    private FacultyList facultyList;
    private MajorList majorList;

    private String[] optionChoice = {"คณะ", "ภาควิชา"};
    private ArrayList<String> facultyChoice;

    private boolean editMode;

    @FXML
    private void initialize() {
        optionChoiceBox.getItems().addAll(optionChoice);

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
        }else{
            modeText.setText("เพิ่มข้อมูล" + tabSelected);
            editButton.setDisable(true);
            confirmButton.setDisable(false);
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
        String name = nameTextField.getText();
        String id = idTextField.getText();

        if (optionChoiceBox.getValue().equals("คณะ")){
            facultyList.addFaculty(name, id);
        }else{
            String faculty = facultyChoiceBox.getValue();
            majorList.addMajor(name, faculty, id, facultyList);
        }

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void onCancelButtonClicked(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void onEditButtonClicked(){
        if (optionChoiceBox.getValue().equals("คณะ")){
            ((Faculty) data).setFacultyName(nameTextField.getText());
            ((Faculty) data).setFacultyId(idTextField.getText());
        }else {
            ((Major) data).setMajorName(nameTextField.getText());
            ((Major) data).setMajorId(idTextField.getText());
            ((Major) data).setFaculty(facultyChoiceBox.getValue());
        }

        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}
