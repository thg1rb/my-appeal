package ku.cs.controllers.major;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
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
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.persons.Approver;
import ku.cs.models.persons.User;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorApproverManageController {
    @FXML Label usernameLabel;
    @FXML Label roleLabel;
    @FXML private TableView approverTableView;

    private Datasource<ApproverList> datasource;
    private ApproverList approverList;
    private User user;
    private Approver selectedApprover;
    public void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        datasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = datasource.readData();

        showTable(approverList);

        approverTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Approver>() {
            @Override
            public void changed(ObservableValue<? extends Approver> observableValue, Approver oldValue, Approver newValue) {
                if(newValue != null) {
                    selectedApprover = newValue;
                }
            }
        });
    }

    public void showPopup(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/approver.fxml"));
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

    @FXML
    protected void onApproverManageButtonClick() {
        try {
            FXRouter.goTo("major-approver-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNisitManageButtonClick() {
        try {
            FXRouter.goTo("major-nisit-manage", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAppealManageButtonClick() {
        try {
            FXRouter.goTo("major-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onLogoutButtonClick(){
        try{
            FXRouter.goTo("login");
        }
        catch(IOException e){
            throw new RuntimeException(e);
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
