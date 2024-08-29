package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListHardCodeDatasource;

import java.io.IOException;

public class AdminStaffManagementController {
    @FXML private TabPane tabPane;

    @FXML private TableView<User> facultyTableView;
    @FXML private TableView<User> majorTableView;
    @FXML private TableView<User> professorTableView;

    private Datasource<UserList> datasource;
    private UserList userList;

    @FXML
    private void initialize() {
        datasource = new UserListHardCodeDatasource();
        userList = datasource.readData();

        showTable(userList, "เจ้าหน้าที่คณะ");
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                showTable(userList, "เจ้าหน้าที่คณะ");
            }
            else if (tabPane.getSelectionModel().getSelectedIndex() == 1) {
                showTable(userList, "เจ้าหน้าที่ภาควิชา");
            }
            else{
               showTable(userList, "อาจารย์ที่ปรึกษา");
            }
        });
    }

    private void showTable(UserList userList, String role) {
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> initPasswordCol = new TableColumn<>("Initial Password");
        initPasswordCol.setCellValueFactory(new PropertyValueFactory<>("initialPasswordText"));

        TableColumn<User, String> facultyCol = new TableColumn<>("Faculty");
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        if (role.equals("เจ้าหน้าที่ภาควิชา")){
            TableColumn<User, String> majorCol = new TableColumn<>("Major");
            majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));

            majorTableView.getColumns().clear();
            majorTableView.getColumns().add(nameCol);
            majorTableView.getColumns().add(usernameCol);
            majorTableView.getColumns().add(initPasswordCol);
            majorTableView.getColumns().add(facultyCol);
            majorTableView.getColumns().add(majorCol);

            nameCol.setPrefWidth(220);
            usernameCol.setPrefWidth(220);
            facultyCol.setPrefWidth(220);
            initPasswordCol.setPrefWidth(220);
            majorCol.setPrefWidth(220);

            majorTableView.getItems().clear();
            for (User user : userList.getUsers()){
                if (user.getRole().equals(role)){
                    majorTableView.getItems().add(user);
                }
            }

        }else if (role.equals("เจ้าหน้าที่คณะ")) {
            facultyTableView.getColumns().clear();
            facultyTableView.getColumns().add(nameCol);
            facultyTableView.getColumns().add(usernameCol);
            facultyTableView.getColumns().add(initPasswordCol);
            facultyTableView.getColumns().add(facultyCol);

            nameCol.setPrefWidth(275);
            usernameCol.setPrefWidth(275);
            facultyCol.setPrefWidth(275);
            initPasswordCol.setPrefWidth(275);

            facultyTableView.getItems().clear();
            for (User user : userList.getUsers()){
                if (user.getRole().equals(role)){
                    facultyTableView.getItems().add(user);
                }
            }
        }else{
            TableColumn<User, String> majorCol = new TableColumn<>("Major");
            majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));

            TableColumn<User, String> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

            professorTableView.getColumns().clear();
            professorTableView.getColumns().add(nameCol);
            professorTableView.getColumns().add(idCol);
            professorTableView.getColumns().add(usernameCol);
            professorTableView.getColumns().add(initPasswordCol);
            professorTableView.getColumns().add(facultyCol);
            professorTableView.getColumns().add(majorCol);

            nameCol.setPrefWidth(184);
            usernameCol.setPrefWidth(183);
            facultyCol.setPrefWidth(183);
            initPasswordCol.setPrefWidth(183);
            majorCol.setPrefWidth(183);
            idCol.setPrefWidth(183);

            professorTableView.getItems().clear();
            for (User user : userList.getUsers()){
                if (user.getRole().equals(role)){
                    professorTableView.getItems().add(user);
                }
            }
        }
    }

    @FXML
    public void onUserButtonClicked() {
        try {
            FXRouter.goTo("admin-user-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
