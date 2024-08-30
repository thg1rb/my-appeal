package ku.cs.controllers.faculty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import ku.cs.services.ApproverListHardCodeDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyApproverManageController {

    @FXML
    private TableView<Approver> approverTableView;

    private Datasource<ApproverList> approversDatasource;
    private ApproverList approverList;

    @FXML
    public void initialize() {
        approversDatasource = new ApproverListHardCodeDatasource();
        approverList = approversDatasource.readData();

        showApproverTable(approverList);
    }

    private void showApproverTable(ApproverList approverList) {
        TableColumn<Approver, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Approver, String> fullNameColumn = new TableColumn<>("Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(roleColumn);
        approverTableView.getColumns().add(fullNameColumn);

        ObservableList<Approver> approverObservableList = FXCollections.observableArrayList(approverList.getApprovers());
        approverTableView.setItems(approverObservableList);
    }

    @FXML private Pane backgroundAddApproverPane;

    @FXML private Pane addApproverPane;

    @FXML
    public void addApproverButton(){
        backgroundAddApproverPane.setVisible(true);
        addApproverPane.setVisible(true);
    }

    @FXML
    public void onCloseButtonClick(){
        backgroundAddApproverPane.setVisible(false);
        addApproverPane.setVisible(false);
    }

    @FXML
    public void onAppealButtonClick(){
        try {
            FXRouter.goTo("faculty-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutButtonClick(){

        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
