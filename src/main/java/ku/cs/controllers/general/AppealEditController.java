package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.dates.ModifyDate;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.ModifyDateListFileDatasource;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.IOException;
import java.util.Date;

public class AppealEditController {
    @FXML private AnchorPane mainPane;

    @FXML private ScrollPane generalAppealScrollPane;
    @FXML private ScrollPane suspendAppealScrollPane;
    @FXML private ScrollPane breakAppealScrollPane;

    @FXML private Label topicLabel;
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
    @FXML private TextArea rejectReasonTextArea;
    @FXML private Label rejectReasonErrorLabel;

    @FXML private Button confirmButton;
    @FXML private Button rejectButton;


    private Appeal selectedAppeal;
    private User staff;
    private String role;
    private String selectedStatus;

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
    }

    // รับ parameters ที่ส่งมาจากหน้า ProfessorStudentAppealController
    public void setSelectedAppeal(Appeal selectedAppeal) {
        this.selectedAppeal = selectedAppeal;
        updateAppealDetails();
    }
    // set ตำแหน่งของผู้ใช้
    public void setRole(User user){
        this.staff = user;
        role = user.getRole();
    }

    public void setMode (boolean mode) {
        if(mode) {
            topicLabel.setText("รายละเอียดคำร้อง");
            confirmButton.setVisible(false);
            rejectButton.setVisible(false);
        }
        else {
            topicLabel.setText("อนุมัติหรือปฏิเสธคำร้องของนิสิต");
            confirmButton.setVisible(true);
            rejectButton.setVisible(true);
        }
    }

    // show accept popup
    void showAcceptPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/accept-appeal-popup.fxml"));
            Parent root = fxmlLoader.load();
            AcceptAppealController controller = fxmlLoader.getController();
            GaussianBlur blur = new GaussianBlur(10);
            controller.setVar(staff, role, selectedStatus, selectedAppeal);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            mainPane.setEffect(blur);
            stage.showAndWait();
            mainPane.setEffect(null);
            modifyDateList = modifyDateListDatasource.readData();

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // กำหนดข้อมูลส่วนตัวของเจ้าของคำร้อง
    private void updateAppealDetails() {
        fullnameLabel.setText(selectedAppeal.getOwnerFullName());
        idLabel.setText(selectedAppeal.getOwnerId());
        typeLabel.setText(selectedAppeal.getType());
        String[] status = selectedAppeal.getStatus().split("\\|");
        statusLabel.setText(String.format("%30s\n%30s", status[0], status[1]));

        showAppealPane(selectedAppeal.isGeneralAppeal(), selectedAppeal.isSuspendAppeal(), selectedAppeal.isBreakAppeal());
        initializeImageButton();
    }

    // แสดงรายละเอียดคำร้องตามประเภทของคำร้อง
    public void showAppealPane(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        initializeLabel(isGeneralAppeal, isSuspendAppeal, isBreakAppeal);
        generalAppealScrollPane.setVisible(isGeneralAppeal);
        suspendAppealScrollPane.setVisible(isSuspendAppeal);
        breakAppealScrollPane.setVisible(isBreakAppeal);
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
        String rejectColor = "-fx-text-fill: #cc0000;";
        createDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getCreateDate());
        createDateLabel.setStyle(createColor);

        advisorApproveDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getAdvisorApproveDate());
        advisorApproveDateLabel.setStyle(createColor);

        if (selectedAppeal.getStatus().equals("ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ")) {
            departmentApproveDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(rejectColor);

            facultyApproveDateLabel.setText("คำร้องถูกปฏิเสธ");
            facultyApproveDateLabel.setStyle(rejectColor);
        }
        else if (selectedAppeal.getStatus().equals("ปฏิเสธโดยคณบดี | คำร้องถูกปฏิเสธ")) {
            departmentApproveDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getDepartmentApproveDate());
            departmentApproveDateLabel.setStyle(createColor);

            facultyApproveDateLabel.setText("คำร้องถูกปฏิเสธ");
            facultyApproveDateLabel.setStyle(rejectColor);
        }
        else {
            if (modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getDepartmentApproveDate() != null) {
                departmentApproveDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getDepartmentApproveDate());
                departmentApproveDateLabel.setStyle(createColor);
            } else {
                departmentApproveDateLabel.setText("กำลังรอการอนุมัติ...");
                departmentApproveDateLabel.setStyle(pendingColor);
            }

            if (modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getFacultyApproveDate() != null) {
                facultyApproveDateLabel.setText(modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).getFacultyApproveDate());
                facultyApproveDateLabel.setStyle(createColor);
            } else {
                facultyApproveDateLabel.setText("กำลังรอการอนุมัติ...");
                facultyApproveDateLabel.setStyle(pendingColor);
            }
        }
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

    // ปิดหน้าต่าง pop-up (ของหน้าแสดงรายละเอียดคำร้อง)
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        modifyDateListDatasource.writeData(modifyDateList);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onConfirmButtonClick(ActionEvent event) {
        if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            selectedStatus = "อนุมัติโดยหัวหน้าภาควิชา";
        }
        else if (role.equals("เจ้าหน้าที่คณะ")) {
            selectedStatus = "อนุมัติโดยคณบดี";
        }
        showAcceptPopup();

        onCloseButtonClick(event);
    }

    // ปิด pop-up (ของหน้าระบุเหตุผลปฏิเสธคำร้อง)
    @FXML
    public void onCloseRejectReasonButtonClick(ActionEvent event) {
        rejectReasonAlertPane.setVisible(false);
    }
    @FXML
    public void onRejectButtonClick(ActionEvent event) {
        rejectReasonAlertPane.setVisible(true); // Ensure this is executed
    }

    // ยืนยันการปฏิเสธ (หลังจากระบุเหตุผลเรียบร้อย)
    @FXML
    public void onConfirmRejectReasonButton(ActionEvent event) {
        if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            selectedStatus = "ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ";
        }
        else if (role.equals("เจ้าหน้าที่คณะ")) {
            selectedStatus = "ปฏิเสธโดยคณบดี | คำร้องถูกปฏิเสธ";
        }
        try {
            String rejectReason = rejectReasonTextArea.getText();
            if (rejectReason.isEmpty())
                throw new EmptyInputException();

            String modifyDate = DateTimeService.detailedDateToString(new Date());

            selectedAppeal.setModifyDate(modifyDate);
            selectedAppeal.setStatus(selectedStatus);
            selectedAppeal.setRejectedReason(rejectReason);
            if (role.equals("เจ้าหน้าที่ภาควิชา")){
                modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setDepartmentApproveDate(modifyDate);
            }
            else if (role.equals("เจ้าหน้าที่คณะ")){
                modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setFacultyApproveDate(modifyDate);
            }

            rejectReasonAlertPane.setVisible(false);
            rejectReasonErrorLabel.setVisible(false);
            onCloseButtonClick(event);

        } catch (EmptyInputException e) {
            rejectReasonErrorLabel.setVisible(true);
        }
    }
}
