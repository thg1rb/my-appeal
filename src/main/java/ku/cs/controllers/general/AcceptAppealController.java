package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.Approver;

import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.ApproverListFileDatasource;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.ModifyDateListFileDatasource;
import ku.cs.services.fileutilities.SignFileUploader;

import java.io.File;
import java.util.Date;

public class AcceptAppealController {
    @FXML private AnchorPane mainPane;

    @FXML private TableView<Approver> approverTableView;
    @FXML private RadioButton finishHereRadioButton;
    @FXML private RadioButton moreOperationRadioButton;
    @FXML private Label approverErrorLabel;
    @FXML private Button uploadButton;
    @FXML private TextField searchTextField;
    @FXML private ImageView imageViewButtonImageView;
    @FXML private Label errorUploadLabel;

    private User staff;
    private String selectedStatus;
    private String subStatus;
    private String role;

    private Datasource<ApproverList> approverDatasource;
    private ApproverList approverList;
    private ApproverList specificTierApproverList;
    private Approver selectedApprover;
    private Appeal selectedAppeal;
    private ToggleGroup toggleGroup;
    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    @FXML
    public void initialize() {
        approverDatasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = approverDatasource.readData();

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        finishHereRadioButton.setSelected(true);
        subStatus = " | " + finishHereRadioButton.getText();
        toggleGroup = new ToggleGroup();
        finishHereRadioButton.setToggleGroup(toggleGroup);
        moreOperationRadioButton.setToggleGroup(toggleGroup);

        approverTableView.setRowFactory(v -> {
            TableRow<Approver> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                selectedApprover = approverTableView.getSelectionModel().getSelectedItem();
            });
            return row;
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String search = searchTextField.getText();
            showTable(specificTierApproverList, search);
        });

        uploadButton.setOnAction(e ->{
            uploadPDF();
        });
    }

    public void setVar(User staff, String role, String status, Appeal appeal) {
        this.staff = staff;
        this.role = role;
        if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            finishHereRadioButton.setSelected(true);
            specificTierApproverList = approverList.getDepartmentTierApprovers(((DepartmentStaff)staff).getDepartment());
        } else if (role.equals("เจ้าหน้าที่คณะ")) {
            finishHereRadioButton.setSelected(true);
            moreOperationRadioButton.setVisible(false);
            moreOperationRadioButton.setDisable(true);
            specificTierApproverList = approverList.getFacultyTierApprovers(((FacultyStaff)staff).getFaculty());
        }
        showTable(specificTierApproverList, null);
        selectedStatus = status;
        selectedAppeal = appeal;
    }

    public void showTable(ApproverList approverList, String searchText) {
        TableColumn<Approver, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Approver, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(roleColumn);
        approverTableView.getColumns().add(fullNameColumn);

        int size = approverTableView.getColumns().size();
        double totalWidth = 448 - approverTableView.getPadding().getLeft() - approverTableView.getPadding().getRight();
        approverTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (TableColumn<?, ?> col : approverTableView.getColumns()) {
            col.setPrefWidth(totalWidth / size);
        }

        approverTableView.getItems().clear();
        if (approverList != null) {
            if (searchText != null) {
                for (Approver approver : approverList.getApprovers()) {
                    if (approver.getFullName().contains(searchText)) {
                        approverTableView.getItems().add(approver);
                    }
                }
            }else {
                for (Approver approver : approverList.getApprovers()) {
                    approverTableView.getItems().add(approver);
                }
            }
        }

        roleColumn.setSortable(false);
        fullNameColumn.setSortable(false);
        approverTableView.getColumns().forEach(column -> column.setReorderable(false));
    }

    public void onConfirmButtonClick(ActionEvent event){
        if (selectedApprover == null) {
            approverErrorLabel.setVisible(true);
        } else if (uploadButton.getText().equals("อัปโหลดไฟล์")){
            errorUploadLabel.setVisible(true);
        }
        else {
            subStatus = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
            String modifyDate = DateTimeService.detailedDateToString(new Date());
            selectedAppeal.setModifyDate(modifyDate);
            selectedAppeal.setStatus(selectedStatus + " | " + subStatus);
            if (role.equals("เจ้าหน้าที่ภาควิชา")) {
                modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setDepartmentApproveDate(modifyDate);
            } else if (role.equals("เจ้าหน้าที่คณะ")) {
                modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setFacultyApproveDate(modifyDate);
            }
            modifyDateListDatasource.writeData(modifyDateList);
            onCloseButtonClick(event);
        }
    }

    private void uploadPDF(){
        String path = "data" + File.separator + "approves-signs";
        SignFileUploader signFileUploader = new SignFileUploader(staff, uploadButton, selectedAppeal, path);
        signFileUploader.upload((Stage) uploadButton.getScene().getWindow());
        if (signFileUploader.uploadSuccess()) {
            uploadButton.setText("");

            if (role.equals("เจ้าหน้าที่ภาควิชา")) selectedAppeal.setDepartmentSignature(signFileUploader.getFullPath());
            else if (role.equals("เจ้าหน้าที่คณะ")) selectedAppeal.setFacultySignature(signFileUploader.getFullPath());

        } else {
            errorUploadLabel.setVisible(true);
        }
    }

    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}