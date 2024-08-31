package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.User;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import javax.xml.crypto.Data;
import java.io.IOException;

public class StudentTrackAppealController {

    @FXML private Circle profileImageCircle;

    @FXML private TableView<Appeal> tableView;
    private Datasource<AppealList> datasource;
    private AppealList appealList;
    private User user;

    @FXML
    public void initialize() {
        // แสดงโปรไฟล์ผู้ใช้งาน
        Image profileImage = new Image(getClass().getResource("/images/student-profile.jpeg").toString());
        profileImageCircle.setFill(new ImagePattern(profileImage));

        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();

//        user = (User) FXRouter.getData();
//        showTable(appealList, user.getId());
        showTable(appealList, "6610401985");
    }

    public void showTable(AppealList appealList, String ownerId) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("Appeal Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);

        // Width

        tableView.getItems().clear();
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getOwner().equals(ownerId)) {
                tableView.getItems().add(appeal);
            }
        }
    }

    // ไปที่หน้าสร้างใบคำร้อง
    @FXML
    private void onCreateAppealButtonClick() {
        try {
            FXRouter.goTo("student-create-appeal");
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