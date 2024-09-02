package ku.cs.controllers.professor;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class ProfessorStudentListController {

    private User user;

    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TextField searchTextField;

    @FXML private TableView<User> tableView;
    private Datasource<UserList> datasource;
    private UserList userList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        // datasource
        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();

        // ช่องค้นหา
        showTable(userList);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[a-zA-zก-๙]+$") && !newValue.isEmpty()) {
                showSearchTable(userList, newValue);
            }
            else if (newValue.isEmpty() || newValue.isBlank()) {
                showTable(userList);
            }
        });
    }

    public void showTable(UserList userList) {
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

        tableView.getItems().clear();
        for (User student : userList.getUsers()) {
            if (student.getRole().equals("นักศึกษา") && student.getAdvisor().equals(user.getFullName())) {
                tableView.getItems().add(student);
            }
        }
    }

    public void showSearchTable(UserList userList, String searchText) {
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

        tableView.getItems().clear();
        for (User student : userList.getUsers()) {
            if ((student.getRole().equals("นักศึกษา") && student.getAdvisor().equals(user.getFullName())) && (student.getFullName().contains(searchText) || student.getId().contains(searchText))) {
                tableView.getItems().add(student);
            }
        }
    }

    // ไปที่หน้าคำร้องของนิสิตในที่ปรึกษา
    @FXML
    public void onStudentAppealButtonClick() {
        try {
            FXRouter.goTo("professor-student-appeal", user);
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