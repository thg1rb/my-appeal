package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;


public class StudentTrackAppealController {

    private Datasource<AppealList> datasource;
    private AppealList appealList;
    private User user;

    @FXML private Pane navbarAnchorPane;

    @FXML private TableView<Appeal> tableView;

    @FXML private Text totalText;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

        // แสดงข้อมูลภายในตาราง
        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        showTable(appealList, ((Student)user).getStudentId());

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

    // ตารางแสดงคำร้องทั้งหมดของนิสิต
    private void showTable(AppealList appealList, String ownerId) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        TableColumn<Appeal, String> typeCol = new TableColumn<>("Appeal Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        dateTimeCol.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(typeCol);

        tableView.getItems().clear();
        for (Appeal appeal : appealList.getAppeals()) {
            if (appeal.getOwnerId().equals(ownerId)) {
                tableView.getItems().add(appeal);
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