package ku.cs.controllers.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.dates.ModifyDate;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.datasources.ModifyDateListFileDatasource;
import ku.cs.services.exceptions.EmptyInputException;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.UUID;

public class StudentCreateAppealController {
    @FXML private Pane navbarAnchorPane;

    @FXML private Label createAppealTitle;

    // Appeal
    @FXML private ChoiceBox<String> appealChoiceBox;

    // ประกาศตัวแปรคำร้องทั่วไป
    @FXML private Pane generalAppealPane;
    @FXML private TextArea topicTextArea;
    @FXML private TextArea detailsTextArea;

    // ประกาศตัวแปรคำร้องขอพักการศึกษา
    @FXML private Pane suspendAppealPane;
    @FXML private TextArea reasonSuspendTextArea;
    @FXML private TextArea subjectsSuspendTextArea;
    @FXML private ChoiceBox<String> semestersSuspendChoiceBox;
    @FXML private ChoiceBox<String> yearsSuspendChoiceBox;

    // ประกาศตัวแปรคำร้องขอลาป่วยหรือลากิจ
    @FXML private Pane breakAppealPane;
    @FXML private ChoiceBox<String> purposesBreakChoiceBox;
    @FXML private TextArea reasonBreakTextArea;
    @FXML private TextArea subjectsBreakTextArea;
    @FXML private DatePicker startBreakDatePicker;
    @FXML private DatePicker endBreakDatePicker;

    //  ประกาศตัวแปร Alert (สร้างคำร้องไม่สำเร็จ และสร้างคำร้องสำเร็จ)
    @FXML private Pane backgroundAlertPane;
    @FXML private Pane failAlertPane;
    @FXML private Label failReasonLabel;
    @FXML private Pane successAlertPane;

    private String[] appeals = {"ทั่วไป", "ขอพักการศึกษา", "ลาป่วยหรือลากิจ"};
    private String selectedAppeal;

    private String[] semesters = {"ภาคต้น", "ภาคปลาย", "ภาคฤดูร้อน"};
    private String selectedSemester;

    private String[] years = {"2565", "2566", "2567", "2568", "2569", "2570", "อื่นๆ"};
    private String selectedYear;

    private String[] purposes = {"ลาป่วย", "ลากิจ"};
    String selectedPurpose;

    private User user;
    private boolean hasAdvisor;

    private Datasource<AppealList> appealListDatasource;
    private AppealList appealList;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();
        hasAdvisor = ((Student) user).getAdvisorUUID() != null;

        // อ่านไฟล์ appeal-list.csv (เอาไปใช้เขียนไฟล์หรือเพิ่มข้อมูล)
        appealListDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealListDatasource.readData();

        // อ่านไฟล์ modify-date.csv (เอาไปใช้เขียนไฟล์หรือเพิ่มข้อมูล)
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        initializeChoiceBox();
        initializeDatePicker();

        try {
            if (!hasAdvisor)
                throw new NullPointerException("บัญชีของคุณยังไม่มีอาจารย์ที่ปรึกษาโปรดติดต่อเจ้าหน้าที่");
        } catch (NullPointerException e) {
            backgroundAlertPane.setVisible(true);
            failReasonLabel.setText(e.getMessage());
            failAlertPane.setVisible(true);
        }
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
                    throw new EmptyInputException("โปรดใส่รายละเอียดของคำร้องให้ครบถ้วนก่อนสร้างคำร้อง");
                }

                backgroundAlertPane.setVisible(true);
                successAlertPane.setVisible(true);

                appealList.addAppeal(new GeneralAppeal(createDate, uuid, "คำร้องทั่วไป", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", null, ((Student)user).getStudentId(), user.getFullName(), ((Student)user).getDepartmentUUID().toString(), ((Student)user).getFacultyUUID().toString(), null, null, details, null, topic));
                modifyDateList.addModifyDate(new ModifyDate(uuid, createDate));
                resetTheValue();
            } catch (EmptyInputException e) {
                failReasonLabel.setText(e.getMessage());
                backgroundAlertPane.setVisible(true);
                failAlertPane.setVisible(true);
            }
        }
        else if (selectedAppeal.equals("ขอพักการศึกษา")) {
            try {
                String reason = reasonSuspendTextArea.getText();
                String subjects = subjectsSuspendTextArea.getText();
                String semester = semestersSuspendChoiceBox.getValue();
                String year = yearsSuspendChoiceBox.getValue();

                if (reason.isEmpty() || subjects.isEmpty()) {
                    throw new EmptyInputException("โปรดใส่รายละเอียดของคำร้องให้ครบถ้วนก่อนสร้างคำร้อง");
                }

                backgroundAlertPane.setVisible(true);
                successAlertPane.setVisible(true);

                appealList.addAppeal(new SuspendAppeal(createDate, uuid, "คำร้องขอพักการศึกษา", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", null, ((Student)user).getStudentId(), user.getFullName(), ((Student)user).getDepartmentUUID().toString(), ((Student)user).getFacultyUUID().toString(), null, null, reason, subjects, semester, year));
                modifyDateList.addModifyDate(new ModifyDate(uuid, createDate));
                resetTheValue();
            } catch (EmptyInputException e) {
                failReasonLabel.setText(e.getMessage());
                backgroundAlertPane.setVisible(true);
                failAlertPane.setVisible(true);
            }
        }
        else if (selectedAppeal.equals("ลาป่วยหรือลากิจ")) {
            try {
                String reason = reasonBreakTextArea.getText();
                String purpose = purposesBreakChoiceBox.getValue();
                String subjects = subjectsBreakTextArea.getText();

                LocalDate startDateValue = startBreakDatePicker.getValue();
                LocalDate endDateValue = endBreakDatePicker.getValue();

                if (purpose.isEmpty() || subjects.isEmpty() || startBreakDatePicker.getValue() == null || startDateValue == null || endDateValue.isBefore(startDateValue)) {
                    throw new EmptyInputException("โปรดใส่รายละเอียดของคำร้องให้ครบก่อนถ้วนสร้างคำร้อง");
                }

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String startDate = startDateValue.format(dateFormatter);
                String endDate = endDateValue.format(dateFormatter);

                backgroundAlertPane.setVisible(true);
                successAlertPane.setVisible(true);

                appealList.addAppeal(new BreakAppeal(createDate, uuid, "คำร้องขอลาป่วยหรือลากิจ", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", null, ((Student)user).getStudentId(), user.getFullName(), ((Student)user).getDepartmentUUID().toString(), ((Student)user).getFacultyUUID().toString(), null, null, reason, subjects, purpose, startDate, endDate));
                modifyDateList.addModifyDate(new ModifyDate(uuid, createDate));
                resetTheValue();
            } catch (EmptyInputException e) {
                failReasonLabel.setText(e.getMessage());
                backgroundAlertPane.setVisible(true);
                failAlertPane.setVisible(true);
            } catch (Exception e) {
                System.out.println("ฟอร์แมทไม่ถูกต้อง");
            }
        }

        modifyDateListDatasource.writeData(modifyDateList);
        modifyDateListDatasource.readData();

        appealListDatasource.writeData(appealList);
        appealListDatasource.readData();
    }

    // รีเซ็ตค่า TextField, TextArea และ, ChoiceBox
    private void resetTheValue() {
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

        startBreakDatePicker.setValue(LocalDate.now());
        endBreakDatePicker.setValue(LocalDate.now());
    }

    // กำหนดเริ่มต้น Date Picker
    private void initializeDatePicker() {
        startBreakDatePicker.setValue(LocalDate.now());
        endBreakDatePicker.setValue(LocalDate.now());

        restrictDatePickerToCurrentAndFuture(startBreakDatePicker);
        restrictDatePickerToCurrentAndFuture(endBreakDatePicker);
    }

    // จำกัดการเลือกวันให้อยู่ในช่วงปัจจุบันและอนาคตเท่านั้น
    private void restrictDatePickerToCurrentAndFuture(DatePicker datePicker) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // ปิดการเลือกวันก่อนหน้าทั้งหมด
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #666666;"); // Optional: make disabled dates stand out
                        }
                    }
                };
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);
    }

    // ปิดหน้าจอเตือน
    @FXML
    public void onCloseButtonClick() {
        backgroundAlertPane.setVisible(false);

        if (successAlertPane.isVisible()) {
            successAlertPane.setVisible(false);
            onTrackAppealButtonClick();
        } else if (failAlertPane.isVisible()) {
            failAlertPane.setVisible(false);
            if (!hasAdvisor)
                onTrackAppealButtonClick();
        }
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