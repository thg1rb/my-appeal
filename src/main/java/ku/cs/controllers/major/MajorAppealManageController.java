package ku.cs.controllers.major;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.models.collections.AppealList;

import ku.cs.services.AppealListHardCodeDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.util.ArrayList;

public class MajorAppealManageController {

    @FXML private TableView<Object> allAppealTable;
    @FXML private TableView<Object> selfAppealTable;

    @FXML private ScrollPane detailScrollPane;
    @FXML private VBox vbox;

    @FXML private Label appealManageLabel;
    @FXML private Label appealDetailsLabel;

    @FXML private TabPane appealTabPane;

    @FXML private Label typeLabel;

    @FXML private Label presentStatusLabel;

    @FXML private Pane popupAppealPane;
    @FXML private ChoiceBox statusChoiceBox;

    @FXML private Label topicLabel;
    @FXML private Label detailLabel;

    @FXML private Label gpaLabel;
    @FXML private Label tcasLabel;

    @FXML private Label purposeLabel;
    @FXML private Label breakTimeLabel;
    @FXML private Label reasonLabel;
    @FXML private Label subjectLabel;

    @FXML private Label academicTermLabel;
    @FXML private Label yearLabel;

    private String[] statusList = {"----- เลือกสถานะ -----", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน", "ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ"};

    String selectedStatus;

    private AppealList appealList;
    private Datasource<AppealList> datasource;
    private Object selectedAppeal;
    @FXML
    public void initialize() {
        datasource = new AppealListHardCodeDatasource();
        appealList = datasource.readData();
        showTable(appealList);

        statusChoiceBox.getItems().addAll(statusList);
        statusChoiceBox.setOnAction(this::getStatus);
        statusChoiceBox.setValue(statusList[0]);


        allAppealTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appeal>() {
            @Override
            public void changed(ObservableValue observable, Appeal oldValue, Appeal newValue){
                if(newValue != null){

                        if(newValue.getType() == "Retire Appeal"){
                            showPopup("Retire Appeal", newValue);
                        }
                }
            }

        });
    }

    @FXML
    public void showPopup(String type, Appeal appeal){

            gpaLabel.setVisible(false);
            tcasLabel.setVisible(false);

            purposeLabel.setVisible(false);
            breakTimeLabel.setVisible(false);
//
            reasonLabel.setVisible(false);
            subjectLabel.setVisible(false);
            academicTermLabel.setVisible(false);
            yearLabel.setVisible(false);

            if(type == "Retire Appeal"){
                RetireAppeal currentAppeal = (RetireAppeal) appeal;

                topicLabel.setText(currentAppeal.getTopic());
                detailLabel.setText(currentAppeal.getDetails());
                gpaLabel.setText("GPA: " + currentAppeal.getGpa());
                tcasLabel.setText("คะแนน TCAS: " + currentAppeal.getTcas());
                gpaLabel.setVisible(true);
                tcasLabel.setVisible(true);
            }

            else if(type == "General Appeal"){
                GeneralAppeal currentAppeal = (GeneralAppeal) appeal;

                detailLabel.setText(currentAppeal.getDetails());
                topicLabel.setText(currentAppeal.getTopic());
            }
            else if(type == "Break Appeal"){
                BreakAppeal currentAppeal = (BreakAppeal) appeal;

                detailLabel.setText(currentAppeal.getDetails());
                topicLabel.setText(currentAppeal.getTopic());

                purposeLabel.setText(currentAppeal.getPurpose());
                purposeLabel.setVisible(true);
                purposeLabel.setLayoutY(70);

                breakTimeLabel.setText("ระยะเวลา: " + currentAppeal.getStartTakingBreak() + " - " + currentAppeal.getEndTakingBreak());
                breakTimeLabel.setLayoutY(100);
                breakTimeLabel.setVisible(true);

                reasonLabel.setText("เหตุผล:" + currentAppeal.getReason());
                reasonLabel.setVisible(true);
                reasonLabel.setLayoutY(130);

                subjectLabel.setText("รายวิชา: " + currentAppeal.getSubjects());
                subjectLabel.setVisible(true);
                subjectLabel.setLayoutY(160);
            }


//            else if(type == "General Appeal"){
//                GeneralAppeal currentAppeal = (GeneralAppeal) appeal;
//            }
//            else if(type == "Break Appeal"){
//                BreakAppeal currentAppeal = (BreakAppeal) appeal;
//            }
//            else if(type == "Installment Appeal"){
//                InstallmentAppeal currentAppeal = (InstallmentAppeal) appeal;
//            }
//            else if(type == "Suspend Appeal"){
//                SuspendAppeal currentAppeal = (SuspendAppeal) appeal;
//            }

            typeLabel.setText(type);
            detailLabel.setVisible(true);
            vbox.prefWidthProperty().bind(detailScrollPane.widthProperty().subtract(40));

            detailScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            detailScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            detailScrollPane.setContent(vbox);
            detailScrollPane.layout();

            popupAppealPane.setVisible(true);
            appealManageLabel.setVisible(false);
            appealDetailsLabel.setVisible(true);


    }

    @FXML
    public void confirmOnButtonClick(){
        selectedAppeal = allAppealTable.getSelectionModel().getSelectedItem();
        selectedAppeal.setStatus(selectedStatus);

        popupAppealPane.setVisible(false);
        appealDetailsLabel.setVisible(false);
        appealManageLabel.setVisible(true);

        allAppealTable.getSelectionModel().select(null);

        selectedStatus = null;
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

    public void getStatus(Event event) {
        selectedStatus = (String) statusChoiceBox.getValue();
        System.out.println(selectedStatus);
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
