package ku.cs.controllers.admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.Advisor;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.FacultyListDatasource;
import ku.cs.services.datasources.MajorListDatasource;
import ku.cs.services.datasources.UserListDatasource;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;

public class AdminStaffManagementController {
    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;
    @FXML private TableView<User> tableView;

    @FXML private Text totalText;

    private User user;

    private HashMap<String, Datasource<UserList>> datasourcesMap;
    private HashMap<String, UserList> staffMap;

    private Datasource<UserList> facultyStaffDatasource;
    private Datasource<UserList> majorStaffDatasource;
    private Datasource<UserList> advisorDatasource;
    private Datasource<FacultyList> facultyListDatasource;
    private Datasource<MajorList> majorListDatasource;

    private FacultyList facultyList;
    private MajorList majorList;

    private User selectedStaff;
    private String selectingTab;

    private boolean popupEditMode;

    @FXML
    private void initialize() {
        user = (User) FXRouter.getData();

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

        facultyStaffDatasource = new UserListDatasource("data" + File.separator + "users", "facultyStaff.csv");
        majorStaffDatasource = new UserListDatasource("data" + File.separator + "users", "majorStaff.csv");
        advisorDatasource = new UserListDatasource("data" + File.separator + "users", "advisor.csv");
        initMap();

        facultyListDatasource = new FacultyListDatasource("data", "faculties.csv");
        majorListDatasource = new MajorListDatasource("data", "majors.csv");
        facultyList = facultyListDatasource.readData();
        majorList = majorListDatasource.readData();

        selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();
        showTable(staffMap.get(selectingTab), selectingTab);
        updateTotalText();

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();
            readData();
            showTable(staffMap.get(selectingTab), selectingTab);
            updateTotalText();
        });

        tableView.getItems().addListener((ListChangeListener<? super User>)change -> updateTotalText() );
        tableView.setRowFactory(v->{
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                popupEditMode = true;
                selectedStaff = tableView.getSelectionModel().getSelectedItem();
                addEditPopup();
            });
            return row;
        });
    }

    private void saveData(){
        for (String key : datasourcesMap.keySet()){
            datasourcesMap.get(key).writeData(staffMap.get(key));
        }
    }
    private void readData(){
        for (String key : datasourcesMap.keySet()){
            staffMap.put(key, datasourcesMap.get(key).readData());
        }
        facultyList = facultyListDatasource.readData();
        majorList = majorListDatasource.readData();
    }

    private void showTable(UserList userList, String role) {
        TableColumn<User, ImageView> imgCol = new TableColumn<>("โปรไฟล์");
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

        TableColumn<User, String> usernameCol = new TableColumn<>("ชื่อผู้ใช้งาน");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> initPasswordCol = new TableColumn<>("รหัสผ่านเริ่มต้น");
        initPasswordCol.setCellValueFactory(new PropertyValueFactory<>("initialPasswordText"));

        TableColumn<User, String> facultyCol = new TableColumn<>("คณะ");
//        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        facultyCol.setCellValueFactory(cellData ->{
            FacultyStaff user = (FacultyStaff) cellData.getValue();
            return new SimpleStringProperty(facultyList.findFacultyByUUID(user.getFacultyUUID()).getFacultyName());
        });
        facultyCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
            }
        });

        tableView.getColumns().clear();
        tableView.getColumns().add(imgCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(initPasswordCol);
        tableView.getColumns().add(facultyCol);

        if (role.equals("เจ้าหน้าที่ภาควิชา") || role.equals("อาจารย์ที่ปรึกษา")){
            TableColumn<User, String> majorCol = new TableColumn<>("สาขา");
            majorCol.setCellValueFactory(cellData ->{
                DepartmentStaff user = (DepartmentStaff) cellData.getValue();
                return new SimpleStringProperty(majorList.findMajorByUUID(user.getDepartmentUUID()).getMajorName());
            });

            tableView.getColumns().add(majorCol);
        }
        if (role.equals("อาจารย์ที่ปรึกษา")){
            TableColumn<User, String> idCol = new TableColumn<>("รหัสประจำตัว");
            idCol.setCellValueFactory(cellData ->{
                Advisor user = (Advisor) cellData.getValue();
                return new SimpleStringProperty(user.getAdvisorId());
            });

            tableView.getColumns().add(idCol);
        }

        // Resize Col
        int sizeCol = tableView.getColumns().size();
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setPrefWidth((double) 1100 / sizeCol);
        }
        imgCol.setPrefWidth(imgCol.getPrefWidth() - 50);
        nameCol.setPrefWidth(nameCol.getPrefWidth() + 20);
        facultyCol.setPrefWidth(facultyCol.getPrefWidth() + 30);
        if (!role.equals("เจ้าหน้าที่คณะ")) {
            facultyCol.setPrefWidth(facultyCol.getPrefWidth() - 15);
            TableColumn<?, ?> majorCol = (tableView.getColumns()).get(5);
            majorCol.setPrefWidth(majorCol.getPrefWidth() + 15);
            if (role.equals("อาจารย์ที่ปรึกษา")){
                facultyCol.setPrefWidth(facultyCol.getPrefWidth() - 5);
                majorCol.setPrefWidth(facultyCol.getPrefWidth() + 15);
                TableColumn<?, ?> idCol = (tableView.getColumns()).get(6);
                idCol.setPrefWidth(idCol.getPrefWidth() - 10);
            }
        }

        tableView.getItems().clear();
        for (User user : userList.getUsers()){
            tableView.getItems().add(user);
        }

        tableView.getSortOrder().add(facultyCol);
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setSortable(false);
        }
        tableView.sort();
    }

    private void updateTotalText(){
        totalText.setText("จำนวน" + selectingTab + "ทั้งหมด " + tableView.getItems().size() + " คน");
    }

    private void addEditPopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/admin/admin-addEdit-staff.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            AdminStaffPopupController staffPopup = fxmlLoader.getController();
            staffPopup.initPopup(popupEditMode, selectedStaff, facultyList, majorList, selectingTab, staffMap);

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (staffPopup.isDeleted()){
                staffMap.get(selectedStaff.getRole()).deleteUser(selectedStaff);
                selectedStaff = null;
            }
            saveData();
            readData();
            showTable(staffMap.get(selectingTab), selectingTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initMap(){
        staffMap = new HashMap<>();
        staffMap.put("เจ้าหน้าที่คณะ", facultyStaffDatasource.readData());
        staffMap.put("เจ้าหน้าที่ภาควิชา", majorStaffDatasource.readData());
        staffMap.put("อาจารย์ที่ปรึกษา", advisorDatasource.readData());
        datasourcesMap = new HashMap<>();
        datasourcesMap.put("เจ้าหน้าที่คณะ", facultyStaffDatasource);
        datasourcesMap.put("เจ้าหน้าที่ภาควิชา", majorStaffDatasource);
        datasourcesMap.put("อาจารย์ที่ปรึกษา", advisorDatasource);
    }

    @FXML
    public void onAddStaffButtonClicked() {
        popupEditMode = false;
        addEditPopup();
    }
}
