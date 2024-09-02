package ku.cs.controllers.admin;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class AdminStaffManagementController {
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TabPane tabPane;

    @FXML private TableView<User> tableView;
    @FXML private Text totalText;

    private User user;
    private Datasource<UserList> datasource;
    private UserList userList;

    private boolean popupEditMode;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();

        showTable(userList, "เจ้าหน้าที่คณะ");
        updateTotalText();

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            showTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
            updateTotalText();
        });

        tableView.getItems().addListener((ListChangeListener<? super User>)change -> updateTotalText() );

        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                popupEditMode = true;
                addEditPopup(newValue);
                tableView.getSelectionModel().select(newValue);
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

        tableView.getColumns().clear();

        if (role.equals("เจ้าหน้าที่ภาควิชา")){
            TableColumn<User, String> majorCol = new TableColumn<>("Major");
            majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));

            tableView.getColumns().add(nameCol);
            tableView.getColumns().add(usernameCol);
            tableView.getColumns().add(initPasswordCol);
            tableView.getColumns().add(facultyCol);
            tableView.getColumns().add(majorCol);

            nameCol.setPrefWidth(220);
            usernameCol.setPrefWidth(220);
            facultyCol.setPrefWidth(220);
            initPasswordCol.setPrefWidth(220);
            majorCol.setPrefWidth(220);
        }else if (role.equals("เจ้าหน้าที่คณะ")) {
            tableView.getColumns().add(nameCol);
            tableView.getColumns().add(usernameCol);
            tableView.getColumns().add(initPasswordCol);
            tableView.getColumns().add(facultyCol);

            nameCol.setPrefWidth(275);
            usernameCol.setPrefWidth(275);
            facultyCol.setPrefWidth(275);
            initPasswordCol.setPrefWidth(275);
        }else{
            TableColumn<User, String> majorCol = new TableColumn<>("Major");
            majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));

            TableColumn<User, String> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

            tableView.getColumns().add(nameCol);
            tableView.getColumns().add(idCol);
            tableView.getColumns().add(usernameCol);
            tableView.getColumns().add(initPasswordCol);
            tableView.getColumns().add(facultyCol);
            tableView.getColumns().add(majorCol);

            nameCol.setPrefWidth(184);
            usernameCol.setPrefWidth(183);
            facultyCol.setPrefWidth(183);
            initPasswordCol.setPrefWidth(183);
            majorCol.setPrefWidth(183);
            idCol.setPrefWidth(183);
        }

        tableView.getItems().clear();
        for (User user : userList.getUsers()){
            if (user.getRole().equals(role)){
                tableView.getItems().add(user);
            }
        }
    }

    public void addStaff(User staff){
        userList.addUser(staff);
        showTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
    }

    public boolean getPopupEditMode(){
        return popupEditMode;
    }

    private void updateTotalText(){
        totalText.setText("จำนวน"+tabPane.getSelectionModel().getSelectedItem().getText()+"ทั้งหมด "+tableView.getItems().size()+" คน");
    }

    private void addEditPopup(User user){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/admin/admin-addEdit-staff.fxml"));
        try{
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            StaffPopup staffPopup = fxmlLoader.getController();
            staffPopup.setMainController(this);
            staffPopup.setSelectedRole(tabPane.getSelectionModel().getSelectedItem().getText());
            staffPopup.setPerson(user);
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            datasource.writeData(userList);
            showTable(userList, tabPane.getSelectionModel().getSelectedItem().getText());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddStaffButtonClicked() {
        popupEditMode = false;
        addEditPopup(null);
    }

    @FXML
    public void onUserButtonClicked() {
        try {
            FXRouter.goTo("admin-user-manage", user);
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
            FXRouter.goTo("admin-faculty-manage", user);
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
