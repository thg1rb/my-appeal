package ku.cs.controllers.student;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ku.cs.controllers.general.AppealDetailsController;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.ProgramSetting;
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

    private int tmpTableSize = 0;

    @FXML private AnchorPane mainPane;

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

        ProgramSetting.getInstance().applyStyles(mainPane);

        showTable(appealList, ((Student)user).getStudentId(), searchTextField.getText());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            showTable(appealList, ((Student)user).getStudentId(), searchTextField.getText());
        });

        tableView.setOnMouseClicked(mouseEvent -> {
            Appeal selectedAppeal = tableView.getSelectionModel().getSelectedItem();
            if (selectedAppeal != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/appeal-details.fxml"));
                    Parent root = loader.load();

                    AppealDetailsController controller = loader.getController();
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
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("วันเวลาที่สถานะเปลี่ยนแปลง");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("ประเภทของคำร้อง");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        dateTimeCol.setComparator(new DateTimeService());
        TableColumn<Appeal, String> statusCol = new TableColumn<>("สถานะของคำร้อง");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        dateTimeCol.setComparator(new DateTimeService());

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);
        tableView.getColumns().add(statusCol);

        dateTimeCol.setPrefWidth(366.66);
        typeCol.setPrefWidth(366.66);
        statusCol.setPrefWidth(366.66);

        tmpTableSize = tableView.getItems().size();

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
        dateTimeCol.setSortable(false);
        typeCol.setSortable(false);
        statusCol.setSortable(false);
        updateTotalText();

    }

    // อัพเดตข้อความแสดงคำร้องทั้งหมด
    private void updateTotalText() {
        totalText.setText("คำร้องทั้งหมด " + tableView.getItems().size() + " คำร้อง");
    }
}