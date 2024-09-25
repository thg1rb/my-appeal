package ku.cs.controllers.major;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Advisor;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import ku.cs.services.exceptions.EmptyInputException;

public class MajorNisitEditPopupController {
    @FXML TextField nisitNameTextField;
    @FXML TextField nisitLastNameTextField;
    @FXML TextField nisitIdTextField;
    @FXML TextField nisitEmailTextField;
    @FXML ComboBox<String> professorComboBox;
    @FXML Button addButton;
    @FXML Button confirmButton;
    @FXML Label topicLabel;

    private Datasource<UserList> advisorDatasource;
    private UserList advisorList;
    private HashMap<String, UUID> advisorMap;

    @FXML Label nameErrorLabel;
    @FXML Label lastNameErrorLabel;
    @FXML Label idErrorLabel;
    @FXML Label emailErrorLabel;

    private DepartmentStaff user;
    private User nisit;
    private UserList studentRoster;

    public void setNisit(User selectedNisit){
        this.nisit = selectedNisit;
        if (nisit != null) {
            nisitNameTextField.setText(nisit.getFirstName());
            nisitLastNameTextField.setText(nisit.getLastName());
            nisitIdTextField.setText(((Student)nisit).getStudentId());
            nisitEmailTextField.setText(((Student)nisit).getEmail());
            UUID advisorUUID = ((Student) nisit).getAdvisorUUID();
            if (advisorUUID != null && !advisorUUID.equals("null")){
                User advisor = advisorList.findUserByUUID(advisorUUID);
                if (advisor != null){
                    professorComboBox.setValue(advisor.getFullName());
                } else {
                    professorComboBox.getSelectionModel().selectFirst();
                }
            }else{
                professorComboBox.getSelectionModel().clearSelection();
            }
        }
    }

    public void setMode(boolean addMode){
        if(addMode){
            confirmButton.setVisible(false);
            topicLabel.setText("เพิ่มข้อมูลนิสิต");

        }
        else{
            confirmButton.setVisible(true);
            topicLabel.setText("แก้ไขข้อมูลนิสิต");
        }
    }

    public void setUser(DepartmentStaff user, UserList studentRoster){
        this.user = user;
        this.studentRoster = studentRoster;

        initMapAndComboBox();
    }

    private void initMapAndComboBox(){
        advisorDatasource = new UserListDatasource("data" + File.separator + "users", "advisor.csv");
        advisorMap = new HashMap<>();

        professorComboBox.getItems().clear();
        advisorMap.put("ไม่มีอาจารย์ที่ปรึกษา", null);
        professorComboBox.getItems().add("ไม่มีอาจารย์ที่ปรึกษา");
        advisorList = advisorDatasource.readData().getUsersByDepartment(user.getDepartment());
        for (User advisor : advisorList.getUsers()){
            advisorMap.put(advisor.getFullName(), advisor.getUuid());
            professorComboBox.getItems().add(advisor.getFullName());
        }
    }

    // Valid the input
    public boolean isEmptyTextField() {
        boolean error = false;
        nameErrorLabel.setVisible(false);
        lastNameErrorLabel.setVisible(false);
        idErrorLabel.setVisible(false);
        emailErrorLabel.setVisible(false);

        TextField[] textFields = {nisitNameTextField, nisitLastNameTextField, nisitIdTextField, nisitEmailTextField};
        Label[] errorLabels = {nameErrorLabel, lastNameErrorLabel, idErrorLabel, emailErrorLabel};

        for (int i = 0; i < textFields.length; i++) {
            if (textFields[i].getText().isEmpty()) {
                errorLabels[i].setVisible(true);
                error = true;
            } else {
                errorLabels[i].setVisible(false);
            }
        }
        return error;
    }

    public void onConfirmButtonClick(ActionEvent event){
        try {
            if (isEmptyTextField()) {
                throw new EmptyInputException();
            }
            nisit.setFirstName(nisitNameTextField.getText());
            nisit.setLastName(nisitLastNameTextField.getText());
            ((Student)nisit).setStudentId(nisitIdTextField.getText());
            ((Student)nisit).setEmail(nisitEmailTextField.getText());
            ((Student)nisit).setAdvisor(advisorMap.get(professorComboBox.getValue()));
            onCancleButtonClick(event);
        }
        catch (EmptyInputException e) {
            System.out.println("Empty text field.");
        }
    }

    public void onAddButtonClick(ActionEvent event){
        if(professorComboBox.getValue() == null){
            studentRoster.addUser(new Student(nisitNameTextField.getText(), nisitLastNameTextField.getText(), nisitIdTextField.getText(), nisitEmailTextField.getText(), user.getFaculty(), user.getDepartment()));
        }
        else{
            studentRoster.addUser(new Student(nisitNameTextField.getText(), nisitLastNameTextField.getText(), nisitIdTextField.getText(), nisitEmailTextField.getText(), user.getFaculty(), user.getDepartment(), advisorMap.get(professorComboBox.getValue())));

        }
        onCancleButtonClick(event);
    }

    @FXML
    public void onCancleButtonClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
