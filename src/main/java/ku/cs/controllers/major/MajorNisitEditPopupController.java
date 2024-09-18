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

public class MajorNisitEditPopupController {
    @FXML TextField nisitNameTextField;
    @FXML TextField nisitLastNameTextField;
    @FXML TextField nisitIdTextField;
    @FXML TextField nisitEmailTextField;
    @FXML TextField professorTextField;
    @FXML Button addButton;
    @FXML Button confirmButton;
    @FXML Label topicLabel;

    private DepartmentStaff user;
    private User nisit;
    private UserList studentRoster;

    public void initialize(){

    }
    public void setNisit(User selectedNisit){
        this.nisit = selectedNisit;
        if (nisit != null) {
            nisitNameTextField.setText(nisit.getFirstName());
            nisitLastNameTextField.setText(nisit.getLastName());
            nisitIdTextField.setText(((Student)nisit).getStudentId());
            nisitEmailTextField.setText(((Student)nisit).getEmail());
            professorTextField.setText(((Student)nisit).getAdvisor());
        }
    }
    public void setMode(boolean addMode, User selectedNisit){
        if(addMode){
            confirmButton.setVisible(false);
            topicLabel.setText("เพิ่มข้อมูลนิสิต");
        }
        else{
            confirmButton.setVisible(true);
            topicLabel.setText("แก้ไขข้อมูลนิสิต");
            setNisit(selectedNisit);
        }
    }
    public void setUser(DepartmentStaff user, UserList studentRoster){
        this.user = user;
        this.studentRoster = studentRoster;
    }

    public void onConfirmButtonClick(ActionEvent event){
        nisit.setFirstName(nisitNameTextField.getText());
        nisit.setLastName(nisitLastNameTextField.getText());
        ((Student)nisit).setStudentId(nisitIdTextField.getText());
        ((Student)nisit).setEmail(nisitEmailTextField.getText());
        ((Student)nisit).setAdvisor(professorTextField.getText());
        onCancleButtonClick(event);
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
