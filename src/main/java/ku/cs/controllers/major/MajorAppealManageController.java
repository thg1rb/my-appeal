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
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;

import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.AppealListHardCodeDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class MajorAppealManageController {

    @FXML private TableView<Appeal> allAppealTable;
    @FXML private TableView<Object> selfAppealTable;

    @FXML private ScrollPane detailScrollPane;
    @FXML private VBox vbox;

    @FXML private Label appealManageLabel;
    @FXML private Label appealDetailsLabel;

    @FXML private Label typeLabel;

    @FXML private Label presentStatusLabel;

    @FXML private Pane popupAppealPane;
    @FXML private ChoiceBox statusChoiceBox;

    @FXML private Label topicLabel;
    @FXML private Label semesterLabel;
    @FXML private Label purposeLabel;
    @FXML private Label breakTimeLabel;
    @FXML private Label reasonLabel;
    @FXML private Label subjectLabel;
    @FXML private Label academicTermLabel;
    @FXML private Label yearLabel;

    private String[] statusList = {"----- เลือกสถานะ -----", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน", "ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ"};

    String selectedStatus;
    Appeal selectedAppeal;

    public AppealList appealList;
    public Datasource<AppealList> datasource;
//    private Object selectedAppeal;
    @FXML
    public void initialize() {
        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        showTable(appealList);

        statusChoiceBox.getItems().addAll(statusList);
        statusChoiceBox.setOnAction(this::getStatus);
        statusChoiceBox.setValue(statusList[0]);

        allAppealTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appeal>() {
            @Override
            public void changed(ObservableValue<? extends Appeal> observableValue, Appeal oldValue, Appeal newValue) {
                if (newValue != null) {
                    selectedAppeal = newValue;
                    showPopup(selectedAppeal.getType(), selectedAppeal);
                }
            }
        });
    }

    @FXML
    public void showPopup(String type, Appeal appeal){
            System.out.println(appeal.getPurpose());

            purposeLabel.setVisible(false);
            breakTimeLabel.setVisible(false);

            reasonLabel.setVisible(false);
            subjectLabel.setVisible(false);
            academicTermLabel.setVisible(false);
            yearLabel.setVisible(false);
            semesterLabel.setVisible(false);

            topicLabel.setText(appeal.getTopic());
            reasonLabel.setText(appeal.getReason());



            if(type.equals("คำร้องขอลากิจหรือลาป่วย")) {
                System.out.println(appeal.getPurpose());
                purposeLabel.setText(appeal.getPurpose());
                purposeLabel.setVisible(true);
                purposeLabel.setLayoutY(70);

                breakTimeLabel.setText("ระยะเวลา: " + appeal.getStartDate() + " - " + appeal.getEndDate());
                breakTimeLabel.setVisible(true);
                breakTimeLabel.setLayoutY(100);

                reasonLabel.setText("เหตุผล:" + appeal.getReason());
                reasonLabel.setVisible(true);
                reasonLabel.setLayoutY(130);

                subjectLabel.setText("รายวิชา: " + appeal.getSubjects());
                subjectLabel.setVisible(true);
                subjectLabel.setLayoutY(160);
            }
            else if(type.equals("คำร้องขอพักการศึกษา")) {
                semesterLabel.setText(appeal.getSemester());
                yearLabel.setText(appeal.getYear());
                subjectLabel.setText(appeal.getSubjects());

                semesterLabel.setVisible(true);
                semesterLabel.setLayoutY(70);

                yearLabel.setVisible(true);
                yearLabel.setLayoutY(100);

                subjectLabel.setVisible(true);
                subjectLabel.setLayoutY(130);
            }

            typeLabel.setText(type);
            reasonLabel.setVisible(true);
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
        selectedAppeal = (Appeal) allAppealTable.getSelectionModel().getSelectedItem();
        selectedAppeal.setStatus(selectedStatus);

        popupAppealPane.setVisible(false);
        appealDetailsLabel.setVisible(false);
        appealManageLabel.setVisible(true);

        allAppealTable.getSelectionModel().select(null);

        selectedStatus = null;
    }
    @FXML
    public void cancleOnButtonClick(){
        popupAppealPane.setVisible(false);
        appealDetailsLabel.setVisible(true);
        appealManageLabel.setVisible(false);
    }

    public void showTable(AppealList appealList) {
        TableColumn<Appeal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        TableColumn<Appeal, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));

        TableColumn<Appeal, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        allAppealTable.getColumns().clear();
        allAppealTable.getColumns().add(dateColumn);
        allAppealTable.getColumns().add(ownerColumn);
        allAppealTable.getColumns().add(typeColumn);

        allAppealTable.getItems().clear();
        if (appealList != null) {
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
    public void onLogoutButtonClick(){
        try{
            FXRouter.goTo("login");
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
