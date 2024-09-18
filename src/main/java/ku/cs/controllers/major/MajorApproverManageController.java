package ku.cs.controllers.major;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.ApproverEditController;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Approver;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.User;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorApproverManageController {
    @FXML private Pane navbarAnchorPane;
    @FXML private TableView approverTableView;

    private DepartmentStaff user;
    private Datasource<ApproverList> approverDatasource;
    private ApproverList approverList;
    private Approver selectedApprover;

    public void initialize() {
        user = (DepartmentStaff) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        approverDatasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = approverDatasource.readData();

        showTable(approverList);

        approverTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Approver>() {
            @Override
            public void changed(ObservableValue<? extends Approver> observableValue, Approver oldValue, Approver newValue) {
                if(newValue != null) {
                    selectedApprover = newValue;
                    showPopup();
                }
            }
        });
    }

    public void showPopup(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/approver-popup.fxml"));
            Parent root = loader.load();
            ApproverEditController controller = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            stage.showAndWait();
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

        if (approverList != null) {
            for(Approver approver : approverList.getApprovers()){
                approverTableView.getItems().add(approver);
            }
        }

        roleColumn.setSortable(false);
        fullNameColumn.setSortable(false);
    }
}
