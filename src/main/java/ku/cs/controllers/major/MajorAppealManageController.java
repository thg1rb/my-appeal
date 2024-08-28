package ku.cs.controllers.major;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.models.Appeal;
import ku.cs.models.collections.AppealList;

import ku.cs.services.AppealListHardCodeDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorAppealManageController {
    @FXML private TableView<Appeal> allAppealTable;
    @FXML private TableView<Appeal> selfAppealTable;
    @FXML private Pane previewPopUpPane;
    @FXML private ScrollPane previewScrollPane;
    @FXML private VBox vbox;
    @FXML private Label topicLabel;
    @FXML private Label detailLabel;

    private AppealList appealList;
    private Datasource<AppealList> datasource;

    @FXML
    public void initialize() {
        datasource = new AppealListHardCodeDatasource();
        appealList = datasource.readData();
        showTable(appealList);

    }
    public void showTable(AppealList appealList) {
        TableColumn<Appeal, String> gpaColumn = new TableColumn<>("GPA");
        gpaColumn.setCellValueFactory(new PropertyValueFactory<>("Gpa"));

        TableColumn<Appeal, String> tcasColumn = new TableColumn<>("TCAS");
        tcasColumn.setCellValueFactory(new PropertyValueFactory<>("Tcas"));

        TableColumn<Appeal, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        allAppealTable.getColumns().clear();
        allAppealTable.getColumns().add(gpaColumn);
        allAppealTable.getColumns().add(tcasColumn);
        allAppealTable.getColumns().add(typeColumn);

        allAppealTable.getItems().clear();
        if(appealList!=null){
            for(Appeal appeal : appealList.getAppeals()){
                allAppealTable.getItems().add(appeal);
            }
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
            FXRouter.goTo("major-nisit-manage");
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

}
