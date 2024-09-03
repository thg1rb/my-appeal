package ku.cs.controllers.major;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.collections.StudentList;
import ku.cs.models.collections.UserList;
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

    private User user;
    private Student nisit;
    private StudentList studentRoster;

    public void initialize(){

    }
    public void setNisit(Student selectedNisit, User user){
        this.nisit = selectedNisit;
        if (nisit != null) {
            nisitNameTextField.setText(nisit.getFirstName());
            nisitLastNameTextField.setText(nisit.getLastName());
            nisitIdTextField.setText(nisit.getId());
            nisitEmailTextField.setText(nisit.getEmail());
            professorTextField.setText(nisit.getAdvisor());
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
    public void setUser(User user, StudentList studentRoster){
        this.user = user;
        this.studentRoster = studentRoster;
    }

    public void onConfirmButtonClick(ActionEvent event){
        nisit.setFirstName(nisitNameTextField.getText());
        nisit.setLastName(nisitLastNameTextField.getText());
        nisit.setFullName();
        nisit.setID(nisitIdTextField.getText());
        nisit.setEmail(nisitEmailTextField.getText());
        nisit.setAdvisor(professorTextField.getText());
        onCancleButtonClick(event);
    }

    public void onAddButtonClick(ActionEvent event){
        if(professorTextField.getText().isEmpty()){
            studentRoster.addStudent(nisitIdTextField.getText(), nisitEmailTextField.getText(), nisitNameTextField.getText(), nisitLastNameTextField.getText(), user);
        }
        else{
            studentRoster.addStudent(nisitIdTextField.getText(), nisitEmailTextField.getText(), nisitNameTextField.getText(), nisitLastNameTextField.getText(), professorTextField.getText(), user);
        }
        onCancleButtonClick(event);
    }

    @FXML
    public void onCancleButtonClick(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
