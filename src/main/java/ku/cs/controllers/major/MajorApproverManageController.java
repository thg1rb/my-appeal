package ku.cs.controllers.major;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ku.cs.controllers.general.ApproverEditController;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.persons.Approver;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.User;

import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ApproverListFileDatasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorApproverManageController {

    @FXML private AnchorPane mainPane;
    @FXML private Pane navbarAnchorPane;
    @FXML private TableView<Approver> approverTableView;
    @FXML private Label totalLabel;

    private User user;
    private Datasource<ApproverList> approverDatasource;
    private ApproverList approverList;
    private ApproverList departmentTierApproverList;
    private Approver selectedApprover;
    private boolean addMode;

    public void initialize() {
        user = (DepartmentStaff) FXRouter.getData();

        approverDatasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = approverDatasource.readData();

        departmentTierApproverList = approverList.getDepartmentTierApprovers();

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


        showTable(departmentTierApproverList);

        approverTableView.setOnMouseClicked(event -> {
            selectedApprover = approverTableView.getSelectionModel().getSelectedItem();
            addMode = false;
            showPopup();
        });
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

            approverDatasource.writeData(approverList);
            approverList = approverDatasource.readData();
            departmentTierApproverList = approverList.getDepartmentTierApprovers();
            showTable(departmentTierApproverList);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showTable(ApproverList approverList){
        TableColumn<Approver, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Approver, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(roleColumn);
        approverTableView.getColumns().add(fullNameColumn);

        roleColumn.setPrefWidth(550);
        fullNameColumn.setPrefWidth(550);

        approverTableView.getItems().clear();
        if (approverList != null) {
            for(Approver approver : approverList.getApprovers()){
                approverTableView.getItems().add(approver);
            }
        }

        roleColumn.setSortable(false);
        fullNameColumn.setSortable(false);
        updateTotalLabel();
    }

    public void addApproverButtonClick(){
        addMode = true;
        showPopup();
    }

    private void updateTotalLabel() {
        totalLabel.setText("จำนวนผู้อนุมัติทั้งหมด " + approverTableView.getItems().size() + " คน");
    }
}
