package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import ku.cs.models.persons.User;
import ku.cs.models.collections.UserList;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserListHardCodeDatasource;

import java.io.IOException;

public class AdminUserManagementController {

    @FXML private TabPane tabPane;
    @FXML private TableView<User> tableView;

    private Datasource<UserList> usersDatasource;
    private UserList userList;

    @FXML
    public void initialize() {
        usersDatasource = new UserListFileDatasource("data", "user.csv");
        userList = usersDatasource.readData();

        showTable(userList);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                showTable(userList);
            } else if (tabPane.getSelectionModel().getSelectedIndex() == 1) {
                showTable(userList, "เจ้าหน้าที่คณะ");
            } else if (tabPane.getSelectionModel().getSelectedIndex() == 2) {
                showTable(userList, "เจ้าหน้าที่ภาควิชา");
            } else if (tabPane.getSelectionModel().getSelectedIndex() == 3) {
                showTable(userList, "อาจารย์ที่ปรึกษา");
            } else {
                showTable(userList, "นักศึกษา");
            }
        });
    }

    private void showTable(UserList userList){
        TableColumn<User, String> imgCol = new TableColumn<>("Profile");
        imgCol.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> loginDateCol = new TableColumn<>("Last Login");
        loginDateCol.setCellValueFactory(new PropertyValueFactory<>("loginDate"));

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(roleCol);
        tableView.getColumns().add(loginDateCol);

        imgCol.setPrefWidth(220);
        nameCol.setPrefWidth(220);
        usernameCol.setPrefWidth(220);
        roleCol.setPrefWidth(220);
        loginDateCol.setPrefWidth(220);

        tableView.getItems().clear();
        for (User user : userList.getUsers()){
            tableView.getItems().add(user);
        }
    }

    private void showTable(UserList userList, String role){
        TableColumn<User, String> imgCol = new TableColumn<>("Profile");
        imgCol.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> loginDateCol = new TableColumn<>("Last Login");
        loginDateCol.setCellValueFactory(new PropertyValueFactory<>("loginDate"));

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(loginDateCol);

        imgCol.setPrefWidth(275);
        nameCol.setPrefWidth(275);
        usernameCol.setPrefWidth(275);
        loginDateCol.setPrefWidth(275);

        tableView.getItems().clear();
        for (User user : userList.getUsers()){
            if (user.getRole().equals(role)){
                tableView.getItems().add(user);
            }
        }
    }

    @FXML
    public void onDashboardButtonClicked() {
        try {
            FXRouter.goTo("admin-dashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onFacultyButtonClicked() {
        try {
            FXRouter.goTo("admin-faculty-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onStaffButtonClicked() {
        try {
            FXRouter.goTo("admin-staff-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
