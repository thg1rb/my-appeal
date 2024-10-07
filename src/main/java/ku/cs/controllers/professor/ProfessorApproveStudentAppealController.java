package ku.cs.controllers.professor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.ModifyDateListFileDatasource;
import ku.cs.services.exceptions.EmptyInputException;

import java.util.Date;

public class ProfessorApproveStudentAppealController {

    @FXML private AnchorPane mainPane;

    @FXML private VBox generalVBox;
    @FXML private VBox suspendVBox;
    @FXML private VBox breakVBox;

    @FXML private Label fullnameLabel;
    @FXML private Label idLabel;
    @FXML private Label typeLabel;
    @FXML private Label statusLabel;

    @FXML private TextArea topicGeneralTextArea;
    @FXML private TextArea detailsGeneralTextArea;

    @FXML private Label semesterSuspendLabel;
    @FXML private Label yearSuspendLabel;
    @FXML private TextArea reasonSuspendTextArea;
    @FXML private TextArea subjectsSuspendTextArea;

    @FXML private Label purposeBreakLabel;
    @FXML private Label startEndBreakLabel;
    @FXML private TextArea reasonBreakTextArea;
    @FXML private TextArea subjectsBreakTextArea;

    @FXML private Label createDateLabel;
    @FXML private Label advisorApproveDateLabel;
    @FXML private Label departmentApproveDateLabel;
    @FXML private Label facultyApproveDateLabel;

    @FXML private ImageView closePopUpImageView;
    @FXML private ImageView closePopUpDarkImageView;
    @FXML private Button closePopUpButton;
    @FXML private Button closePopUpDarkButton;

    @FXML private Pane rejectReasonAlertPane;
    @FXML private TextArea rejectReasonTextArea;
    @FXML private Label rejectReasonErrorLabel;

    private Appeal selectedAppeal;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    private Image defaultClosePopUpLightImage;
    private Image hoverClosePopUpLightImage;
    private Image defaultClosePopUpDarkImage;
    private Image hoverClosePopUpDarkImage;

    @FXML
    private void initialize() {
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);
    }

    // รับ parameters ที่ส่งมาจากหน้า ProfessorStudentAppealController
    public void setSelectedAppeal(Appeal selectedAppeal) {
        this.selectedAppeal = selectedAppeal;

        updateAppealDetails();
    }

    // กำหนดข้อมูลส่วนตัวของเจ้าของคำร้อง
    private void updateAppealDetails() {
        fullnameLabel.setText(selectedAppeal.getOwnerFullName());
        idLabel.setText(selectedAppeal.getOwnerId());
        typeLabel.setText(selectedAppeal.getType());
        String[] status = selectedAppeal.getStatus().split("\\|");
        statusLabel.setText(String.format("%30s\n%30s", status[0], status[1]));

        showAppealScrollPane(selectedAppeal.isGeneralAppeal(), selectedAppeal.isSuspendAppeal(), selectedAppeal.isBreakAppeal());
        initializeImageButton();
    }

    // แสดงรายละเอียดคำร้องตามประเภทของคำร้อง
    public void showAppealScrollPane(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        initializeLabel(isGeneralAppeal, isSuspendAppeal, isBreakAppeal);

        generalVBox.setVisible(isGeneralAppeal);
        suspendVBox.setVisible(isSuspendAppeal);
        breakVBox.setVisible(isBreakAppeal);
    }

    // กำหนดรูปภาพให้กับปุ่ม (รูปภาพของปุ่มปิดหน้าต่าง)
    private void initializeImageButton() {
        defaultClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        hoverClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());
        defaultClosePopUpDarkImage = new Image(getClass().getResource("/icons/close-pop-up-dark.png").toString());
        hoverClosePopUpDarkImage = new Image(getClass().getResource("/icons/close-pop-up-dark-hover.png").toString());

        closePopUpImageView.setImage(defaultClosePopUpLightImage);
        closePopUpDarkImageView.setImage(defaultClosePopUpDarkImage);

        closePopUpButton.setOnMouseEntered(mouseEvent -> closePopUpImageView.setImage(hoverClosePopUpLightImage));
        closePopUpButton.setOnMouseExited(mouseEvent -> closePopUpImageView.setImage(defaultClosePopUpLightImage));
        closePopUpDarkButton.setOnMouseEntered(mouseEvent -> closePopUpDarkImageView.setImage(hoverClosePopUpDarkImage));
        closePopUpDarkButton.setOnMouseExited(mouseEvent -> closePopUpDarkImageView.setImage(defaultClosePopUpDarkImage));
    }

    // กำหนดค่าให้กับ Label
    private void initializeLabel(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        // Label ของเวลา
        String createColor = "-fx-text-fill: #008f2f;";
        String pendingColor = "-fx-text-fill: #b57500;";
        createDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getCreateDate());
        createDateLabel.setStyle(createColor);

        advisorApproveDateLabel.setText("กำลังรอการอนุมัติ...");
        advisorApproveDateLabel.setStyle(pendingColor);

        departmentApproveDateLabel.setText("กำลังรอการอนุมัติ...");
        departmentApproveDateLabel.setStyle(pendingColor);

        facultyApproveDateLabel.setText("กำลังรอการอนุมัติ...");
        facultyApproveDateLabel.setStyle(pendingColor);

        // Label ของแต่ละคำร้อง
        if (isGeneralAppeal) {
            GeneralAppeal generalAppeal = (GeneralAppeal) selectedAppeal;
            topicGeneralTextArea.setText(generalAppeal.getTopic());
            detailsGeneralTextArea.setText(generalAppeal.getReason());
        } else if (isSuspendAppeal) {
            SuspendAppeal suspendAppeal = (SuspendAppeal) selectedAppeal;
            reasonSuspendTextArea.setText(suspendAppeal.getReason());
            semesterSuspendLabel.setText(suspendAppeal.getSemester());
            yearSuspendLabel.setText(suspendAppeal.getYear());
            subjectsSuspendTextArea.setText(suspendAppeal.getSubjects().replace('/', '\n'));
        } else if (isBreakAppeal) {
            BreakAppeal breakAppeal = (BreakAppeal) selectedAppeal;
            purposeBreakLabel.setText(breakAppeal.getPurpose());
            startEndBreakLabel.setText(breakAppeal.getStartDate() + " - " + breakAppeal.getEndDate());
            reasonBreakTextArea.setText(breakAppeal.getReason());
            subjectsBreakTextArea.setText(breakAppeal.getSubjects().replace('/', '\n'));
        }
    }

    // ปิดหน้าต่าง pop-up (ของหน้าแสดงรายละเอียดคำร้อง)
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
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
        rejectReasonAlertPane.setVisible(true);
    }

    // ปิด pop-up (ของหน้าระบุเหตุผลปฏิเสธคำร้อง)
    @FXML
    public void onCloseRejectReasonButtonClick(ActionEvent event) {
        rejectReasonAlertPane.setVisible(false);
    }

    // ยืนยันการปฏิเสธ (หลังจากระบุเหตุผลเรียบร้อย)
    public void onConfirmRejectReasonButton(ActionEvent event) {
        try {
            String rejectReason = rejectReasonTextArea.getText();
            if (rejectReason.isEmpty())
                throw new EmptyInputException();

            String modifyDate = DateTimeService.detailedDateToString(new Date());

            selectedAppeal.setModifyDate(modifyDate);
            selectedAppeal.setStatus("ปฏิเสธโดยอาจารย์ที่ปรึกษา | คำร้องถูกปฏิเสธ");
            selectedAppeal.setRejectedReason(rejectReason);

            modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setAdvisorApproveDate(modifyDate);

            rejectReasonAlertPane.setVisible(false);
            rejectReasonErrorLabel.setVisible(false);
            onCloseButtonClick(event);
        } catch (EmptyInputException e) {
            rejectReasonErrorLabel.setVisible(true);
        }
    }
}
