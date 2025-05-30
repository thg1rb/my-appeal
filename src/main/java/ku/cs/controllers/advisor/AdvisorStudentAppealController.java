package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ku.cs.models.appeals.Appeal;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.*;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.datasources.UserListDatasource;
import ku.cs.services.datasources.Datasource;

import java.io.File;
import java.io.IOException;

public class AdvisorStudentAppealController {

    @FXML private AnchorPane mainPane;
    @FXML private Label totalLabel;
    @FXML private Pane navbarAnchorPane;
    @FXML private TableView<Appeal> tableView;

    private User user;

    private Datasource<AppealList> appealDatasource;
    private AppealList appealList;

    private Datasource<UserList> studentDatasource;
    private UserList studentList;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

        appealDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealDatasource.readData();

        studentDatasource = new UserListDatasource("data" + File.separator + "users", "student.csv");
        studentList = studentDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        showTable(appealList, studentList);

        // แสดง pop-up เมื่อกดที่เซลล์ใดเซลล์หนึ่งในตาราง
        tableView.setOnMouseClicked(mouseEvent -> {
            Appeal selectedAppeal = tableView.getSelectionModel().getSelectedItem();
            if (selectedAppeal != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/advisor/advisor-approve-student-appeal.fxml"));
                    Parent root = loader.load();

                    AdvisorApproveStudentAppealController controller = loader.getController();
                    controller.setSelectedAppeal(selectedAppeal);

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setAlwaysOnTop(true);
                    stage.setScene(new Scene(root));

                    stage.showAndWait();

                    appealDatasource.writeData(appealList);

                    tableView.getSelectionModel().clearSelection();  // Clear the current selection safely
                    showTable(appealList, studentList);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ตารางแสดงคำร้องของนิสิตในที่ปรึกษาทั้งหมด
    private void showTable(AppealList appealList, UserList studentList) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("วันที่สร้างคำร้อง");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("ประเภทของคำร้อง");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> ownerIdCol = new TableColumn<>("รหัสนิสิต");
        ownerIdCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));

        TableColumn<Appeal, String> ownerFullNameCol = new TableColumn<>("ชื่อ-สกุล");
        ownerFullNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));

        dateTimeCol.setComparator(new DateTimeService());

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);
        tableView.getColumns().add(ownerIdCol);
        tableView.getColumns().add(ownerFullNameCol);

        dateTimeCol.setPrefWidth(275);
        typeCol.setPrefWidth(275);
        ownerIdCol.setPrefWidth(275);
        ownerFullNameCol.setPrefWidth(275);

        tableView.getItems().clear();
        for (User eachUser : studentList.getUsers()) {
            for (Appeal appeal : appealList.getAppeals()) {
                if ((user.getUuid()).equals(((Student)eachUser).getAdvisorUUID()) && ((Student)eachUser).getStudentId().equals(appeal.getOwnerId()) && appeal.getStatus().equals("ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา")) {
                    tableView.getItems().add(appeal);
                }
            }
        }
        tableView.getSortOrder().add(dateTimeCol);

        dateTimeCol.setSortable(false);
        typeCol.setSortable(false);
        ownerIdCol.setSortable(false);
        ownerFullNameCol.setSortable(false);
        tableView.getColumns().forEach(column -> column.setReorderable(false));

        updateTotalLabel();
    }

    // อัพเดทข้อความแสดงจำนวนคำร้องทั้งหมด
    private void updateTotalLabel() {
        totalLabel.setText("คำร้องของนิสิตในที่ปรึกษาทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }
}
