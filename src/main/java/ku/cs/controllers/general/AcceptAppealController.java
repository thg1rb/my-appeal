package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.Approver;

import ku.cs.services.datasources.ApproverListFileDatasource;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.util.Date;

public class AcceptAppealController {
    @FXML
    TableView<Approver> approverTableView;
    @FXML
    RadioButton radioButton1;
    @FXML
    RadioButton radioButton2;
    @FXML Label approverErrorLabel;

    private String selectedStatus;
    private String subStatus;
    private String role;


    private Datasource<ApproverList> approverDatasource;
    private ApproverList approverList;
    private Approver selectedApprover;
    private Appeal selectedAppeal;
    private ToggleGroup toggleGroup;
    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    public void initialize() {
        approverDatasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = approverDatasource.readData();

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        toggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(toggleGroup);
        radioButton2.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                System.out.println(selectedRadioButton.getText());
                 subStatus = " | คำร้อง" + selectedRadioButton.getText();
            }
        });
        showTable(approverList);
        approverTableView.setRowFactory(v -> {
            TableRow<Approver> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                selectedApprover = approverTableView.getSelectionModel().getSelectedItem();
            });
            return row;
        });

    }

    public void setVar(String r, String status, Appeal appeal) {
        role = r;
        if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            radioButton2.setSelected(true);
        } else if (role.equals("เจ้าหน้าที่คณะ")) {
            radioButton2.setSelected(false);
        }
        selectedStatus = status;
        selectedAppeal = appeal;


    }

    public void showTable(ApproverList approverList) {
        TableColumn<Approver, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Approver, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(roleColumn);
        approverTableView.getColumns().add(fullNameColumn);

        int size = approverTableView.getColumns().size();
        for (TableColumn<?, ?> col : approverTableView.getColumns()) {
            col.setPrefWidth((double) 311 / size);
        }

        approverTableView.getItems().clear();
        if (approverList != null) {
            for(Approver approver : approverList.getApprovers()){
                approverTableView.getItems().add(approver);
            }
        }

        roleColumn.setSortable(false);
        fullNameColumn.setSortable(false);
    }


    public void onConfirmButtonClick(ActionEvent event){
        if (selectedApprover == null) {
            approverErrorLabel.setVisible(true);
        }
        else {
            String modifyDate = DateTimeService.detailedDateToString(new Date());
            selectedAppeal.setModifyDate(modifyDate);
            System.out.println(selectedStatus);
            selectedAppeal.setStatus(selectedStatus+subStatus);
            if (role.equals("เจ้าหน้าที่ภาควิชา")) {
                modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setDepartmentApproveDate(modifyDate);
            } else if (role.equals("เจ้าหน้าที่คณะ")) {
                modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setFacultyApproveDate(modifyDate);
            }
            onCloseButtonClick(event);
        }

    }

    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}