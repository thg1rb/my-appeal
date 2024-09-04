package ku.cs.controllers.professor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class ProfessorStudentListController {
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TextField searchTextField;

    @FXML private TableView<User> tableView;

    private User user;

    private Datasource<UserList> userDatasource;
    private Datasource<AppealList> appealDatasource;
    private UserList userList;
    private AppealList appealList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        // Datasources (Appeal and User)
        userDatasource = new UserListFileDatasource("data", "user.csv");
        userList = userDatasource.readData();

        appealDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealDatasource.readData();

        // ช่องค้นหา
        showTable(userList);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[a-zA-zก-๙0-9]+$") && !newValue.isEmpty()) {
                showSearchTable(userList, newValue);
            }
            else if (newValue.isEmpty() || newValue.isBlank()) {
                showTable(userList);
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/professor/professor-track-student-appeal.fxml"));
                    Parent root = loader.load();

                    ProfessorTrackStudentAppealController controller = loader.getController();
                    controller.showTable(appealList, newValue);
                    controller.ownerAppealLabel.setText("คำร้องทั้งหมดของ " + newValue.getFullName());

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setAlwaysOnTop(true);
                    stage.setScene(new Scene(root));

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        pathCol.setPrefWidth(295);
        usernameCol.setPrefWidth(295);
        fullnameCol.setPrefWidth(295);
        idCol.setPrefWidth(295);

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

        pathCol.setPrefWidth(295);
        usernameCol.setPrefWidth(295);
        fullnameCol.setPrefWidth(295);
        idCol.setPrefWidth(295);

        tableView.getItems().clear();
        for (User student : userList.getUsers()) {
            if ((student.getRole().equals("นักศึกษา") && student.getAdvisor().equals(user.getFullName())) && (student.getUsername().contains(searchText) || student.getFullName().contains(searchText) || student.getId().contains(searchText))) {
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