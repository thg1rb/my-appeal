package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.Pane;
import ku.cs.models.persons.User;
import ku.cs.models.collections.UserList;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;


public class AdminUserManagementController {
    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;
    @FXML private TableView<User> tableView;

    @FXML private TextField searchTextField;

    private User user;
    private Datasource<UserList> usersDatasource;
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

        usersDatasource = new UserListFileDatasource("data", "user.csv");
        userList = usersDatasource.readData();

        showTable(userList);

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                showTable(userList);
            } else {
                showTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[a-zA-Zก-๙0-9]+$") && !newValue.isEmpty()) {
                tabPane.getSelectionModel().select(0);
                showSearchTable(userList, newValue);
            }else if (newValue.isEmpty() || newValue.isBlank()) {
                if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                    showTable(userList);
                }else {
                    showTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
                }
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

        TableColumn<User, String> banCol = new TableColumn<>("Accessibility");
        banCol.setCellValueFactory(new PropertyValueFactory<>("ban"));

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(roleCol);
        tableView.getColumns().add(loginDateCol);

        tableView.getColumns().add(banCol);

        imgCol.setPrefWidth(184);
        nameCol.setPrefWidth(183);
        usernameCol.setPrefWidth(183);
        roleCol.setPrefWidth(183);
        loginDateCol.setPrefWidth(183);
        banCol.setPrefWidth(183);

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

        TableColumn<User, String> banCol = new TableColumn<>("Accessibility");
        banCol.setCellValueFactory(new PropertyValueFactory<>("ban"));

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(loginDateCol);
        tableView.getColumns().add(banCol);

        imgCol.setPrefWidth(220);
        nameCol.setPrefWidth(220);
        usernameCol.setPrefWidth(220);
        loginDateCol.setPrefWidth(220);
        banCol.setPrefWidth(220);

        tableView.getItems().clear();
        for (User user : userList.getUsers()){
            if (user.getRole().equals(role)){
                tableView.getItems().add(user);
            }
        }
    }

    private void showSearchTable(UserList userList, String searchText){
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

        TableColumn<User, String> banCol = new TableColumn<>("Accessibility");
        banCol.setCellValueFactory(new PropertyValueFactory<>("ban"));

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(roleCol);
        tableView.getColumns().add(loginDateCol);
        tableView.getColumns().add(banCol);

        imgCol.setPrefWidth(184);
        nameCol.setPrefWidth(183);
        usernameCol.setPrefWidth(183);
        roleCol.setPrefWidth(183);
        loginDateCol.setPrefWidth(183);
        banCol.setPrefWidth(183);

        tableView.getItems().clear();
        for (User user : userList.getUsers()){
            if (user.getFullName().contains(searchText)){
                tableView.getItems().add(user);
            }
        }

    }

    private void saveData(){
        usersDatasource.writeData(userList);
    }

    @FXML
    public void onBanButtonClicked(){
        User user = tableView.getSelectionModel().getSelectedItem();
        user.banUser();
        saveData();
        userList = usersDatasource.readData();
    }

    @FXML
    public void onUnBanButtonClicked(){
        User user = tableView.getSelectionModel().getSelectedItem();
        user.unbanUser();
        saveData();
        userList = usersDatasource.readData();
    }
}
