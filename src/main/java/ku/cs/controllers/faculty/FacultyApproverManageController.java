package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.ApproverEditController;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.persons.Approver;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;

import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ApproverListFileDatasource;
import ku.cs.services.FXRouter;

import java.io.IOException;


public class FacultyApproverManageController {
    private User user;

    private Datasource<ApproverList> approversDatasource;
    private ApproverList approverList;
    private ApproverList facultyTierApproverList;
    private Approver selectedApprover;
    private boolean addMode;

    @FXML private AnchorPane mainPane;
    @FXML private Pane navbarAnchorPane;

    @FXML private TableView<Approver> approverTableView;
    @FXML private TextField searchTextField;

    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        user = (FacultyStaff)FXRouter.getData();

        approversDatasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = approversDatasource.readData();
        facultyTierApproverList = approverList.getFacultyTierApprovers();

        ProgramSetting.getInstance().applyStyles(mainPane);

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        showApproverTable(facultyTierApproverList);
    }

    private void showApproverTable(ApproverList approverList) {
        TableColumn<Approver, String> roleColumn = new TableColumn<>("ตำแหน่ง");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Approver, String> fullNameColumn = new TableColumn<>("ชื่อ-สกุล");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(roleColumn);
        approverTableView.getColumns().add(fullNameColumn);

        roleColumn.setPrefWidth(550);
        fullNameColumn.setPrefWidth(550);

        approverTableView.getItems().clear();
        for (Approver approver : approverList.getApprovers()) {
            approverTableView.getItems().add(approver);
        }
        approverTableView.setRowFactory(tv -> {
            TableRow<Approver> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    selectedApprover = row.getItem();
                    addMode = false;
                    showPopup();
                }
            });
            return row;
        });
        approverTableView.getColumns().forEach(column -> column.setReorderable(false));

        updateTotalLabel();
    }

    public void showPopup(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/approver-popup.fxml"));
            Parent root = loader.load();
            ApproverEditController controller = loader.getController();

            controller.setRole(user);
            controller.setMode(addMode, selectedApprover, user, approverList);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            controller.setStage(stage);

            stage.showAndWait();

            approversDatasource.writeData(approverList);
            approverList = approversDatasource.readData();
            facultyTierApproverList = approverList.getFacultyTierApprovers();

            showApproverTable(facultyTierApproverList);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void addApproverButton() {
        addMode = true;
        showPopup();
    }

    @FXML
    public void onSearchKeyReleased() {
        String searchText = searchTextField.getText().toLowerCase();
        ApproverList filteredApproverList = new ApproverList();

        if (searchText.isEmpty()) {
            showApproverTable(facultyTierApproverList);
        } else {
            for (Approver approver : facultyTierApproverList.getApprovers()) {
                String fullName = (approver.getFirstName() + " " + approver.getLastName()).toLowerCase();
                if (fullName.contains(searchText)) {
                    filteredApproverList.addApprover(approver);
                }
            }
            showApproverTable(filteredApproverList);
        }
    }

    private void updateTotalLabel() {
        totalLabel.setText("จำนวนผู้อนุมัติทั้งหมด " + approverTableView.getItems().size() + " คน");
    }
}
