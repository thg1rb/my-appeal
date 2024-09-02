package ku.cs.controllers.professor;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;
import ku.cs.services.*;

import java.io.IOException;

public class ProfessorStudentAppealController {

    private User user;

    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TableView<Appeal> tableView;
    private Datasource<AppealList> appealDatasource;
    private Datasource<UserList> userDatasource;
    private AppealList appealList;
    private UserList userList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        appealDatasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = appealDatasource.readData();

        userDatasource = new UserListFileDatasource("data", "user.csv");
        userList = userDatasource.readData();

        showTable(appealList, userList);
    }

    public void showTable(AppealList appealList, UserList userList) {
        TableColumn<Appeal, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        TableColumn<Appeal, String> type = new TableColumn<>("Appeal Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> ownerIdCol = new TableColumn<>("ID");
        ownerIdCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));

        TableColumn<Appeal, String> ownerFullNameCol = new TableColumn<>("Fullname");
        ownerFullNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));

        dateTimeCol.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(dateTimeCol);
        tableView.getColumns().add(type);
        tableView.getColumns().add(ownerIdCol);
        tableView.getColumns().add(ownerFullNameCol);

        tableView.getItems().clear();
        //  Add Appeal filter by Professor name in Student (Not Done Yet)
        for (Appeal appeal : appealList.getAppeals()) {
            for (User student : userList.getUsers()) {
                if (appeal.getOwnerId().equals(student.getId())) {
                    tableView.getItems().add(appeal);
                }
            }
        }

        tableView.getSortOrder().add(dateTimeCol);


    }

    // ไปที่หน้านิสิตในที่ปรึกษา
    @FXML
    public void onStudentListButtonClick() {
        try {
            FXRouter.goTo("professor-student-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ออกจากระบบ (กลับไปที่หน้าเข้าสู่ระบบ)
    @FXML
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
