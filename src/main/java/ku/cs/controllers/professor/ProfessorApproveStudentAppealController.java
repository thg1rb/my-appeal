package ku.cs.controllers.professor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.util.Date;

public class ProfessorApproveStudentAppealController {

    @FXML private ScrollPane generalAppealScrollPane;
    @FXML private ScrollPane suspendAppealScrollPane;
    @FXML private ScrollPane breakAppealScrollPane;

    @FXML private Label fullnameLabel;
    @FXML private Label idLabel;
    @FXML private Label typeLabel;
    @FXML private Label statusLabel;

    @FXML private Label topicGeneralLabel;
    @FXML private Label detailsGeneralLabel;
    @FXML private Label reasonSuspendLabel;
    @FXML private Label semesterSuspendLabel;
    @FXML private Label yearSuspendLabel;
    @FXML private Label subjectsSuspendLabel;

    @FXML private Label purposeBreakLabel;
    @FXML private Label startEndBreakLabel;
    @FXML private Label reasonBreakLabel;
    @FXML private Label subjectsBreakLabel;

    @FXML private Label createDateLabel;
    @FXML private Label advisorApproveDateLabel;
    @FXML private Label departmentApproveDateLabel;
    @FXML private Label facultyApproveDateLabel;

    @FXML private ImageView closePopUpImageView;
    @FXML private ImageView closePopUpDarkImageView;
    @FXML private Button closePopUpButton;
    @FXML private Button closePopUpDarkButton;

    @FXML private Pane rejectReasonAlertPane;

    private Appeal selectedAppeal;
    private Datasource<AppealList> appealListDatasource;
    private AppealList appealList;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    @FXML
    private void initialize() {
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        // ปุ่มปิดหน้าต่าง
        Image defaultClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        Image hoverClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());
        Image defaultClosePopUpDarkImage = new Image(getClass().getResource("/icons/close-pop-up-dark.png").toString());
        Image hoverClosePopUpDarkImage = new Image(getClass().getResource("/icons/close-pop-up-dark-hover.png").toString());

        closePopUpImageView.setImage(defaultClosePopUpLightImage);
        closePopUpDarkImageView.setImage(defaultClosePopUpDarkImage);

        closePopUpButton.setOnMouseEntered(mouseEvent -> closePopUpImageView.setImage(hoverClosePopUpLightImage));
        closePopUpButton.setOnMouseExited(mouseEvent -> closePopUpImageView.setImage(defaultClosePopUpLightImage));
        closePopUpDarkButton.setOnMouseEntered(mouseEvent -> closePopUpDarkImageView.setImage(hoverClosePopUpDarkImage));
        closePopUpDarkButton.setOnMouseExited(mouseEvent -> closePopUpDarkImageView.setImage(defaultClosePopUpDarkImage));
    }

    // รับ parameters ที่ส่งมาจากหน้า ProfessorStudentAppealController
    public void setSelectedAppeal(Appeal selectedAppeal, AppealList appealList, Datasource<AppealList> datasource) {
        this.selectedAppeal = selectedAppeal;
        this.appealList = appealList;
        this.appealListDatasource = datasource;

        updateAppealDetails();
    }

    private void updateAppealDetails() {
        fullnameLabel.setText(selectedAppeal.getOwnerFullName());
        idLabel.setText(selectedAppeal.getOwnerId());
        typeLabel.setText(selectedAppeal.getType());
        String[] status = selectedAppeal.getStatus().split("\\|");
        statusLabel.setText(String.format("%30s\n%30s", status[0], status[1]));

        showAppealPane(selectedAppeal.isGeneralAppeal(), selectedAppeal.isSuspendAppeal(), selectedAppeal.isBreakAppeal());
    }

    // แสดงรายละเอียดคำร้องตามประเภทของคำร้อง
    public void showAppealPane(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        initializeLabel(isGeneralAppeal, isSuspendAppeal, isBreakAppeal);
        generalAppealScrollPane.setVisible(isGeneralAppeal);
        suspendAppealScrollPane.setVisible(isSuspendAppeal);
        breakAppealScrollPane.setVisible(isBreakAppeal);
    }

    // กำหนดค่าให้กับ Label
    private void initializeLabel(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        // Label ของเวลา
        createDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getCreateDate());
        createDateLabel.setStyle("-fx-text-fill: #008f2f;");

        advisorApproveDateLabel.setText("กำลังรอการอนุมัติ...");
        advisorApproveDateLabel.setStyle("-fx-text-fill: #b57500;");

        departmentApproveDateLabel.setText("กำลังรอการอนุมัติ...");
        departmentApproveDateLabel.setStyle("-fx-text-fill: #b57500;");

        facultyApproveDateLabel.setText("กำลังรอการอนุมัติ...");
        facultyApproveDateLabel.setStyle("-fx-text-fill: #b57500;");

        // Label ของแต่ละคำร้อง
        if (isGeneralAppeal) {
            GeneralAppeal generalAppeal = (GeneralAppeal) selectedAppeal;
            topicGeneralLabel.setText(generalAppeal.getTopic() + "\n\n\n");
            detailsGeneralLabel.setText(generalAppeal.getReason());
        } else if (isSuspendAppeal) {
            SuspendAppeal suspendAppeal = (SuspendAppeal) selectedAppeal;
            reasonSuspendLabel.setText(suspendAppeal.getReason() + "\n\n\n");
            semesterSuspendLabel.setText(suspendAppeal.getSemester());
            yearSuspendLabel.setText(suspendAppeal.getYear() + "\n\n\n");
            subjectsSuspendLabel.setText(suspendAppeal.getSubjects());
        } else if (isBreakAppeal) {
            BreakAppeal breakAppeal = (BreakAppeal) selectedAppeal;
            reasonBreakLabel.setText(breakAppeal.getReason() + "\n\n\n");
            purposeBreakLabel.setText(breakAppeal.getPurpose());
            startEndBreakLabel.setText(breakAppeal.getStartDate() + " - " + breakAppeal.getEndDate() + "\n\n\n");
            subjectsBreakLabel.setText(breakAppeal.getSubjects());
        }
    }

    //
    private void showRejectReasonPane() {
        rejectReasonAlertPane.setVisible(true);
    }

    @FXML
    public void onCloseRejectReasonButtonClick(ActionEvent event) {
//        if ()
        rejectReasonAlertPane.setVisible(false);
    }

    // ปิดหน้าต่าง pop-up
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // อนุมัติคำร้องเพื่อส่งต่อให้หัวหน้าภาควิชา
    @FXML
    public void onApproveButtonClick(ActionEvent event) {
        String modifyDate = DateTimeService.detailedDateToString(new Date());

        selectedAppeal.setModifyDate(modifyDate);
        selectedAppeal.setStatus("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา");

        modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setAdvisorApproveDate(modifyDate);

        onCloseButtonClick(event);
    }

    // ปฏิเสธคำร้อง
    @FXML
    public void onRejectButtonClick(ActionEvent event) {

//        String modifyDate = DateTimeService.detailedDateToString(new Date());
//
//        selectedAppeal.setModifyDate(modifyDate);
//        selectedAppeal.setStatus("ปฏิเสธโดยอาจารย์ที่ปรึกษา | คำร้องถูกปฏิเสธ");
//
//        modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setAdvisorApproveDate(modifyDate);
//
//        onCloseButtonClick(event);
        showRejectReasonPane();
    }

}
