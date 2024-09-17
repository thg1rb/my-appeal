package ku.cs.controllers.professor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class ProfessorStudentListController {
    @FXML private Pane navbarAnchorPane;

    @FXML private Text totalText;

    private User user;

    @FXML private TableView<User> tableView;
    private Datasource<UserList> datasource;
    private UserList userList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        // datasource
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();

        showTable(userList);
    }

    // ตารางแสดงนิสิตในที่ปรึกษาทั้งหมด (default)
    private void showTable(UserList userList) {
        TableColumn<User, String> pathCol = new TableColumn<>("Profile");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> fullnameCol = new TableColumn<>("Fullname");
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));

        tableView.getColumns().clear();
        tableView.getColumns().add(pathCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(fullnameCol);
        tableView.getColumns().add(idCol);

        pathCol.setPrefWidth(275);
        usernameCol.setPrefWidth(275);
        fullnameCol.setPrefWidth(275);
        idCol.setPrefWidth(275);

        tableView.getItems().clear();
        for (User student : userList.getUsers()) {
            if (student.getRole().equals("นักศึกษา") && ((Student)student).getAdvisor().equals(user.getFullName())) {
                tableView.getItems().add(student);
            }
        }
        updateTotalText();
    }

    // ตารางแสดงนิสิตในที่ปรึกษาทั้งหมด (ใช้ร่วมกับ Search Text Field)
    private void showSearchTable(UserList userList, String searchText) {
        TableColumn<User, String> pathCol = new TableColumn<>("Profile");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> fullnameCol = new TableColumn<>("Fullname");
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));

        tableView.getColumns().clear();
        tableView.getColumns().add(pathCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(fullnameCol);
        tableView.getColumns().add(idCol);

        pathCol.setPrefWidth(275);
        usernameCol.setPrefWidth(275);
        fullnameCol.setPrefWidth(275);
        idCol.setPrefWidth(275);

        // Add Student filter by Professer name (Not Done Yet)
        tableView.getItems().clear();
        for (User student : userList.getUsers()) {
            if (student.getRole().equals("นักศึกษา") && ((Student)student).getAdvisor().equals(user.getFullName())) {
                tableView.getItems().add(student);
            }
        }
        updateTotalText();
    }

    // อัพเดทข้อความแสดงจำนวนนิสิตในที่ปรึกษาทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องของนิสิตในที่ปรึกษาทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }

}