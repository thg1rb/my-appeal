package ku.cs.controllers.major;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.collections.StudentList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentRosterListFileDatasource;

import java.io.IOException;

public class MajorNisitManageController {

    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label topicLabel;
    @FXML private TableView<Student> nisitTableView;

    private StudentList studentRoster;
    private Datasource<StudentList> rosterDatasource;
    private Datasource<StudentList> datasource;
    private Student selectedNisit;
    private User user;
    public boolean addMode = false;

    public void initialize() {
        user = (User) FXRouter.getData();

//        usernameLabel.setText(user.getUsername());
//        roleLabel.setText(user.getRole());
        datasource = new StudentRosterListFileDatasource("data", "student-roster.csv");
        studentRoster = datasource.readData();
        user = (User)FXRouter.getData();

        showTable(studentRoster);
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
    public void showTable(StudentList studentRoster){
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
        if(studentRoster != null){
            for(Student nisit : studentRoster.getStudents()){
                if(nisit.getMajor().equals(user.getMajor())){
                    nisitTableView.getItems().add(nisit);
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
                controller.setUser(user, studentRoster);
                controller.setMode(addMode);
            }

            Stage popupStage = new Stage();

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setAlwaysOnTop(true);
            popupStage.setScene(new Scene(popuproot));

            popupStage.showAndWait();

            datasource.writeData(studentRoster);
            studentRoster = datasource.readData();

            showTable(studentRoster);
        }
        catch(IOException e){
            e.printStackTrace();
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
