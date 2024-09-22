package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.io.IOException;


public class StudentTrackAppealController {

    private Datasource<AppealList> appealDatasource;
    private AppealList appealList;

    private Datasource<ModifyDateList> modifyDateDatasource;
    private ModifyDateList modifyDateList;

    private User user;

    @FXML private Pane navbarAnchorPane;

    @FXML private TableView<Appeal> tableView;

    @FXML private Text totalText;

    @FXML private TextField searchTextField;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

        // แสดงข้อมูลภายในตาราง
        appealDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealDatasource.readData();

        modifyDateDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateDatasource.readData();

        showTable(appealList, ((Student)user).getStudentId(), searchTextField.getText());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            showTable(appealList, ((Student)user).getStudentId(), searchTextField.getText());
        });

        tableView.setOnMouseClicked(mouseEvent -> {
            Appeal selectedAppeal = tableView.getSelectionModel().getSelectedItem();
            if (selectedAppeal != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/student/student-show-appeal.fxml"));
                    Parent root = loader.load();

                    StudentShowAppealController controller = loader.getController();
                    controller.setSelectedAppeal(selectedAppeal, modifyDateList.findModifyDateByUuid(selectedAppeal.getUuid()));

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setAlwaysOnTop(true);
                    stage.setScene(new Scene(root));

                    stage.showAndWait();

                    tableView.getSelectionModel().clearSelection();  // Clear the current selection safely
                    showTable(appealList, ((Student)user).getStudentId(), searchTextField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    // ตารางแสดงคำร้องทั้งหมดของนิสิต (default แสดงทั้งหมด)
    private void showTable(AppealList appealList, String ownerId, String searchText) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("Appeal Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        dateTimeCol.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);
        tableView.getColumns().add(statusCol);

        dateTimeCol.setPrefWidth(366.66);
        typeCol.setPrefWidth(366.66);
        statusCol.setPrefWidth(366.66);

        tableView.getItems().clear();
        if (searchText.isEmpty()) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (appeal.getOwnerId().equals(ownerId)) {
                    tableView.getItems().add(appeal);
                }
            }
        } else {
            for (Appeal appeal : appealList.getAppeals()) {
                if (appeal.getOwnerId().equals(ownerId) && (appeal.getType().contains(searchText) || appeal.getStatus().contains(searchText))) {
                    tableView.getItems().add(appeal);
                }
            }
        }

        tableView.getSortOrder().add(dateTimeCol);
        updateTotalText();
    }

    // อัพเดตข้อความแสดงคำร้องทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }
}