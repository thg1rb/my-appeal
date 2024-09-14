package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.User;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentTrackAppealController {

    private Datasource<AppealList> datasource;
    private AppealList appealList;
    private User user;

    @FXML private Circle profileImageCircle;

    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TableView<Appeal> tableView;

    @FXML private Text totalText;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

        // แสดงโปรไฟล์ผู้ใช้งาน
        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        Image profileImage = new Image(getClass().getResource("/images/student-profile.jpeg").toString());
        profileImageCircle.setFill(new ImagePattern(profileImage));

        // แสดงข้อมูลภายในตาราง
        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        showTable(appealList, user.getId());
    }

    // ตารางแสดงคำร้องทั้งหมดของนิสิต
    private void showTable(AppealList appealList, String ownerId) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("Appeal Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        dateTimeCol.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);

        tableView.getItems().clear();
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getOwnerId().equals(ownerId)) {
                tableView.getItems().add(appeal);
            }
        }
        tableView.getSortOrder().add(dateTimeCol);

        updateTotalText();
    }

    // อัพเดตข้อความแสดงคำร้องทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }

    // ไปที่หน้าสร้างใบคำร้อง
    @FXML
    private void onCreateAppealButtonClick() {
        try {
            FXRouter.goTo("student-create-appeal", user);
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