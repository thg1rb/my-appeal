package ku.cs.controllers.professor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import ku.cs.services.*;

import java.io.IOException;

public class ProfessorStudentAppealController {

    private User user;

    @FXML private Pane navbarAnchorPane;

    @FXML private TableView<Appeal> tableView;
    private Datasource<AppealList> appealDatasource;
    private Datasource<UserList> userDatasource;
    private AppealList appealList;
    private UserList userList;

    @FXML
    public void initialize() {
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
                if (appeal.getOwnerId().equals(((Student)student).getStudentId())) {
                    tableView.getItems().add(appeal);
                }
            }
        }

        tableView.getSortOrder().add(dateTimeCol);


    }
}
