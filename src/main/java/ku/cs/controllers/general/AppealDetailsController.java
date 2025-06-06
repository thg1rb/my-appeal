package ku.cs.controllers.general;

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
import ku.cs.models.dates.ModifyDate;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ModifyDateListFileDatasource;
import ku.cs.services.fileutilities.SignFileDownloader;

public class AppealDetailsController {

    @FXML private AnchorPane mainPane;

    @FXML private VBox generalVBox;
    @FXML private VBox suspendVBox;
    @FXML private VBox breakVBox;

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

    @FXML private Button closePopUpLightButton;
    @FXML private ImageView closePopUpLightImageView;

    @FXML private Pane rejectedReasonAlertPane;
    @FXML private Label rejectedReasonTitleLabel;
    @FXML private TextArea rejectedReasonTextArea;

    private Appeal selectedAppeal;
    private ModifyDate selectedAppealDate;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    private Image defaultClosePopUpLightImage;
    private Image hoverClosePopUpLightImage;

    @FXML
    private void initialize() {
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        defaultClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        hoverClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());

        closePopUpLightImageView.setImage(defaultClosePopUpLightImage);

        closePopUpLightButton.setOnMouseEntered(mouseEvent -> closePopUpLightImageView.setImage(hoverClosePopUpLightImage));
        closePopUpLightButton.setOnMouseExited(mouseEvent -> closePopUpLightImageView.setImage(defaultClosePopUpLightImage));
    }

    // รับค่าที่ส่งมาจากหน้าหลักมากำหนดให้หน้าต่างนี้
    public void setSelectedAppeal(Appeal selectedAppeal, ModifyDate selectedAppealDate) {
        this.selectedAppeal = selectedAppeal;
        this.selectedAppealDate = selectedAppealDate;

        showAppealScrollPane(selectedAppeal.isGeneralAppeal(), selectedAppeal.isSuspendAppeal(), selectedAppeal.isBreakAppeal());
    }

    private void showAppealScrollPane(boolean isGeneralAppeal, boolean isSuspendAppeal, boolean isBreakAppeal) {
        initializeLabel(isGeneralAppeal, isSuspendAppeal, isBreakAppeal);

        generalVBox.setVisible(isGeneralAppeal);
        suspendVBox.setVisible(isSuspendAppeal);
        breakVBox.setVisible(isBreakAppeal);
    }

    private void initializeLabel(boolean isGeneralAppeal, boolean isSuspendAppeal, boolean isBreakAppeal) {
        String approveColor = "-fx-text-fill: #008f2f;";
        String rejectColor = "-fx-text-fill: #cc0000;";
        String pendingColor = "-fx-text-fill: #b57500;";

        String downloadSignature = "ดาวน์โหลดเอกสาร";
        String viewRejectedReason = "ดูเหตุผลในการปฏิเสธ";

        createDateLabel.setText(selectedAppealDate.getCreateDate());
        createDateLabel.setStyle(approveColor);

        advisorApproveDateLabel.setOnMouseClicked(null);
        departmentApproveDateLabel.setOnMouseClicked(null);
        facultyApproveDateLabel.setOnMouseClicked(null);

        if (selectedAppealDate.isAdvisorRejected(selectedAppeal.getStatus())) {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(rejectColor + "-fx-cursor: hand;");
            advisorApproveDateLabel.setOnMouseEntered(mouseEvent -> advisorApproveDateLabel.setText(viewRejectedReason));
            advisorApproveDateLabel.setOnMouseExited(mouseEvent -> advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate()));
            rejectedReasonTitleLabel.setText("เหตุผลในการปฏิเสธคำร้องของอาจารย์ที่ปรึกษา");
            advisorApproveDateLabel.setOnMouseClicked(mouseEvent -> onShowRejectedReasonAlertPane());

            departmentApproveDateLabel.setText("คำร้องถูกปฏิเสธ");
            departmentApproveDateLabel.setStyle(rejectColor);

            facultyApproveDateLabel.setText("คำร้องถูกปฏิเสธ");
            facultyApproveDateLabel.setStyle(rejectColor);

        } else if (selectedAppealDate.isDepartmentRejected(selectedAppeal.getStatus())) {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(approveColor);

            departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(rejectColor + "-fx-cursor: hand;");
            departmentApproveDateLabel.setOnMouseEntered(mouseEvent -> departmentApproveDateLabel.setText(viewRejectedReason));
            departmentApproveDateLabel.setOnMouseExited(mouseEvent -> departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate()));
            rejectedReasonTitleLabel.setText("เหตุผลในการปฏิเสธคำร้องของหัวหน้าภาค");
            departmentApproveDateLabel.setOnMouseClicked(mouseEvent -> onShowRejectedReasonAlertPane());

            facultyApproveDateLabel.setText("คำร้องถูกปฏิเสธ");
            facultyApproveDateLabel.setStyle(rejectColor);
        } else if (selectedAppealDate.isFacultyRejected(selectedAppeal.getStatus())) {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(approveColor);

            departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(approveColor);

            facultyApproveDateLabel.setText(selectedAppealDate.getFacultyApproveDate());
            facultyApproveDateLabel.setOnMouseEntered(mouseEvent -> facultyApproveDateLabel.setText(viewRejectedReason));
            facultyApproveDateLabel.setOnMouseExited(mouseEvent -> facultyApproveDateLabel.setText(selectedAppealDate.getFacultyApproveDate()));
            facultyApproveDateLabel.setStyle(rejectColor + "-fx-cursor: hand;");
            rejectedReasonTitleLabel.setText("เหตุผลในการปฏิเสธคำร้องของหัวหน้าคณะ");
            facultyApproveDateLabel.setOnMouseClicked(mouseEvent -> onShowRejectedReasonAlertPane());
        } else if (selectedAppealDate.isAdvisorPending()) {
            advisorApproveDateLabel.setText("กำลังรอการดำเนินการ...");
            advisorApproveDateLabel.setStyle(pendingColor);

            departmentApproveDateLabel.setText("กำลังรอการดำเนินการ...");
            departmentApproveDateLabel.setStyle(pendingColor);

            facultyApproveDateLabel.setText("กำลังรอการดำเนินการ...");
            facultyApproveDateLabel.setStyle(pendingColor);
        } else if (selectedAppealDate.isDepartmentPending()) {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(approveColor);

            departmentApproveDateLabel.setText("กำลังรอการดำเนินการ...");
            departmentApproveDateLabel.setStyle(pendingColor);

            facultyApproveDateLabel.setText("กำลังรอการดำเนินการ...");
            facultyApproveDateLabel.setStyle(pendingColor);
        } else if (selectedAppealDate.isFacultyPending()) {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(approveColor);

            departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(approveColor + "-fx-cursor: hand;");
            departmentApproveDateLabel.setOnMouseEntered(mouseEvent -> departmentApproveDateLabel.setText(downloadSignature));
            departmentApproveDateLabel.setOnMouseExited(mouseEvent -> departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate()));
            departmentApproveDateLabel.setOnMouseClicked(mouseEvent -> downloadApproveSignature(true));

            if (selectedAppeal.getStatus().contains("คำร้องดำเนินการครบถ้วน")) {
                facultyApproveDateLabel.setText("ดำเนินการครบถ้วน");
                facultyApproveDateLabel.setStyle(approveColor);
            } else {
                facultyApproveDateLabel.setText("กำลังรอการดำเนินการ...");
                facultyApproveDateLabel.setStyle(pendingColor);
            }
        } else {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(approveColor);

            departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(approveColor + "-fx-cursor: hand;");
            departmentApproveDateLabel.setOnMouseEntered(mouseEvent -> departmentApproveDateLabel.setText(downloadSignature));
            departmentApproveDateLabel.setOnMouseExited(mouseEvent -> departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate()));
            departmentApproveDateLabel.setOnMouseClicked(mouseEvent -> downloadApproveSignature(true));

            facultyApproveDateLabel.setText(selectedAppealDate.getFacultyApproveDate());
            facultyApproveDateLabel.setStyle(approveColor + "-fx-cursor: hand;");
            facultyApproveDateLabel.setOnMouseEntered(mouseEvent -> facultyApproveDateLabel.setText(downloadSignature));
            facultyApproveDateLabel.setOnMouseExited(mouseEvent -> facultyApproveDateLabel.setText(selectedAppealDate.getFacultyApproveDate()));
            facultyApproveDateLabel.setOnMouseClicked(mouseEvent -> downloadApproveSignature(false));
        }

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

    // แสดงหน้าต่างเหตุผลในการปฏิเสธคำร้อง
    private void onShowRejectedReasonAlertPane() {
        rejectedReasonTextArea.setText(selectedAppeal.getRejectedReason());
        rejectedReasonAlertPane.setVisible(true);
    }

    // ดาวน์โหลดไฟล์ลงนาม PDF
    private void downloadApproveSignature(boolean isDepartment) {
        SignFileDownloader downloader = new SignFileDownloader(selectedAppeal);
        downloader.download((Stage) mainPane.getScene().getWindow(), isDepartment);
    }

    // ปิดหน้าต่างสาเหตุในการปฏิเสธคำร้อง
    @FXML
    private void onCloseAlertPane() {
        rejectedReasonAlertPane.setVisible(false);
    }

    // ปิดหน้าต่างของหน้าปัจจุบัน
    @FXML
    private void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
