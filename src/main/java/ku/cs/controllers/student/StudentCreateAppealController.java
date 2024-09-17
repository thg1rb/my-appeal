package ku.cs.controllers.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StudentCreateAppealController {
    @FXML private Pane navbarAnchorPane;

    // Appeal
    @FXML private ChoiceBox<String> appealChoiceBox;
    private String[] appeals = {"ทั่วไป", "ขอพักการศึกษา", "ลาป่วยหรือลากิจ"};
    String selectedAppeal;

    // ประกาศตัวแปรคำร้องทั่วไป
    @FXML private Pane generalAppealPane;
    @FXML private TextArea topicTextArea;
    @FXML private TextArea detailsTextArea;

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

    //  ประกาศตัวแปรคำเตือน (ใส่ข้อมูลไม่ครบถ้วน)
    @FXML private Pane backgroundAlertPane;
    @FXML private Pane alertPane;

    private User user;

    private Datasource<AppealList> datasource;
    private AppealList appealList;

    @FXML
    public void initialize() {
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

        // อ่านไฟล์ appeal-list.csv (เอาไปใช้เขียนไฟล์ เพิ่มข้อมูล)
        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();

        initializeChoiceBox();
    }

    // รับค่าจาก ChoiceBox
    private void getAppealType(ActionEvent actionEvent) {
        selectedAppeal = appealChoiceBox.getValue();

        if (selectedAppeal.equals("ทั่วไป")) {
            generalAppealPane.setVisible(true);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(false);
        }
        else if (selectedAppeal.equals("ขอพักการศึกษา")) {
            generalAppealPane.setVisible(false);
            suspendAppealPane.setVisible(true);
            breakAppealPane.setVisible(false);
        }
        else if (selectedAppeal.equals("ลาป่วยหรือลากิจ")) {
            generalAppealPane.setVisible(false);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(true);
        }
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

        purposesBreakChoiceBox.getItems().addAll(purposes);
        purposesBreakChoiceBox.setOnAction(this::getPurpose);
        purposesBreakChoiceBox.setValue(purposes[0]);

        semestersSuspendChoiceBox.getItems().addAll(semesters);
        semestersSuspendChoiceBox.setOnAction(this::getSemesters);
        semestersSuspendChoiceBox.setValue(semesters[0]);

        yearsSuspendChoiceBox.getItems().addAll(years);
        yearsSuspendChoiceBox.setOnAction(this::getYears);
        yearsSuspendChoiceBox.setValue(years[0]);
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
                appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()), "คำร้องทั่วไป", ((Student)user).getStudentId(), user.getFullName(), topic, details));

                System.out.println(topic + " " + details);
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
                appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()), "คำร้องขอพักการศึกษา", ((Student)user).getStudentId(), user.getFullName(), reason, semester, year, subjects));

                System.out.println(reason + " " + semester + " " + year + " " + subjects);
                resetTheValue();
            }
        }
        else if (selectedAppeal.equals("ลาป่วยหรือลากิจ")) {
            String purpose = purposesBreakChoiceBox.getValue();
            String subjects = subjectsBreakTextArea.getText();
            String reason = reasonBreakTextArea.getText();

            // Bugs can't check empty DatePicker
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String startDate = (startBreakDatePicker.getValue() == null) ? "" : startBreakDatePicker.getValue().format(dateFormatter);
            String endDate = (endBreakDatePicker.getValue() == null) ? "" : endBreakDatePicker.getValue().format(dateFormatter);
            if (purpose.isEmpty() || subjects.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            } else {
                appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอลาป่วยหรือลากิจ", ((Student)user).getStudentId(), user.getFullName(), purpose, subjects, startDate, endDate));

                System.out.println(purpose + " " + subjects + " " + startDate + " " + endDate);
                resetTheValue();
            }
        }
        datasource.writeData(appealList);
        appealList = datasource.readData();
    }

    // รีเซ็ตค่า TextField, TextArea และ, ChoiceBox
    public void resetTheValue() {
        topicTextArea.clear();
        detailsTextArea.clear();
        reasonSuspendTextArea.clear();
        subjectsSuspendTextArea.clear();
        subjectsBreakTextArea.clear();
        startBreakDatePicker.getEditor().clear();
        endBreakDatePicker.getEditor().clear();
        reasonBreakTextArea.clear();

        purposesBreakChoiceBox.setValue(purposes[0]);
        semestersSuspendChoiceBox.setValue(semesters[0]);
        yearsSuspendChoiceBox.setValue(years[0]);
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
            FXRouter.goTo("student-track-appeal", user);
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