package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import ku.cs.models.persons.Approver;

public class AcceptAppealController {
    @FXML TableView<Approver> appealTable;
    @FXML ChoiceBox<String> statusChoiceBox;
    @FXML ComboBox<String> statusComboBox;


    private String[] majorStatusList = {"คำร้องส่งต่อให้คณบดี", "คำร้องดำเนินการครบถ้วน"};
    private String[] facultyStatusList = {"คำร้องดำเนินการครบถ้วน"};
    private String selectedStatus;
    private String role;

    public void initialize() {

    }

    public void setRole(String r) {
        role = r;
        if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            statusComboBox.getItems().addAll(majorStatusList);
        }
        else if (role.equals("เจ้าหน้าที่คณะ")) {
            statusComboBox.getItems().addAll(facultyStatusList);
        }
    }

}
