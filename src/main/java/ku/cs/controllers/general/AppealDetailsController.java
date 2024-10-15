package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

import java.io.File;

public class AppealDetailsController {

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

    @FXML private Button closePopUpLightButton;
    @FXML private ImageView closePopUpLightImageView;

    @FXML private Pane rejectedReasonAlertPane;
    @FXML private Label rejectedReasonTitleLabel;
    @FXML private TextArea rejectedReasonTextArea;

    @FXML private Pane approveSignatureAlertPane;
    @FXML private Label approveSignatureTitleLabel;
    @FXML private ImageView approveSignatureImageView;

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

    //
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
            System.out.println(selectedAppeal.getDepartmentSignature());

            departmentApproveDateLabel.setOnMouseClicked(mouseEvent -> onShowApproveSignatureAlertPane("คำร้องอนุมัติโดยเจ้าหน้าที่ภาควิชา", selectedAppeal.getDepartmentSignature()));

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
            departmentApproveDateLabel.setStyle(approveColor);
            departmentApproveDateLabel.setOnMouseClicked(mouseEvent -> onShowApproveSignatureAlertPane("คำร้องอนุมัติโดยเจ้าหน้าที่ภาควิชา", selectedAppeal.getDepartmentSignature()));

            facultyApproveDateLabel.setText(selectedAppealDate.getFacultyApproveDate());
            facultyApproveDateLabel.setStyle(approveColor);
            facultyApproveDateLabel.setOnMouseClicked(mouseEvent -> onShowApproveSignatureAlertPane("คำร้องอนุมัติโดยเจ้าหน้าที่คณะ", selectedAppeal.getFacultySignature()));
        }

        //
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

    //
    private void onShowRejectedReasonAlertPane() {
        rejectedReasonTextArea.setText(selectedAppeal.getRejectedReason());
        rejectedReasonAlertPane.setVisible(true);
    }

    private void onShowApproveSignatureAlertPane(String approveSignatureTitle, String path) {
        approveSignatureTitleLabel.setText(approveSignatureTitle);

        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        approveSignatureImageView.setImage(image);

        approveSignatureAlertPane.setVisible(true);
    }

    //
    @FXML
    private void onCloseAlertPane() {
        rejectedReasonAlertPane.setVisible(false);
        approveSignatureAlertPane.setVisible(false);
    }

    //
    @FXML
    private void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

}
