package ku.cs.controllers.faculty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.User;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyAppealManageController {
    @FXML private Pane navbarAnchorPane;

    private User user;

    @FXML private TableView<Appeal> allAppealTable;
    @FXML private TableView<Appeal> selfAppealTable;

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

    private String[] statusList = {"ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "อนุมัติโดยคณบดี | คำร้องดำเนินการครบถ้วน", "ปฏิเสธโดยคณบดี  | คำร้องถูกปฏิเสธ"};

    String selectedStatus;
    Appeal selectedAppeal;

    public AppealList appealList;
    public Datasource<AppealList> datasource;

    @FXML
    public void initialize(){
        user = (User) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

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
                    showPopup(selectedAppeal);
                }
            }
        });
    }

    @FXML
    public void showPopup(Appeal appeal){
        purposeLabel.setVisible(false);
        breakTimeLabel.setVisible(false);

        reasonLabel.setVisible(false);
        subjectLabel.setVisible(false);
        academicTermLabel.setVisible(false);
        yearLabel.setVisible(false);
        semesterLabel.setVisible(false);

        if (appeal.isGeneralAppeal()) {
            GeneralAppeal generalAppeal = (GeneralAppeal) appeal;
            topicLabel.setText(generalAppeal.getTopic());
            reasonLabel.setText(generalAppeal.getReason());
        }
        else if (appeal.isSuspendAppeal()) {
            SuspendAppeal suspendAppeal = (SuspendAppeal) appeal;
            semesterLabel.setText("ภาคการศึกษา: " + suspendAppeal.getSemester());
            yearLabel.setText("ปีการศึกษา: " + suspendAppeal.getYear());
            subjectLabel.setText("รายวิชา: " + suspendAppeal.getSubjects());

            semesterLabel.setVisible(true);
            semesterLabel.setLayoutY(70);

            yearLabel.setVisible(true);
            yearLabel.setLayoutY(100);

            subjectLabel.setVisible(true);
            subjectLabel.setLayoutY(130);
        }
        else if (appeal.isBreakAppeal()) {
            BreakAppeal breakAppeal = (BreakAppeal) appeal;
            purposeLabel.setText("จุดประสงค์: " + breakAppeal.getPurpose());
            purposeLabel.setVisible(true);
            purposeLabel.setLayoutY(70);

            breakTimeLabel.setText("ระยะเวลา: " + breakAppeal.getStartDate() + " - " + breakAppeal.getEndDate());
            breakTimeLabel.setVisible(true);
            breakTimeLabel.setLayoutY(100);

            reasonLabel.setText(breakAppeal.getReason());
            reasonLabel.setVisible(true);
            reasonLabel.setLayoutY(130);

            subjectLabel.setText("รายวิชา: " + breakAppeal.getSubjects());
            subjectLabel.setVisible(true);
            subjectLabel.setLayoutY(160);
        }

        typeLabel.setText("ประเภทคำร้อง: " + appeal.getType());
        reasonLabel.setVisible(true);
        vbox.prefWidthProperty().bind(detailScrollPane.widthProperty().subtract(40));

        detailScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        detailScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        detailScrollPane.setContent(vbox);
        detailScrollPane.layout();

        presentStatusLabel.setText(appeal.getStatus());

        popupAppealPane.setVisible(true);
        appealManageLabel.setVisible(false);
        appealDetailsLabel.setVisible(true);
    }

    public void showTable(AppealList appealList) {
        TableColumn<Appeal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        dateColumn.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

        TableColumn<Appeal, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));


        TableColumn<Appeal, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        allAppealTable.getColumns().clear();
        allAppealTable.getColumns().add(dateColumn);
        allAppealTable.getColumns().add(ownerColumn);
        allAppealTable.getColumns().add(typeColumn);

        allAppealTable.getSortOrder().add(dateColumn);

        allAppealTable.getItems().clear();
        if (appealList != null) {
            for(Appeal appeal : appealList.getAppeals()){
                allAppealTable.getItems().add(appeal);
            }
        }
        allAppealTable.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);

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

        allAppealTable.getSelectionModel().select(null);

        selectedStatus = null;
    }

    public void getStatus(Event event) {
        selectedStatus = (String) statusChoiceBox.getValue();
    }
}
