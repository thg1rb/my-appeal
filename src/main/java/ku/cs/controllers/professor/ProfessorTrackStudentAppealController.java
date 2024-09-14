package ku.cs.controllers.professor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.User;

public class ProfessorTrackStudentAppealController {
    @FXML Label ownerAppealLabel;

    @FXML private TableView<Appeal> tableView;

    // ตารางแสดงคำร้องของนิสิตในที่ปรึกษารายบุคคล
    public void showTable(AppealList appealList, User user) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);
        tableView.getColumns().add(statusCol);

        tableView.getItems().clear();
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getOwnerId().equals(user.getId())) {
                tableView.getItems().add(appeal);
            }
        }
    }

    // ปิดหน้าต่าง pop up
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}