package ku.cs.controllers.professor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.services.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.ModifyDateListFileDatasource;

import java.util.Date;

public class ProfessorApproveStudentAppealController {

    @FXML private Pane generalAppealPane;
    @FXML private Pane suspendAppealPane;
    @FXML private Pane breakAppealPane;

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

    private Appeal selectedAppeal;
    private Datasource<AppealList> appealListDatasource;
    private AppealList appealList;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    @FXML
    private void initialize() {
        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();
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
        generalAppealPane.setVisible(isGeneralAppeal);
        suspendAppealPane.setVisible(isSuspendAppeal);
        breakAppealPane.setVisible(isBreakAppeal);
    }

    // ปิดหน้าต่าง pop up
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
        selectedAppeal.setStatus("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา");

        modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setAdvisorApproveDate(modifyDate);

        onCloseButtonClick(event);
    }

    // ปฏิเสธคำร้อง
    @FXML
    public void onRejectButtonClick(ActionEvent event) {
        String modifyDate = DateTimeService.detailedDateToString(new Date());

        selectedAppeal.setModifyDate(modifyDate);
        selectedAppeal.setStatus("ปฏิเสธโดยอาจารย์ที่ปรึกษา | คำร้องถูกปฏิเสธ");

        modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()).setAdvisorApproveDate(modifyDate);

        onCloseButtonClick(event);
    }

}
