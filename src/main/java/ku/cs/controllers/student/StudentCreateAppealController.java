package ku.cs.controllers.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentCreateAppealController {

    @FXML private Circle profileImageCircle;

    // Appeal
    @FXML private ChoiceBox<String> appealChoiceBox;
    private String[] appeals = {"ทั่วไป", "ขอลาออก", "ขอพักการศึกษา", "ลาป่วยหรือลากิจ", "ขอผ่อนผันการชำระ"};
    String selectedAppeal;

    // ประกาศตัวแปรคำร้องทั่วไป
    @FXML private Pane generalAppealPane;
    @FXML private TextArea topicTextArea;
    @FXML private TextArea detailsTextArea;

    // ประกาศตัวแปรคำร้องขอลาออก
    @FXML private Pane retireAppealPane;
    @FXML private ChoiceBox<String> tcasChoiceBox;
    private String[] tcas = {"รอบที่ 1", "รอบที่ 2", "รอบที่ 3", "รอบที่ 4", "อื่นๆ"};
    String selectedTcas;
    @FXML private TextField gpaTextField;
    @FXML private TextArea purposeRetireTextArea;

    // ประกาศตัวแปรคำร้องขอพักการศึกษา
    @FXML private Pane suspendAppealPane;
    @FXML private TextArea reasonSuspendTextArea;
    @FXML private TextArea subjectsSuspendTextArea;
    @FXML private ChoiceBox<String> semestersSuspendChoiceBox;
    private String[] semesters = {"ภาคต้น", "ภาคปลาย", "ภาคฤดูร้อน"};
    String selectedSemester;
    @FXML private ChoiceBox<String> yearsSuspendChoiceBox;
    private String[] years = {"2565", "2566", "2567", "2568", "2569", "2570", "อื่นๆ"};
    String selectedYear;

    // ประกาศตัวแปรคำร้องขอลาป่วยหรือลากิจ
    @FXML private Pane breakAppealPane;
    @FXML private ChoiceBox<String> purposesBreakChoiceBox;
    private String[] purposes = {"ลาป่วย", "ลากิจ"};
    String selectedPurpose;
    @FXML private TextArea reasonBreakTextArea;
    @FXML private TextArea subjectsBreakTextArea;
    @FXML private DatePicker startBreakDatePicker;
    @FXML private DatePicker endBreakDatePicker;

    // ประกาศตัวแปรคำร้องขอผ่อนชำระ
    @FXML private Pane installmentAppealPane;
    @FXML private TextArea reasonInstallmentTextArea;
    @FXML private ChoiceBox<String> semestersInstallmentChoiceBox;
    @FXML private ChoiceBox<String> yearsInstallmentChoiceBox;

    //  ประกาศตัวแปรคำเตือน (ใส่ข้อมูลไม่ครบถ้วน)
    @FXML private Pane backgroundAlertPane;
    @FXML private Pane alertPane;

    @FXML
    public void initialize() {
        // แสดงโปรไฟล์ผู้ใช้งาน
        Image profileImage = new Image(getClass().getResource("/images/student-profile.jpeg").toString());
        profileImageCircle.setFill(new ImagePattern(profileImage));

        initializeChoiceBox();
    }

    // รับค่าจาก ChoiceBox
    private void getAppealType(ActionEvent actionEvent) {
        selectedAppeal = appealChoiceBox.getValue();

        if (selectedAppeal.equals("ทั่วไป")) {
            generalAppealPane.setVisible(true);
            retireAppealPane.setVisible(false);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(false);
            installmentAppealPane.setVisible(false);
        }
        else if (selectedAppeal.equals("ขอลาออก")) {
            generalAppealPane.setVisible(false);
            retireAppealPane.setVisible(true);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(false);
            installmentAppealPane.setVisible(false);
        }
        else if (selectedAppeal.equals("ขอพักการศึกษา")) {
            generalAppealPane.setVisible(false);
            retireAppealPane.setVisible(false);
            suspendAppealPane.setVisible(true);
            breakAppealPane.setVisible(false);
            installmentAppealPane.setVisible(false);
        }
        else if (selectedAppeal.equals("ลาป่วยหรือลากิจ")) {
            generalAppealPane.setVisible(false);
            retireAppealPane.setVisible(false);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(true);
            installmentAppealPane.setVisible(false);
        }
        else if (selectedAppeal.equals("ขอผ่อนผันการชำระ")) {
            generalAppealPane.setVisible(false);
            retireAppealPane.setVisible(false);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(false);
            installmentAppealPane.setVisible(true);
        }

    }

    private void getTcas(ActionEvent actionEvent) {
        selectedTcas = tcasChoiceBox.getValue();
    }

    private void getSemesters(ActionEvent actionEvent) {
        selectedSemester = yearsSuspendChoiceBox.getValue();
    }

    private void getYears(ActionEvent actionEvent) {
        selectedYear = yearsSuspendChoiceBox.getValue();
    }

    private void getPurpose(ActionEvent actionEvent) {
        selectedPurpose = purposesBreakChoiceBox.getValue();
    }

    // แสดงและกำหนดค่าเริ่มต้น ChoiceBox
    private void initializeChoiceBox() {
        // ChoiceBox เลือกประเภทของคำร้อง
        selectedAppeal = "ทั่วไป";
        generalAppealPane.setVisible(true);

        appealChoiceBox.getItems().addAll(appeals);
        appealChoiceBox.setOnAction(this::getAppealType);
        appealChoiceBox.setValue(appeals[0]);

        tcasChoiceBox.getItems().addAll(tcas);
        tcasChoiceBox.setOnAction(this::getTcas);
        tcasChoiceBox.setValue(tcas[0]);

        purposesBreakChoiceBox.getItems().addAll(purposes);
        purposesBreakChoiceBox.setOnAction(this::getPurpose);
        purposesBreakChoiceBox.setValue(purposes[0]);

        semestersSuspendChoiceBox.getItems().addAll(semesters);
        semestersSuspendChoiceBox.setOnAction(this::getSemesters);
        semestersSuspendChoiceBox.setValue(semesters[0]);

        yearsSuspendChoiceBox.getItems().addAll(years);
        yearsSuspendChoiceBox.setOnAction(this::getYears);
        yearsSuspendChoiceBox.setValue(years[0]);

        semestersInstallmentChoiceBox.getItems().addAll(semesters);
        semestersInstallmentChoiceBox.setOnAction(this::getSemesters);
        semestersInstallmentChoiceBox.setValue(semesters[0]);

        yearsInstallmentChoiceBox.getItems().addAll(years);
        yearsInstallmentChoiceBox.setOnAction(this::getYears);
        yearsInstallmentChoiceBox.setValue(years[0]);
    }

    // ยืนยันการสร้างคำร้อง (Confirm Button)
    @FXML
    public void onConfirmButtonClick() {
        if (selectedAppeal.equals("ทั่วไป")) {
            String topic = topicTextArea.getText();
            String details = detailsTextArea.getText();
            if (topic.isEmpty() || details.isEmpty()) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
            else {
                System.out.println(topic + " " + details);
                resetTheValue();
            }
        }
        else if (selectedAppeal.equals("ขอลาออก")) {
            String gpa = gpaTextField.getText();
            String tcas = tcasChoiceBox.getValue();
            String purpose = purposeRetireTextArea.getText();
            if (gpa.isEmpty() || purpose.isEmpty()) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
            else {
                System.out.println(gpaTextField.getText() + " " + tcas + " " +  purposeRetireTextArea.getText());
                resetTheValue();
            }
        }
        else if (selectedAppeal.equals("ขอพักการศึกษา")) {
            String reason = reasonSuspendTextArea.getText();
            String semester = semestersSuspendChoiceBox.getValue();
            String year = yearsSuspendChoiceBox.getValue();
            String subjects = subjectsSuspendTextArea.getText();
            if (reason.isEmpty() || subjects.isEmpty()) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
            else {
                System.out.println(reason + " " + semester + " " + year + " " + subjects);
                resetTheValue();
            }
        }
        else if (selectedAppeal.equals("ลาป่วยหรือลากิจ")) {
            String purpose = reasonBreakTextArea.getText();
            String subjects = subjectsBreakTextArea.getText();

            // Bugs can't check empty DatePicker
            String startDate = (startBreakDatePicker == null) ? "" : startBreakDatePicker.getValue().toString();
            String endDate = (endBreakDatePicker == null) ? "" : endBreakDatePicker.getValue().toString();
            if (purpose.isEmpty() || subjects.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            } else {
                System.out.println(purpose + " " + subjects + " " + startDate + " " + endDate);
                resetTheValue();
            }
        }
        else if (selectedAppeal.equals("ขอผ่อนผันการชำระ")) {
            String reason = reasonInstallmentTextArea.getText();
            String semester = semestersInstallmentChoiceBox.getValue();
            String year = yearsInstallmentChoiceBox.getValue();
            if (reason.isEmpty()) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
            else {
                System.out.println(semester + " " + year + " " + reason);
                resetTheValue();
            }
        }
    }

    // รีเซ็ตค่า TextField, TextArea และ, ChoiceBox
    public void resetTheValue() {
        topicTextArea.clear();
        detailsTextArea.clear();
        gpaTextField.clear();
        purposeRetireTextArea.clear();
        reasonSuspendTextArea.clear();
        subjectsSuspendTextArea.clear();
        subjectsBreakTextArea.clear();
        startBreakDatePicker.getEditor().clear();
        endBreakDatePicker.getEditor().clear();
        reasonBreakTextArea.clear();
        reasonInstallmentTextArea.clear();

        tcasChoiceBox.setValue(tcas[0]);
        purposesBreakChoiceBox.setValue(purposes[0]);
        semestersSuspendChoiceBox.setValue(semesters[0]);
        yearsSuspendChoiceBox.setValue(years[0]);
        semestersInstallmentChoiceBox.setValue(semesters[0]);
        yearsInstallmentChoiceBox.setValue(years[0]);
    }

    // ปิดหน้าจอเตือน
    @FXML
    public void onCloseButtonClick() {
        backgroundAlertPane.setVisible(false);
        alertPane.setVisible(false);
    }

    // ไปที่หน้าติดตามคำร้อง
    @FXML
    private void onTrackAppealButtonClick() {
        try {
            FXRouter.goTo("student-track-appeal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ออกจากระบบ (กลับไปที่หน้าเข้าสู่ระบบ)
    @FXML
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}