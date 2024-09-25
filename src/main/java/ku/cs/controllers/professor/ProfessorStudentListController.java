package ku.cs.controllers.professor;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Advisor;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ProfessorStudentListController {

    @FXML private Pane navbarAnchorPane;
    @FXML private Text totalText;
    @FXML private TableView<User> tableView;
    @FXML private TextField searchTextField;

    private User user;
    private Datasource<UserList> studentDatasource;
    private UserList studentList;

    private Datasource<AppealList> appealDatasource;
    private AppealList appealList;

    @FXML
    private void initialize() {
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

        // Student Datasource
        studentDatasource = new UserListDatasource("data" + File.separator + "users", "student.csv");
        studentList = studentDatasource.readData();

        // Appeal Datasource
        appealDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealDatasource.readData();

        // ช่องค้นหา
        showTable(studentList, searchTextField.getText());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            showTable(studentList, newValue);
        });

        // แสดง pop-up เมื่อกดที่เซลล์ใดเซลล์หนึ่งในตาราง
        tableView.setOnMouseClicked(mouseEvent -> {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/professor/professor-track-student-appeal.fxml"));
                    Parent root = loader.load();

                    ProfessorTrackStudentAppealController controller = loader.getController();
                    controller.setSelectedStudent(appealList, (Student) selectedUser);

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

    // ตารางแสดงนิสิตในที่ปรึกษาทั้งหมด (ใช้ร่วมกับ Search Text Field)
    private void showTable(UserList studentList, String searchText) {
        TableColumn<User, ImageView> imgCol = new TableColumn<>("โปรไฟล์");
        imgCol.setCellValueFactory(cellData ->{
            User user = cellData.getValue();
            Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            return new SimpleObjectProperty<>(imageView);
        });

        TableColumn<User, String> usernameCol = new TableColumn<>("ชื่อผู้ใช้");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> fullnameCol = new TableColumn<>("ชื่อ-สกุล");
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> idCol = new TableColumn<>("รหัสนิสิต");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(fullnameCol);
        tableView.getColumns().add(idCol);

        imgCol.setPrefWidth(275);
        usernameCol.setPrefWidth(275);
        fullnameCol.setPrefWidth(275);
        idCol.setPrefWidth(275);

        // Add Student filter by Professer name
        tableView.getItems().clear();
        if (searchText.isEmpty()) {
            for (User student : studentList.getUsers()) {
                UUID studentAdvisorUUID = ((Student) student).getAdvisorUUID();
                if (studentAdvisorUUID != null && studentAdvisorUUID.equals(user.getUuid())){
                    tableView.getItems().add(student);
                }
            }
        } else {
            for (User student : studentList.getUsers()) {
                UUID studentAdvisorUUID = ((Student) student).getAdvisorUUID();
                if (studentAdvisorUUID != null && studentAdvisorUUID.equals(user.getUuid()) && (student.getUsername().contains(searchText) || student.getFullName().contains(searchText) || ((Student) student).getStudentId().contains(searchText))) {
                    tableView.getItems().add(student);
                }
            }
        }
        updateTotalText();
    }

    // อัพเดทข้อความแสดงจำนวนนิสิตในที่ปรึกษาทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องของนิสิตในที่ปรึกษาทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }
}