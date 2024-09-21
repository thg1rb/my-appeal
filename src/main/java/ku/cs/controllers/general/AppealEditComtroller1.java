package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.util.Date;

public class AppealEditComtroller1 {

    @FXML private ScrollPane generalAppealScrollPane;
    @FXML private ScrollPane suspendAppealScrollPane;
    @FXML private ScrollPane breakAppealScrollPane;

    @FXML private Label fullnameLabel;
    @FXML private Label idLabel;
    @FXML private Label typeLabel;
    @FXML private Label createDateLabel;

    @FXML private Label detailsGeneralLabel;
    @FXML private Label reasonSuspendLabel;
    @FXML private Label semesterSuspendLabel;
    @FXML private Label yearSuspendLabel;
    @FXML private Label subjectsSuspendLabel;

    @FXML private Label purposeBreakLabel;
    @FXML private Label startEndBreakLabel;
    @FXML private Label reasonBreakLabel;
    @FXML private Label subjectsBreakLabel;

    @FXML private ChoiceBox<String> statusChoiceBox;

    @FXML private ImageView closePopUpImageView;
    @FXML private Button closePopUpButton;

    private Appeal selectedAppeal;
    private Datasource<AppealList> appealListDatasource;
    private AppealList appealList;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    private String[] majorStatusList = {"ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี", "อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน", "ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ"};
    private String[] facultyStatusList = {"อนุมัติโดยคณบดี | คำร้องดำเนินการครบถ้วน" , "ปฏิเสธโดยคณบดี | คำร้องถูกปฏิเสธ"};
    private String role;
    private String selectedStatus;

    @FXML
    private void initialize() {
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        // ปุ่มปิดหน้าต่าง
        Image defaultClosePopUpImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        Image hoverClosePopUpImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());

        closePopUpImageView.setImage(defaultClosePopUpImage);

        closePopUpButton.setOnMouseEntered(mouseEvent -> closePopUpImageView.setImage(hoverClosePopUpImage));
        closePopUpButton.setOnMouseExited(mouseEvent -> closePopUpImageView.setImage(defaultClosePopUpImage));
    }

    // กำหนดตำแหน่ง ภาควิชา / คณะ
    public void setRole(User user){
        role = user.getRole();
        if(role.equals("เจ้าหน้าที่ภาควิชา")){
            statusChoiceBox.getItems().addAll(majorStatusList);
            statusChoiceBox.setOnAction(this::getStatus);
            statusChoiceBox.setValue(majorStatusList[0]);
        }
        else if(role.equals("เจ้าหน้าที่คณะ")) {
            statusChoiceBox.getItems().addAll(facultyStatusList);
            statusChoiceBox.setOnAction(this::getStatus);
            statusChoiceBox.setValue(facultyStatusList[0]);
        }
    }

    public void getStatus(Event event) {
        selectedStatus = statusChoiceBox.getValue();
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
        createDateLabel.setText(selectedAppeal.getModifyDate());

        showAppealPane(selectedAppeal.isGeneralAppeal(), selectedAppeal.isSuspendAppeal(), selectedAppeal.isBreakAppeal());
    }

    // กำหนดค่าให้กับ Label ตามหน้าที่ประเภทของคำร้อง
    private void initializeLabel(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        if (isGeneralAppeal) {
            GeneralAppeal generalAppeal = (GeneralAppeal) selectedAppeal;
            detailsGeneralLabel.setText(generalAppeal.getReason());
        } else if (isSuspendAppeal) {
            SuspendAppeal suspendAppeal = (SuspendAppeal) selectedAppeal;
            reasonSuspendLabel.setText(suspendAppeal.getReason());
            semesterSuspendLabel.setText(suspendAppeal.getSemester());
            yearSuspendLabel.setText(suspendAppeal.getYear());
            subjectsSuspendLabel.setText(suspendAppeal.getSubjects());
        } else if (isBreakAppeal) {
            BreakAppeal breakAppeal = (BreakAppeal) selectedAppeal;
            reasonBreakLabel.setText(breakAppeal.getReason());
            purposeBreakLabel.setText(breakAppeal.getPurpose());
            startEndBreakLabel.setText(breakAppeal.getStartDate() + " - " + breakAppeal.getEndDate());
            subjectsBreakLabel.setText(breakAppeal.getSubjects());
        }
    }

    // แสดงรายละเอียดคำร้องตามประเภทของคำร้อง
    public void showAppealPane(Boolean isGeneralAppeal, Boolean isSuspendAppeal, Boolean isBreakAppeal) {
        initializeLabel(isGeneralAppeal, isSuspendAppeal, isBreakAppeal);
        generalAppealScrollPane.setVisible(isGeneralAppeal);
        suspendAppealScrollPane.setVisible(isSuspendAppeal);
        breakAppealScrollPane.setVisible(isBreakAppeal);
    }

    // ปิดหน้าต่าง pop-up & ยกเลิก
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
        selectedAppeal.setStatus(selectedStatus);
        if(role.equals("เจ้าหน้าที่ภาควิชา")){
            modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setDepartmentApproveDate(modifyDate);
        }
        else if(role.equals("เจ้าหน้าที่คณะ")){
            modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setDeanApproveDate(modifyDate);
        }
        onCloseButtonClick(event);
    }

}
