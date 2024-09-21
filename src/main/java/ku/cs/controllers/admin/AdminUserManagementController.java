package ku.cs.controllers.admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;
import ku.cs.models.collections.UserList;

import ku.cs.services.DateTimeService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;

public class AdminUserManagementController {
    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;
    @FXML private TableView<User> tableView;

    @FXML private TextField searchTextField;

    private User user;
    private UserList userList;

    @FXML
    public void initialize() {
        user = (AdminUser) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        userList = UserListDatasource.readAllUsers();

        showTable(userList, "");

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                showTable(userList, "");
            } else {
                showRoleTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[a-zA-Zก-๙0-9]+$") && !newValue.isEmpty()) {
                tabPane.getSelectionModel().select(0);
                showTable(userList, newValue);
            }else if (newValue.isEmpty() || newValue.isBlank()) {
                if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                    showTable(userList, "");
                }else {
                    showRoleTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
                }
            }
        });
    }

    private void showTable(UserList userList, String searchText){
        basicInfoColCreator(183, false);
        if (!searchText.isEmpty()) {
            for (User user : userList.getUsers()) {
                if (user.getFullName().contains(searchText) && !user.getLoginDate().equals("null")) {
                    tableView.getItems().add(user);
                }
            }
        }else{
            for (User user : userList.getUsers()) {
                if (!user.getLoginDate().equals("null")) tableView.getItems().add(user);
            }
        }
        tableView.sort();
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setSortable(false);
        }
    }
    private void showRoleTable(UserList userList, String role){
        basicInfoColCreator(220, true);
        for (User user : userList.getUsers()){
            if (user.getRole().equals(role) && !user.getLoginDate().equals("null")){
                tableView.getItems().add(user);
            }
        }
        tableView.sort();
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setSortable(false);
        }
    }

    private void basicInfoColCreator(int colWidhth, boolean roleSpecific) {
        TableColumn<User, ImageView> imgCol = new TableColumn<>("Profile");
        imgCol.setCellValueFactory(cellData ->{
            User user = cellData.getValue();
            Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            return new SimpleObjectProperty<>(imageView);
        });

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> loginDateCol = new TableColumn<>("Last Login");
        loginDateCol.setCellValueFactory(new PropertyValueFactory<>("loginDate"));
        loginDateCol.setComparator(new DateTimeService());

        TableColumn<User, String> banCol = new TableColumn<>("Accessibility");
        banCol.setCellValueFactory(cellData -> {
            User item = cellData.getValue();
            String value = item.getAccesibility();
            return new SimpleStringProperty(value);
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        if (!roleSpecific) {
            tableView.getColumns().add(roleCol);
        }
        tableView.getColumns().add(loginDateCol);
        tableView.getColumns().add(banCol);

        imgCol.setPrefWidth(colWidhth);
        nameCol.setPrefWidth(colWidhth);
        usernameCol.setPrefWidth(colWidhth);
        roleCol.setPrefWidth(colWidhth);
        loginDateCol.setPrefWidth(colWidhth);
        banCol.setPrefWidth(colWidhth);

        tableView.getSortOrder().add(loginDateCol);

        tableView.getItems().clear();
    }

    private void saveData(User user){
        String roleUpdated = user.getRoleInEnglish();
        Datasource<UserList> userUpdatedDatasource = new UserListDatasource("data" + File.separator + "users", roleUpdated + ".csv");
        UserList updateList = userList.getSpecificRoleUser(roleUpdated);
        userUpdatedDatasource.writeData(updateList);
    }

    @FXML
    public void onBanButtonClicked(){
        User user = tableView.getSelectionModel().getSelectedItem();
        user.banUser();
        saveData(user);
        tableView.refresh();
    }

    @FXML
    public void onUnBanButtonClicked(){
        User user = tableView.getSelectionModel().getSelectedItem();
        user.unbanUser();
        saveData(user);
        tableView.refresh();
    }
}
