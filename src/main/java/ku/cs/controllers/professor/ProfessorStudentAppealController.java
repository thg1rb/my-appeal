package ku.cs.controllers.professor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;
import ku.cs.services.*;

import java.io.IOException;

public class ProfessorStudentAppealController {
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TableView<Appeal> tableView;

    @FXML private Text totalText;

    private User user;

    private Datasource<AppealList> appealDatasource;
    private Datasource<UserList> userDatasource;
    private AppealList appealList;
    private UserList userList;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        appealDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealDatasource.readData();

        userDatasource = new UserListFileDatasource("data", "user.csv");
        userList = userDatasource.readData();

        showTable(appealList, userList);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/professor/professor-approve-student-appeal.fxml"));
                    Parent root = loader.load();

                    ProfessorApproveStudentAppealController controller = loader.getController();
                    controller.setSelectedAppeal(newValue, appealList, appealDatasource);

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setAlwaysOnTop(true);
                    stage.setScene(new Scene(root));

                    stage.showAndWait();

                    appealDatasource.writeData(appealList);

                    Platform.runLater(() -> {
                        if (!tableView.getItems().isEmpty()) {
                            tableView.getSelectionModel().clearSelection();  // Clear the current selection safely
                            showTable(appealList, userList);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ตารางแสดงคำร้องของนิสิตในที่ปรึกษาทั้งหมด
    private void showTable(AppealList appealList, UserList userList) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> type = new TableColumn<>("Appeal Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> ownerIdCol = new TableColumn<>("ID");
        ownerIdCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));

        TableColumn<Appeal, String> ownerFullNameCol = new TableColumn<>("Fullname");
        ownerFullNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));

        dateTimeCol.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(type);
        tableView.getColumns().add(ownerIdCol);
        tableView.getColumns().add(ownerFullNameCol);

        dateTimeCol.setPrefWidth(275);
        type.setPrefWidth(275);
        ownerIdCol.setPrefWidth(275);
        ownerFullNameCol.setPrefWidth(275);

        tableView.getItems().clear();
        for (User eachUser : userList.getUsers()) {
            if (!eachUser.getRole().equals("นักศึกษา")) continue;
            for (Appeal appeal : appealList.getAppeals()) {
                if (user.getFullName().equals(eachUser.getAdvisor()) && eachUser.getId().equals(appeal.getOwnerId()) && appeal.getStatus().equals("ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา")) {
                    tableView.getItems().add(appeal);
                }
            }
        }

        tableView.getSortOrder().add(dateTimeCol);
        updateTotalText();
    }

    // อัพเดทข้อความแสดงจำนวนคำร้องทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องของนิสิตในที่ปรึกษาทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }

    // ไปที่หน้านิสิตในที่ปรึกษา
    @FXML
    public void onStudentListButtonClick() {
        try {
            FXRouter.goTo("professor-student-list");
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
