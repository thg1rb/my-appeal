package ku.cs.controllers.major;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.models.Appeal;
import ku.cs.models.RetireAppeal;
import ku.cs.models.collections.AppealList;

import ku.cs.services.AppealListHardCodeDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorAppealManageController {
    @FXML private AnchorPane scenePane;
    @FXML private TableView<Appeal> allAppealTable;
    @FXML private TableView<Appeal> selfAppealTable;
    @FXML private Pane previewPopUpPane;
    @FXML private ScrollPane detailScrollPane;
    @FXML private VBox vbox;
    @FXML private Label appealManageLabel;
    @FXML private TabPane appealTabPane;
    @FXML private Label typeLabel;
    @FXML private Label topicLabel;
    @FXML private Label detailLabel;

    @FXML private Pane retireAppealPane;
    @FXML private Label gpaLabel;
    @FXML private Label tcasLabel;


    private AppealList appealList;
    private Datasource<AppealList> datasource;

    @FXML
    public void initialize() {
        datasource = new AppealListHardCodeDatasource();
        appealList = datasource.readData();
        showTable(appealList);
        allAppealTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appeal>() {
            @Override
            public void changed(ObservableValue observable, Appeal oldValue, Appeal newValue){
                if(newValue != null){
                        if(newValue.getType() == "Retire Appeal"){
                            RetireAppeal retireAppeal = (RetireAppeal)newValue;

                            typeLabel.setText(newValue.getType());
                            detailLabel.setText(retireAppeal.getDetails());
                            gpaLabel.setText(Double.toString(retireAppeal.getGpa()));
                            tcasLabel.setText(Integer.toString(retireAppeal.getTcas()));

                            detailLabel.setWrapText(true);
                            vbox.prefWidthProperty().bind(detailScrollPane.widthProperty());

                            detailScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                            detailScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                            detailScrollPane.setContent(vbox);
                            detailScrollPane.layout();

                            retireAppealPane.setVisible(true);
                            appealManageLabel.setVisible(false);

                        }



                }
            }
        });
    }

    public void showTable(AppealList appealList) {
        TableColumn<Appeal, String> gpaColumn = new TableColumn<>("GPA");
        gpaColumn.setCellValueFactory(new PropertyValueFactory<>("gpa"));

        TableColumn<Appeal, String> tcasColumn = new TableColumn<>("TCAS");
        tcasColumn.setCellValueFactory(new PropertyValueFactory<>("tcas"));

        TableColumn<Appeal, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
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
