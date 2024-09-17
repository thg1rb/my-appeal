package ku.cs.controllers.major;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.collections.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.File;
import java.io.IOException;


public class MajorNisitManageController {
    @FXML private Pane navbarAnchorPane;

    @FXML private TableView<Student> nisitTableView;

    private UserList studentList;
    private Datasource<UserList> datasource;
    private Student selectedNisit;
    private DepartmentStaff user;
    public boolean addMode = false;

    public void initialize() {
        user = (DepartmentStaff) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        datasource = new UserListFileDatasource("data" + File.separator + "users", "student.csv");
        studentList = datasource.readData();

        showTable(studentList);
        nisitTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observableValue, Student oldValue, Student newValue) {
                if (newValue != null) {
                    selectedNisit = newValue;
                    showPopUp(addMode);
                }
            }
        });
    }
    public void onAddButtonClick(){
        addMode = true;
        showPopUp(addMode);
    }
    public void showTable(UserList studentRoster) {
        TableColumn<Student, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

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
                if (((Student)nisit).getDepartment().equals(user.getDepartment())) {
                    nisitTableView.getItems().add((Student) nisit);
                }
            }
        }
    }

    public void showPopUp(boolean addMode){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/major/nisitEditPopup.fxml"));
            Parent popuproot = fxmlLoader.load();
            MajorNisitEditPopupController controller = fxmlLoader.getController();

            if(!addMode){
                controller.setNisit(selectedNisit, user);
                controller.setMode(addMode);
            }
            else{
                controller.setUser(user, studentList);
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
        catch(IOException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onApproverManageButtonClick() {
        try {
            FXRouter.goTo("major-approver-manage", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAppealManageButtonClick() {
        try {
            FXRouter.goTo("major-appeal-manage", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onLogoutButtonClick(){
        try{
            FXRouter.goTo("login");
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
