package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.User;
import ku.cs.models.collections.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListHardCodeDatasource;

import java.io.IOException;

public class AdminUserManagementController {

    @FXML private TableView<User> allRoleTableView;

    private Datasource<UserList> usersDatasource;
    private UserList userList;

    @FXML
    public void initialize() {
        usersDatasource = new UserListHardCodeDatasource();
        userList = usersDatasource.readData();

        showTable(userList);
    }

    private void showTable(UserList userList){
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, Double> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        allRoleTableView.getColumns().clear();
        allRoleTableView.getColumns().add(nameColumn);
        allRoleTableView.getColumns().add(usernameColumn);
        allRoleTableView.getColumns().add(roleColumn);

        allRoleTableView.getItems().clear();

        for (User user: userList.getUsers()) {
            allRoleTableView.getItems().add(user);
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
