package ku.cs.controllers.advisor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.controllers.general.AppealDetailsController;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.DateTimeService;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.io.IOException;

public class AdvisorTrackStudentAppealController {
    @FXML private AnchorPane mainPane;

    @FXML private Label ownerAppealLabel;

    @FXML private TableView<Appeal> tableView;
    @FXML private Label totalLabel;

    @FXML private ImageView closePopUpImageView;
    @FXML private Button closePopUpButton;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;

    private Appeal selectedAppeal;

    @FXML
    private void initialize() {

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        // ปุ่มปิดหน้าต่าง
        Image defaultClosePopUpImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        Image hoverClosePopUpImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());

        closePopUpImageView.setImage(defaultClosePopUpImage);

        closePopUpButton.setOnMouseEntered(mouseEvent -> closePopUpImageView.setImage(hoverClosePopUpImage));
        closePopUpButton.setOnMouseExited(mouseEvent -> closePopUpImageView.setImage(defaultClosePopUpImage));

        tableView.setRowFactory(v -> {
            TableRow<Appeal> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                selectedAppeal = tableView.getSelectionModel().getSelectedItem();
                if (selectedAppeal != null)
                    showStudentAppealDetail();
            });
            return row;
        });
    }

    // ตารางแสดงคำร้องของนิสิตในที่ปรึกษารายบุคคล
    public void showTable(AppealList appealList, User user) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("วันเวลาที่สถานะเปลี่ยน");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("ประเภทของคำร้อง");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> statusCol = new TableColumn<>("สถานะของคำร้อง");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        dateTimeCol.setComparator(new DateTimeService());

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);
        tableView.getColumns().add(statusCol);

        tableView.getItems().clear();
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getOwnerId().equals(((Student)user).getStudentId())) {
                tableView.getItems().add(appeal);
            }
        }

        tableView.getSortOrder().add(dateTimeCol);

        dateTimeCol.setSortable(false);
        typeCol.setSortable(false);
        statusCol.setSortable(false);
        tableView.getColumns().forEach(column -> column.setReorderable(false));
        updateTotalLabel();
    }

    public void setSelectedStudent(AppealList appealList, Student selectedStudent) {
        ownerAppealLabel.setText("คำร้องทั้งหมดของ " + selectedStudent.getFullName());
        showTable(appealList, selectedStudent);
    }

    private void showStudentAppealDetail() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/appeal-details.fxml"));
            Parent root = loader.load();

            AppealDetailsController controller = loader.getController();
            controller.setSelectedAppeal(selectedAppeal, modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()));

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // อัพเดทข้อความแสดงจำนวนนิสิตในที่ปรึกษาทั้งหมด
    private void updateTotalLabel() {
        totalLabel.setText("คำร้องของนิสิตในที่ปรึกษาทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }

    // ปิดหน้าต่าง pop up
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}