package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.persons.Approver;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;

public class AcceptAppealController {
    @FXML TableView<Approver> appealTable;
    @FXML ChoiceBox<String> statusChoiceBox;
    @FXML ComboBox<String> statusComboBox;


    private String[] majorStatusList = {"คำร้องส่งต่อให้คณบดี", "คำร้องดำเนินการครบถ้วน"};
    private String[] facultyStatusList = {"คำร้องดำเนินการครบถ้วน"};
    private String selectedStatus;
    private String role;
    private Datasource<ApproverList> approverDatasource;
    private ApproverList approverList;
    private Approver selectedApprover;

    public void initialize() {
//        approverDatasource = new ApproverListFileDatasource("data", "approver.csv");
//        approverList = approverDatasource.readData();

//        showTable(approverList);
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

    public void showTable() {

    }

}
