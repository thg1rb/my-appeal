package ku.cs.controllers.general;

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
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.dates.ModifyDate;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

public class AppealDetailsController {

    @FXML private ScrollPane generalScrollPane;
    @FXML private ScrollPane suspendScrollPane;
    @FXML private ScrollPane breakScrollPane;

    @FXML private Label topicGeneralLabel;
    @FXML private Label detailsGeneralLabel;

    @FXML private Label semesterSuspendLabel;
    @FXML private Label yearSuspendLabel;
    @FXML private Label reasonSuspendLabel;
    @FXML private Label subjectsSuspendLabel;

    @FXML private Label purposeBreakLabel;
    @FXML private Label startEndBreakLabel;
    @FXML private Label reasonBreakLabel;
    @FXML private Label subjectsBreakLabel;

    @FXML private Label createDateLabel;
    @FXML private Label advisorApproveTitleLabel;
    @FXML private Label advisorApproveDateLabel;
    @FXML private Label departmentApproveTitleLabel;
    @FXML private Label departmentApproveDateLabel;
    @FXML private Label facultyApproveTitleLabel;
    @FXML private Label facultyApproveDateLabel;

    @FXML private Button closePopUpLightButton;
    @FXML private ImageView closePopUpLightImageView;

    @FXML private Pane rejectedReasonAlertPane;
    @FXML private ScrollPane rejectedReasonScrollPane;
    @FXML private Label rejectedReasonTitleLabel;
    @FXML private Label rejectedReasonLabel;

    private Appeal selectedAppeal;
    private ModifyDate selectedAppealDate;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    private Image defaultClosePopUpLightImage;
    private Image hoverClosePopUpLightImage;

    @FXML
    private void initialize() {
        defaultClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        hoverClosePopUpLightImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());

        closePopUpLightImageView.setImage(defaultClosePopUpLightImage);

        closePopUpLightButton.setOnMouseEntered(mouseEvent -> closePopUpLightImageView.setImage(hoverClosePopUpLightImage));
        closePopUpLightButton.setOnMouseExited(mouseEvent -> closePopUpLightImageView.setImage(defaultClosePopUpLightImage));

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();
    }

    //
    public void setSelectedAppeal(Appeal selectedAppeal, ModifyDate selectedAppealDate) {
        this.selectedAppeal = selectedAppeal;
        this.selectedAppealDate = selectedAppealDate;

        showAppealScrollPane(selectedAppeal.isGeneralAppeal(), selectedAppeal.isSuspendAppeal(), selectedAppeal.isBreakAppeal());
    }

    private void showAppealScrollPane(boolean isGeneralAppeal, boolean isSuspendAppeal, boolean isBreakAppeal) {
        initializeLabel(isGeneralAppeal, isSuspendAppeal, isBreakAppeal);

        ScrollPane.ScrollBarPolicy never = ScrollPane.ScrollBarPolicy.NEVER;
        generalScrollPane.setHbarPolicy(never);
        suspendScrollPane.setHbarPolicy(never);
        breakScrollPane.setHbarPolicy(never);

        generalScrollPane.setVisible(isGeneralAppeal);
        suspendScrollPane.setVisible(isSuspendAppeal);
        breakScrollPane.setVisible(isBreakAppeal);
    }

    private void initializeLabel(boolean isGeneralAppeal, boolean isSuspendAppeal, boolean isBreakAppeal) {
        String approveColor = "-fx-text-fill: #008f2f;";
        String rejectColor = "-fx-text-fill: #cc0000;";
        String pendingColor = "-fx-text-fill: #b57500;";

        createDateLabel.setText(selectedAppealDate.getCreateDate());
        createDateLabel.setStyle(approveColor);

        advisorApproveDateLabel.setOnMouseClicked(null);
        departmentApproveDateLabel.setOnMouseClicked(null);
        facultyApproveDateLabel.setOnMouseClicked(null);

        if (selectedAppealDate.isAdvisorRejected(selectedAppeal.getStatus())) {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(rejectColor);
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
            departmentApproveDateLabel.setStyle(rejectColor);
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
            facultyApproveDateLabel.setStyle(rejectColor);
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
            departmentApproveDateLabel.setStyle(approveColor);
            facultyApproveDateLabel.setText("กำลังรอการดำเนินการ...");
            facultyApproveDateLabel.setStyle(pendingColor);
        } else {
            advisorApproveDateLabel.setText(selectedAppealDate.getAdvisorApproveDate());
            advisorApproveDateLabel.setStyle(approveColor);
            departmentApproveDateLabel.setText(selectedAppealDate.getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(approveColor);
            facultyApproveDateLabel.setText(selectedAppealDate.getFacultyApproveDate());
            facultyApproveDateLabel.setStyle(approveColor);
        }

        //
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
    private void onShowRejectedReasonAlertPane() {
        rejectedReasonScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rejectedReasonLabel.setText(selectedAppeal.getRejectedReason());
        rejectedReasonAlertPane.setVisible(true);
    }

    //
    @FXML
    private void onCloseRejectedReasonButton() {
        rejectedReasonAlertPane.setVisible(false);
    }

    //
    @FXML
    private void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

}
