package ku.cs.controllers.professor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;

import java.io.IOException;

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
    private AppealList appealList;
    private Datasource<AppealList> datasource;

    public void setSelectedAppeal(Appeal selectedAppeal, AppealList appealList, Datasource<AppealList> datasource) {
        this.selectedAppeal = selectedAppeal;
        this.appealList = appealList;
        this.datasource = datasource;

        updateAppealDetails();
        initializeLabel();
    }

    public void updateAppealDetails() {
        fullnameLabel.setText(selectedAppeal.getOwnerFullName());
        idLabel.setText(selectedAppeal.getOwnerId());
        typeLabel.setText(selectedAppeal.getType());
        createDateLabel.setText(selectedAppeal.getCreateDate());
        showAppealPane(selectedAppeal.getType());
    }

    public void initializeLabel() {
        detailsGeneralLabel.setText(selectedAppeal.getReason());
        reasonSuspendLabel.setText(selectedAppeal.getReason());
        semesterSuspendLabel.setText(selectedAppeal.getSemester());
        yearSuspendLabel.setText(selectedAppeal.getYear());
        subjectsSuspendLabel.setText(selectedAppeal.getSubjects());

        purposeBreakLabel.setText(selectedAppeal.getPurpose());
        startEndBreakLabel.setText(selectedAppeal.getStartDate() + " - " + selectedAppeal.getEndDate());
        System.out.println(selectedAppeal.getStartDate() + " " + selectedAppeal.getEndDate());
        reasonBreakLabel.setText(selectedAppeal.getReason());
        subjectsBreakLabel.setText(selectedAppeal.getSubjects());
    }

    public void showAppealPane(String type) {
        if (type.equals("คำร้องทั่วไป")) {
            generalAppealPane.setVisible(true);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(false);
        }
        else if (type.equals("คำร้องขอพักการศึกษา")) {
            generalAppealPane.setVisible(false);
            suspendAppealPane.setVisible(true);
            breakAppealPane.setVisible(false);
        }
        else if (type.equals("คำร้องขอลาป่วยหรือลากิจ")) {
            generalAppealPane.setVisible(false);
            suspendAppealPane.setVisible(false);
            breakAppealPane.setVisible(true);
        }
    }

    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    //
    @FXML
    public void onApproveButtonClick(ActionEvent event) {
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getUuid().equals(selectedAppeal.getUuid())) {
                appeal.setStatus("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา");
                break;
            }
        }

        datasource.writeData(appealList);
        onCloseButtonClick(event);
    }

    //
    @FXML
    public void onRejectButtonClick(ActionEvent event) {
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getUuid().equals(selectedAppeal.getUuid())) {
                appeal.setStatus("ปฏิเสธโดยอาจารย์ที่ปรึกษา | คำร้องถูกปฏิเสธ");
                break;
            }
        }

        datasource.writeData(appealList);
        onCloseButtonClick(event);
    }

}
