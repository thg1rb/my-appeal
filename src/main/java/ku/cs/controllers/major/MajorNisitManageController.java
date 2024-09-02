package ku.cs.controllers.major;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.collections.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.models.persons.User;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class MajorNisitManageController {
    @FXML TableView<User> nisitTableView;

    private UserList userList;
    private Datasource<UserList> datasource;
    private User user;

    public void initialize() {
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
                if(nisit.getMajor().equals(user.getMajor()) && nisit.getRole().equals("นักศึกษา")){
                    nisitTableView.getItems().add(nisit);
                }
            }
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
