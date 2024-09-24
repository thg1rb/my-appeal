package ku.cs.controllers.major;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.exceptions.EmptyInputException;

public class MajorNisitEditPopupController {
    @FXML TextField nisitNameTextField;
    @FXML TextField nisitLastNameTextField;
    @FXML TextField nisitIdTextField;
    @FXML TextField nisitEmailTextField;
    @FXML TextField professorTextField;
    @FXML Button addButton;
    @FXML Button confirmButton;
    @FXML Label topicLabel;

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
            String firstName = nisit.getFirstName();
            String lastName = nisit.getLastName();
            String id = ((Student)nisit).getStudentId();
            String email = ((Student)nisit).getEmail();
            String professor = ((Student)nisit).getAdvisor();

            nisitNameTextField.setText(firstName);
            nisitLastNameTextField.setText(lastName);
            nisitIdTextField.setText(id);
            nisitEmailTextField.setText(email);
            professorTextField.setText(professor);
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
            ((Student)nisit).setAdvisor(professorTextField.getText());
            onCancleButtonClick(event);
        }
        catch (EmptyInputException e) {
            System.out.println("Empty text field.");
        }
    }

    public void onAddButtonClick(ActionEvent event){
        if(professorTextField.getText().isEmpty()){
            studentRoster.addUser(new Student(nisitNameTextField.getText(), nisitLastNameTextField.getText(), nisitIdTextField.getText(), nisitEmailTextField.getText(), user.getFaculty(), user.getDepartment()));
        }
        else{
            studentRoster.addUser(new Student(nisitNameTextField.getText(), nisitLastNameTextField.getText(), nisitIdTextField.getText(), nisitEmailTextField.getText(), user.getFaculty(), user.getDepartment(), professorTextField.getText()));

        }
        onCancleButtonClick(event);
    }

    @FXML
    public void onCancleButtonClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
