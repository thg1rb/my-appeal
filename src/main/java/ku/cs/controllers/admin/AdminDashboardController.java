package ku.cs.controllers.admin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import ku.cs.models.Displayable;
import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminDashboardController {

    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private Label allAppealLabel;
    @FXML private Label successAppealLabel;
    @FXML private Label operatingAppealLabel;
    @FXML private Label rejectAppealLabel;

    @FXML private TreeTableView<Displayable> treeTableView;
    @FXML private Label allUserLabel;
    @FXML private Label facultyStaffLabel;
    @FXML private Label departmentStaffLabel;
    @FXML private Label advisorLabel;
    @FXML private Label studentLabel;

    private User user;

    private Datasource<AppealList> appealDatasource;
    private AppealList appealList;

    private Datasource<FacultyList> facultyDatasource;
    private FacultyList facultyList;
    private Datasource<MajorList> majorDatasource;
    private MajorList majorList;

    private HashMap<String, Integer> numUserTypeMap;

    @FXML
    public void initialize() {
        user = (AdminUser) FXRouter.getData();

        ProgramSetting.getInstance().applyStyles(mainPane);

        loadAppealData();
        loadFacultyAndDepartmentData();
        loadUserData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        showAllStatusAppealsCount();
        showTreeTable();
        showNumUsersInSystem();
    }

    @FXML
    private void showTreeTable(){
        TreeTableColumn<Displayable, String> nameCol = new TreeTableColumn<>("ชื่อ");
        nameCol.setCellValueFactory(cellData ->{
            Displayable item = cellData.getValue().getValue();
            if (item != null) {
                return new SimpleStringProperty(item.getDisplayName());
            }
            return null;
        });

        TreeTableColumn<Displayable, String> successAppealCol = new TreeTableColumn<>("จำนวนคำร้องที่อนุมัติแล้ว");
        successAppealCol.setCellValueFactory(cellData ->{
            Displayable item = cellData.getValue().getValue();
            if (item instanceof Faculty){
                int count = appealList.countSuccessAppealByFacultyUUID(((Faculty)item).getUuid().toString());
                return new SimpleStringProperty(String.valueOf(count));
            } else if (item instanceof Major){
                int count = appealList.countSuccessAppealByDepartmentUUID(((Major)item).getUuid().toString());
                return new SimpleStringProperty(String.valueOf(count));
            }
            return null;
        });

        treeTableView.getColumns().clear();
        treeTableView.getColumns().add(nameCol);
        treeTableView.getColumns().add(successAppealCol);

        TreeItem<Displayable> rootItem = new TreeItem<>();
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);

        for (Faculty faculty : facultyList.getFaculties()){
            TreeItem<Displayable> facultyTreeItem = new TreeItem<>(faculty);
            ArrayList<Major> majorInFacultyList = majorList.getMajorsByFaculty(faculty.getUuid());
            for (Major major : majorInFacultyList){
                TreeItem<Displayable> majorTreeItem = new TreeItem<>(major);
                facultyTreeItem.getChildren().add(majorTreeItem);
            }
            rootItem.getChildren().add(facultyTreeItem);
            facultyTreeItem.setExpanded(true);
        }

        for (TreeTableColumn<?, ?> column : treeTableView.getColumns()){
            column.setPrefWidth((double) 551 /treeTableView.getColumns().size());
        }
    }

    @FXML
    private void showNumUsersInSystem(){
        allUserLabel.setText(numUserTypeMap.get("ทั้งหมด").toString());
        facultyStaffLabel.setText(numUserTypeMap.get("เจ้าหน้าที่คณะ").toString());
        departmentStaffLabel.setText(numUserTypeMap.get("เจ้าหน้าที่ภาควิชา").toString());
        advisorLabel.setText(numUserTypeMap.get("อาจารย์ที่ปรึกษา").toString());
        studentLabel.setText(numUserTypeMap.get("นักศึกษา").toString());
    }

    private void loadAppealData(){
        appealDatasource = new AppealListFileDatasource("data" , "appeal-list.csv");
        appealList = appealDatasource.readData();
    }

    private void loadFacultyAndDepartmentData(){
        facultyDatasource = new FacultyListDatasource("data" , "faculties.csv");
        facultyList = facultyDatasource.readData();
        majorDatasource = new MajorListDatasource("data" , "majors.csv");
        majorList = majorDatasource.readData();
    }

    private void loadUserData(){
        numUserTypeMap = new HashMap<>();
        numUserTypeMap.put("เจ้าหน้าที่คณะ", new UserListDatasource("data" + File.separator + "users", "facultyStaff.csv").readData().getUsers().size());
        numUserTypeMap.put("เจ้าหน้าที่ภาควิชา", new UserListDatasource("data" + File.separator + "users", "majorStaff.csv").readData().getUsers().size());
        numUserTypeMap.put("อาจารย์ที่ปรึกษา", new UserListDatasource("data" + File.separator + "users", "advisor.csv").readData().getUsers().size());
        numUserTypeMap.put("นักศึกษา", new UserListDatasource("data" + File.separator + "users", "student.csv").readData().getRegisteredStudents().getUsers().size());

        int totalUsers = 0;
        for (String key : numUserTypeMap.keySet()) {
            if (!key.equals("ทั้งหมด")) {
                totalUsers += numUserTypeMap.get(key);
            }
        }
        numUserTypeMap.put("ทั้งหมด", totalUsers);
    }

    @FXML
    private void showAllStatusAppealsCount(){
        HashMap<String, Integer> allStatusAppealsCountMap = appealList.getStatusAppealsCount();
        int allAppeals = 0, successAppeals = 0, operatingAppeals = 0, rejectAppeals = 0;

        for (String key : allStatusAppealsCountMap.keySet()){
            int num = allStatusAppealsCountMap.get(key);
            allAppeals += num;
            if (key.contains("ดำเนินการครบถ้วน")){
                successAppeals += num;
            } else if (key.contains("คำร้องถูกปฏิเสธ")){
                rejectAppeals += num;
            } else {
                operatingAppeals += num;
            }
        }
        allAppealLabel.setText(String.valueOf(allAppeals));
        successAppealLabel.setText(String.valueOf(successAppeals));
        operatingAppealLabel.setText(String.valueOf(operatingAppeals));
        rejectAppealLabel.setText(String.valueOf(rejectAppeals));
    }
}
