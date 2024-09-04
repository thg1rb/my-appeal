package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.models.appeal.Appeal;
import ku.cs.models.persons.User;

public class AppealEditController {
    @FXML private Label typeLabel;
    @FXML private Label topicLabel;
    @FXML private Label semesterLabel;
    @FXML private Label yearLabel;
    @FXML private Label subjectLabel;
    @FXML private Label breakTimeLabel;
    @FXML private Label purposeLabel;
    @FXML private Label reasonLabel;
    @FXML private Label presentStatusLabel;
    @FXML private VBox vbox;
    @FXML private ScrollPane reasonScrollPane;
    @FXML private ChoiceBox statusChoiceBox;



    private Appeal appeal;
    private String selectedStatus;
    private String[] majorStatusList = {"ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน", "ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ"};
    private String[] facultyStatusList = {};
    private String role;

    public void initialize() {

        vbox.prefWidthProperty().bind(reasonScrollPane.widthProperty().subtract(40));
        reasonScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        reasonScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        reasonScrollPane.setContent(vbox);
        reasonScrollPane.layout();

    }

    public void getStatus(Event event) {
        selectedStatus = (String) statusChoiceBox.getValue();
    }

    public void setType(String type, Appeal appeal, User user){
        this.role = user.getRole();
        this.appeal = appeal;
        typeLabel.setText(type);
        reasonLabel.setText(appeal.getReason());
        presentStatusLabel.setText(appeal.getStatus());
        if(role.equals("เจ้าหน้าที่ภาควิชา")){
            statusChoiceBox.getItems().addAll(majorStatusList);
            statusChoiceBox.setOnAction(this::getStatus);
            statusChoiceBox.setValue(majorStatusList[0]);
        }

        if(type.equals("คำร้องทั่วไป")){
            purposeLabel.setVisible(false);
            breakTimeLabel.setVisible(false);
            yearLabel.setVisible(false);
            subjectLabel.setVisible(false);
            semesterLabel.setVisible(false);
            subjectLabel.setVisible(false);

            topicLabel.setVisible(true);
            topicLabel.setText("เรื่อง: " + appeal.getTopic());
        }
        else if(type.equals("คำร้องขอพักการศึกษา")){
            topicLabel.setVisible(false);
            purposeLabel.setVisible(false);
            breakTimeLabel.setVisible(false);

            semesterLabel.setVisible(true);
            semesterLabel.setText("ภาคการศึกษา: " + appeal.getSemester());

            yearLabel.setVisible(true);
            yearLabel.setText("ปีการศึกษา" + appeal.getYear());

            subjectLabel.setVisible(true);
            subjectLabel.setText("รายวิชาที่ต้องการลา: " + appeal.getSubjects());
        }
        else if(type.equals("คำร้องขอลาป่วยหรือลากิจ")){
            topicLabel.setVisible(false);
            semesterLabel.setVisible(false);
            yearLabel.setVisible(false);

            purposeLabel.setVisible(true);
            purposeLabel.setText("เหตุผล: " + appeal.getPurpose());

            breakTimeLabel.setVisible(true);
            breakTimeLabel.setText("ระยะเวลา: " + appeal.getStartDate()+"-"+appeal.getEndDate());

            subjectLabel.setVisible(true);
            subjectLabel.setText("รายวิชาที่ต้องการลา: " + appeal.getSubjects());
        }
    }

    public void onConfirmButtonClick(ActionEvent event){
        appeal.setStatus(selectedStatus);
        selectedStatus = null;
        onCancleButtonClick(event);
    }

    public void onCancleButtonClick(ActionEvent event){
        selectedStatus = null;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
