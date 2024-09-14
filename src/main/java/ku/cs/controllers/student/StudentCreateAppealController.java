package ku.cs.controllers.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.dates.ModifyDate;
import ku.cs.models.persons.User;
import ku.cs.services.*;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class StudentCreateAppealController {

    @FXML private Circle profileImageCircle;

    @FXML private Label usernameLabel;
    @FXML private Label  roleLabel;

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

    private Datasource<AppealList> appealListDatasource;
    private AppealList appealList;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        // แสดงโปรไฟล์ผู้ใช้งาน
        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        Image profileImage = new Image(getClass().getResource("/images/student-profile.jpeg").toString());
        profileImageCircle.setFill(new ImagePattern(profileImage));

        // อ่านไฟล์ appeal-list.csv (เอาไปใช้เขียนไฟล์หรือเพิ่มข้อมูล)
        appealListDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealListDatasource.readData();

        // อ่านไฟล์ modify-date.csv (เอาไปใช้เขียนไฟล์หรือเพิ่มข้อมูล)
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        initializeChoiceBox();
    }

    // แสดงและกำหนดค่าเริ่มต้น ChoiceBox
    private void initializeChoiceBox() {
        // ChoiceBox เลือกประเภทของคำร้อง
        selectedAppeal = "ทั่วไป";
        generalAppealPane.setVisible(true);

        // Appeal Type ChoiceBox
        appealChoiceBox.getItems().addAll(appeals);
        appealChoiceBox.setOnAction((ActionEvent actionEvent) -> {
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
        });
        appealChoiceBox.setValue(appeals[0]);

        // Semesters (คำร้องขอพักการศึกษา) ChoiceBox
        semestersSuspendChoiceBox.getItems().addAll(semesters);
        semestersSuspendChoiceBox.setOnAction((ActionEvent actionEvent) -> {
            selectedSemester = semestersSuspendChoiceBox.getValue();
        });
        semestersSuspendChoiceBox.setValue(semesters[0]);

        // Years (คำร้องขอพักการศึกษา) ChoiceBox
        yearsSuspendChoiceBox.getItems().addAll(years);
        yearsSuspendChoiceBox.setOnAction((ActionEvent actionEvent) -> {
            selectedYear = yearsSuspendChoiceBox.getValue();
        });
        yearsSuspendChoiceBox.setValue(years[0]);

        // Purposes (คำร้องขอลากิจหรือลาป่วย) ChoiceBox
        purposesBreakChoiceBox.getItems().addAll(purposes);
        purposesBreakChoiceBox.setOnAction((ActionEvent actionEvent) ->  {
            selectedPurpose = purposesBreakChoiceBox.getValue();
        });
        purposesBreakChoiceBox.setValue(purposes[0]);
    }

    // ยืนยันการสร้างคำร้อง (Confirm Button)
    @FXML
    public void onConfirmButtonClick() {
        String uuid = UUID.randomUUID().toString();
        String createDate = DateTimeService.detailedDateToString(new Date());

        if (selectedAppeal.equals("ทั่วไป")) {
            try {
                String topic = topicTextArea.getText();
                String details = detailsTextArea.getText();

                if (topic.isEmpty() || details.isEmpty()) {
                    throw new EmptyInputException();
                }

                appealList.addAppeal(new GeneralAppeal(createDate, uuid, "คำร้องทั่วไป", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", user.getId(), user.getFullName(), details, topic));
                resetTheValue();
            } catch (EmptyInputException e) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
        }
        else if (selectedAppeal.equals("ขอพักการศึกษา")) {
            try {
                String reason = reasonSuspendTextArea.getText();
                String subjects = subjectsSuspendTextArea.getText();
                String semester = semestersSuspendChoiceBox.getValue();
                String year = yearsSuspendChoiceBox.getValue();

                if (reason.isEmpty() || subjects.isEmpty()) {
                    throw new EmptyInputException();
                }

                appealList.addAppeal(new SuspendAppeal(createDate, uuid, "คำร้องขอพักการศึกษา", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", user.getId(), user.getFullName(), reason, subjects, semester, year));
                resetTheValue();
            } catch (EmptyInputException e) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
        }
        else if (selectedAppeal.equals("ลาป่วยหรือลากิจ")) {
            try {
                String reason = reasonBreakTextArea.getText();
                String purpose = purposesBreakChoiceBox.getValue();
                String subjects = subjectsBreakTextArea.getText();

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                String startDate = (startBreakDatePicker.getValue() == null) ? "" : startBreakDatePicker.getValue().format(dateFormatter);
                String endDate = (endBreakDatePicker.getValue() == null) ? "" : endBreakDatePicker.getValue().format(dateFormatter);

                if (purpose.isEmpty() || subjects.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    throw new EmptyInputException();
                }

                appealList.addAppeal(new BreakAppeal(createDate, uuid, "คำร้องขอลาป่วยหรือลากิจ", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", user.getId(), user.getFullName(), reason, subjects, purpose, startDate, endDate));
                resetTheValue();
            } catch (EmptyInputException e) {
                backgroundAlertPane.setVisible(true);
                alertPane.setVisible(true);
            }
        }

        modifyDateList.addModifyDate(new ModifyDate(uuid, createDate));
        modifyDateListDatasource.writeData(modifyDateList);
        modifyDateListDatasource.readData();

        appealListDatasource.writeData(appealList);
        appealListDatasource.readData();
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