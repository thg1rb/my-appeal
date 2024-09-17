package ku.cs.controllers.major;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.Student;
import ku.cs.models.persons.User;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.collections.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;


public class MajorNisitManageController {
    @FXML private Pane navbarAnchorPane;

    @FXML TableView<User> nisitTableView;

    private UserList userList;
    private Datasource<UserList> datasource;

    private User user;

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

        datasource = new UserListFileDatasource("data", "user.csv");
        userList = datasource.readData();
        user = (User)FXRouter.getData();

        showTable(userList);
    }

    public void showTable(UserList userlist){
        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        nisitTableView.getColumns().clear();
        nisitTableView.getColumns().add(idColumn);
        nisitTableView.getColumns().add(nameColumn);
        nisitTableView.getColumns().add(emailColumn);

        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        emailColumn.setSortable(false);

        nisitTableView.getItems().clear();
        if(userlist != null){
            for(User nisit : userlist.getUsers()){
                if(((Student)nisit).getDepartment().equals(((DepartmentStaff)user).getDepartment()) && nisit.getRole().equals("นักศึกษา")){
                    nisitTableView.getItems().add(nisit);
                }
            }
        }
    }
}
