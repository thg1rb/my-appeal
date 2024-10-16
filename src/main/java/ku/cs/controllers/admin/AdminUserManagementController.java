package ku.cs.controllers.admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;
import ku.cs.models.collections.UserList;

import ku.cs.services.DateTimeService;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;
import java.util.HashMap;

public class AdminUserManagementController {
    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;
    @FXML private TableView<User> tableView;

    @FXML private TextField searchTextField;

    private User user;

    private HashMap<String, Datasource<UserList> > datasourceMap;
    private HashMap<String, UserList> userInSystemMap;

    private String selectedTab;
    private String searchText;

    @FXML
    public void initialize() {
        user = (AdminUser) FXRouter.getData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        initMap();
        selectedTab = tabPane.getSelectionModel().getSelectedItem().getText();
        searchText = searchTextField.getText();

        showTable(userInSystemMap.get(selectedTab), searchTextField.getText());

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            selectedTab = tabPane.getSelectionModel().getSelectedItem().getText();
            showTable(userInSystemMap.get(selectedTab), searchTextField.getText());
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            showTable(userInSystemMap.get(selectedTab), searchText);
        });
    }

    private void showTable(UserList userList, String searchText){
        basicInfoColCreator(!selectedTab.equals("ทั้งหมด"));
        if (!searchText.isEmpty()) {
            for (User user : userList.getUsers()) {
                if (user.getFullName().contains(searchText)) {
                    tableView.getItems().add(user);
                }
            }
        }else{
            for (User user : userList.getUsers()) {
                tableView.getItems().add(user);
            }
        }

        tableView.sort();
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setSortable(false);
        }
    }

    private void basicInfoColCreator(boolean roleSpecific) {
        TableColumn<User, ImageView> imgCol = new TableColumn<>("Profile");
        imgCol.setCellValueFactory(cellData ->{
            User user = cellData.getValue();
            Image image = new Image("file:data" + File.separator + "profile-images" + File.separator + user.getProfileUrl());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            return new SimpleObjectProperty<>(imageView);
        });

        TableColumn<User, String> nameCol = new TableColumn<>("ชื่อ-สกุล");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> usernameCol = new TableColumn<>("ชื่อผู้ใช้");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("ตำแหน่ง");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> loginDateCol = new TableColumn<>("ล็อคอินล่าสุด");
        loginDateCol.setCellValueFactory(cellData ->{
            String loginDate = cellData.getValue().getLoginDate();
            if (loginDate == null || loginDate.isEmpty() || loginDate.equals("null")) {
                return new SimpleStringProperty("ไม่เคยเข้าใช้งาน");
            } else {
                return new SimpleStringProperty(loginDate);
            }
        });

        TableColumn<User, String> banCol = new TableColumn<>("สิทธิ์ในการใช้งาน");
        banCol.setCellValueFactory(cellData -> {
            User item = cellData.getValue();
            String value = item.getAccesibility();
            return new SimpleStringProperty(value);
        });

        //Add col
        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        if (!roleSpecific) {
            tableView.getColumns().add(roleCol);
        }
        tableView.getColumns().add(loginDateCol);
        tableView.getColumns().add(banCol);

        //Resize Col
        int colWidth = roleSpecific ? 220 : 183;
        imgCol.setPrefWidth(colWidth-60);
        nameCol.setPrefWidth(colWidth+30);
        usernameCol.setPrefWidth(colWidth+15);
        roleCol.setPrefWidth(colWidth);
        loginDateCol.setPrefWidth(colWidth+15);
        banCol.setPrefWidth(colWidth);

        //Clear old item and set sort for table
        loginDateCol.setComparator(new DateTimeService());
        tableView.getSortOrder().add(loginDateCol);
        tableView.getItems().clear();
    }

    private void saveData(User user, boolean ban){
        if (user.getRole().equals("นักศึกษา")){
            UserList allStudents = datasourceMap.get(user.getRole()).readData();
            User savedStudent = allStudents.findUserByUUID(user.getUuid());
            if (ban)savedStudent.banUser();
            else savedStudent.unbanUser();
            datasourceMap.get(user.getRole()).writeData(allStudents);
        }else {
            datasourceMap.get(user.getRole()).writeData(userInSystemMap.get(user.getRole()));
        }
    }

    private void initMap(){
        userInSystemMap = new HashMap<>();
        datasourceMap = new HashMap<>();

        datasourceMap.put("เจ้าหน้าที่คณะ", new UserListDatasource("data" + File.separator + "users", "facultyStaff.csv"));
        datasourceMap.put("เจ้าหน้าที่ภาควิชา", new UserListDatasource("data" + File.separator + "users", "departmentStaff.csv"));
        datasourceMap.put("อาจารย์ที่ปรึกษา", new UserListDatasource("data" + File.separator + "users", "advisor.csv"));
        datasourceMap.put("นักศึกษา" , new UserListDatasource("data" + File.separator + "users", "student.csv"));

        userInSystemMap.put("ทั้งหมด", new UserList());
        for (String key : datasourceMap.keySet()) {
            if (key.equals("นักศึกษา")){
                userInSystemMap.put(key, datasourceMap.get(key).readData().getRegisteredStudents());
            }else {
                userInSystemMap.put(key, datasourceMap.get(key).readData());
            }
            userInSystemMap.get("ทั้งหมด").addUserLists(userInSystemMap.get(key));
        }
    }

    @FXML
    public void onBanButtonClicked(){
        User user = tableView.getSelectionModel().getSelectedItem();
        user.banUser();
        saveData(user, true);
        tableView.refresh();
    }

    @FXML
    public void onUnBanButtonClicked(){
        User user = tableView.getSelectionModel().getSelectedItem();
        user.unbanUser();
        saveData(user, false);
        tableView.refresh();
    }
}
