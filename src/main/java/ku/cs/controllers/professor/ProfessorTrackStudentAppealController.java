package ku.cs.controllers.professor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;

public class ProfessorTrackStudentAppealController {
    @FXML Label ownerAppealLabel;

    @FXML private TableView<Appeal> tableView;
    @FXML private Text totalText;

    @FXML private ImageView closePopUpImageView;
    @FXML private Button closePopUpButton;

    @FXML
    private void initialize() {

        // ปุ่มปิดหน้าต่าง
        Image defaultClosePopUpImage = new Image(getClass().getResource("/icons/close-pop-up.png").toString());
        Image hoverClosePopUpImage = new Image(getClass().getResource("/icons/close-pop-up-hover.png").toString());

        closePopUpImageView.setImage(defaultClosePopUpImage);

        closePopUpButton.setOnMouseEntered(mouseEvent -> closePopUpImageView.setImage(hoverClosePopUpImage));
        closePopUpButton.setOnMouseExited(mouseEvent -> closePopUpImageView.setImage(defaultClosePopUpImage));
    }

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
            if (appeal.getOwnerId().equals(((Student)user).getStudentId())) {
                tableView.getItems().add(appeal);
            }
        }
        updateTotalText();
    }

    // อัพเดทข้อความแสดงจำนวนนิสิตในที่ปรึกษาทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องของนิสิตในที่ปรึกษาทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }

    // ปิดหน้าต่าง pop up
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}