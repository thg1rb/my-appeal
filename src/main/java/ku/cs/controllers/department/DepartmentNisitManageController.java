package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.collections.UserList;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;
import java.io.IOException;

public class DepartmentNisitManageController {
    @FXML private AnchorPane mainPane;
    @FXML private Pane navbarAnchorPane;
    @FXML private TextField searchTextField;

    @FXML private TableView<Student> nisitTableView;

    @FXML private Label totalLabel;

    private UserList studentList;
    private Datasource<UserList> datasource;
    private User selectedNisit;
    private User user;
    public boolean addMode = false;

    public void initialize() {
        user = (DepartmentStaff) FXRouter.getData();

        datasource = new UserListDatasource("data" + File.separator + "users", "student.csv");
        studentList = datasource.readData();

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

        showTable(studentList);
        // ช่องค้นหา
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[a-zA-zก-๙0-9]+$") && !newValue.isEmpty()) {
                showSearchTable(studentList, newValue);
            }
            else if (newValue.isEmpty() || newValue.isBlank()) {
                showTable(studentList);
            }
        });

        nisitTableView.setOnMouseClicked(event ->{
            selectedNisit = nisitTableView.getSelectionModel().getSelectedItem();
            addMode = false;
            showPopUp(addMode);
        });

    }

    public void addNisitOnButtonClick(){
        addMode = true;
        showPopUp(addMode);
    }

    public void showTable(UserList studentList) {
        TableColumn<Student, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        nisitTableView.getColumns().clear();
        nisitTableView.getColumns().add(idColumn);
        nisitTableView.getColumns().add(nameColumn);
        nisitTableView.getColumns().add(emailColumn);

        idColumn.setPrefWidth(367);
        nameColumn.setPrefWidth(366);
        emailColumn.setPrefWidth(366);

        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        emailColumn.setSortable(false);

        nisitTableView.getItems().clear();
        if (studentList != null) {
            for (User nisit : studentList.getUsers()) {
                if (((Student)nisit).getDepartment().equals(((DepartmentStaff)user).getDepartment())) {
                    nisitTableView.getItems().add((Student) nisit);
                }
            }
        }
        updateTotalLabel();
    }
    public void showSearchTable(UserList studentList, String searchText) {
        TableColumn<Student, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        nisitTableView.getColumns().clear();
        nisitTableView.getColumns().add(idColumn);
        nisitTableView.getColumns().add(nameColumn);
        nisitTableView.getColumns().add(emailColumn);

        idColumn.setPrefWidth(367);
        nameColumn.setPrefWidth(366);
        emailColumn.setPrefWidth(366);

        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        emailColumn.setSortable(false);

        nisitTableView.getItems().clear();
        for (User nisit : studentList.getUsers()) {
            if (nisit.getRole().equals("นักศึกษา") && (nisit.getUsername().contains(searchText) || nisit.getFullName().contains(searchText) || ((Student) nisit).getStudentId().contains(searchText))) {
                nisitTableView.getItems().add((Student)nisit);
            }
        }
        updateTotalLabel();
    }

    public void showPopUp(boolean addMode){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/department/nisit-edit-popup.fxml"));
            Parent popuproot = fxmlLoader.load();
            DepartmentNisitEditPopupController controller = fxmlLoader.getController();

            controller.setUser(((DepartmentStaff)user) , studentList);
            if(!addMode){
                controller.setMode(addMode);
                controller.setNisit(selectedNisit);
            }
            else{
                controller.setMode(addMode);
            }

            Stage popupStage = new Stage();

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setAlwaysOnTop(true);
            popupStage.setScene(new Scene(popuproot));

            popupStage.showAndWait();

            datasource.writeData(studentList);
            studentList = datasource.readData();
            showTable(studentList);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTotalLabel() {
        totalLabel.setText("จำนวนนิสิตทั้งหมด " + nisitTableView.getItems().size() + " คน");
    }
}
