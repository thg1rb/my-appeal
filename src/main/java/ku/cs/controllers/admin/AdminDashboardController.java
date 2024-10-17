package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import ku.cs.models.Department;
import ku.cs.models.Displayable;
import ku.cs.models.Faculty;
import ku.cs.models.collections.AppealList;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.DepartmentList;
import ku.cs.models.collections.UserList;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.*;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AdminDashboardController {

    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private Label allAppealLabel;
    @FXML private Label successAppealLabel;
    @FXML private Label operatingAppealLabel;
    @FXML private Label rejectAppealLabel;

    @FXML private TreeTableView<Displayable> treeTableView;
    @FXML private ToggleButton inSystemToggleButton;
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
    private Datasource<DepartmentList> departmentDatasource;
    private DepartmentList departmentList;

    private HashMap<String, UserList> userListHashMap;

    private Thread fileWatcherThread;

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
            AdminNavbarController adminNavbarController = navbarComponentLoader.getController();
            adminNavbarController.setAdminDashboardController(this);
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        showAllStatusAppealsCount();
        showTreeTable();
        showNumUsersInSystem(null);

        treeTableView.setRowFactory(v->{
            TreeTableRow<Displayable> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                showNumUsersInSystem(row.getItem());
            });
            return row;
        });

        inSystemToggleButton.setOnMouseClicked(v->{
            if (treeTableView.getSelectionModel().getSelectedItem() != null) {
                showNumUsersInSystem(treeTableView.getSelectionModel().getSelectedItem().getValue());
            } else{
                showNumUsersInSystem(null);
            }
        });

        startWatchingFiles();
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
                int count = appealList.countSuccessAppealByFacultyUUID(((Faculty)item).getUuid());
                return new SimpleStringProperty(String.valueOf(count));
            } else if (item instanceof Department){
                int count = appealList.countSuccessAppealByDepartmentUUID(((Department)item).getUuid());
                return new SimpleStringProperty(String.valueOf(count));
            }
            return null;
        });
        successAppealCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o2) - Integer.parseInt(o1);
            }
        });

        treeTableView.getColumns().clear();
        treeTableView.getColumns().add(nameCol);
        treeTableView.getColumns().add(successAppealCol);

        TreeItem<Displayable> rootItem = new TreeItem<>();
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);

        for (Faculty faculty : facultyList.getFaculties()){
            TreeItem<Displayable> facultyTreeItem = new TreeItem<>(faculty);
            ArrayList<Department> departmentInFacultyList = departmentList.getDepartmentsByFaculty(faculty.getUuid());
            for (Department department : departmentInFacultyList){
                TreeItem<Displayable> departmentTreeItem = new TreeItem<>(department);
                facultyTreeItem.getChildren().add(departmentTreeItem);
            }
            rootItem.getChildren().add(facultyTreeItem);
            facultyTreeItem.setExpanded(true);
        }

        for (TreeTableColumn<?, ?> column : treeTableView.getColumns()){
            column.setPrefWidth((double) 600 /treeTableView.getColumns().size());
        }

        treeTableView.setColumnResizePolicy(treeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableView.getSortOrder().add(successAppealCol);
        nameCol.setSortable(false);
        successAppealCol.setSortable(false);
        treeTableView.getColumns().forEach(column -> column.setReorderable(false));
    }

    @FXML
    private void showNumUsersInSystem(Object filter){
        if (inSystemToggleButton.isSelected() || filter == null){
            facultyStaffLabel.setText(String.valueOf(userListHashMap.get("เจ้าหน้าที่คณะ").getUsers().size()));
            departmentStaffLabel.setText(String.valueOf(userListHashMap.get("เจ้าหน้าที่ภาควิชา").getUsers().size()));
            advisorLabel.setText(String.valueOf(userListHashMap.get("อาจารย์ที่ปรึกษา").getUsers().size()));
            studentLabel.setText(String.valueOf(userListHashMap.get("นักศึกษา").getUsers().size()));
            allUserLabel.setText(String.valueOf(userListHashMap.get("ทั้งหมด").getUsers().size()));
        }
        else if (filter instanceof Faculty faculty){
            facultyStaffLabel.setText(String.valueOf(userListHashMap.get("เจ้าหน้าที่คณะ").getUsersByFacultyUUID(faculty.getUuid()).getUsers().size()));
            departmentStaffLabel.setText(String.valueOf(userListHashMap.get("เจ้าหน้าที่ภาควิชา").getUsersByFacultyUUID(faculty.getUuid()).getUsers().size()));
            advisorLabel.setText(String.valueOf(userListHashMap.get("อาจารย์ที่ปรึกษา").getUsersByFacultyUUID(faculty.getUuid()).getUsers().size()));
            studentLabel.setText(String.valueOf(userListHashMap.get("นักศึกษา").getUsersByFacultyUUID(faculty.getUuid()).getUsers().size()));
            allUserLabel.setText(String.valueOf(userListHashMap.get("ทั้งหมด").getUsersByFacultyUUID(faculty.getUuid()).getUsers().size()));
        } else if (filter instanceof Department department){
            facultyStaffLabel.setText(String.valueOf(userListHashMap.get("เจ้าหน้าที่คณะ").getUsersByDepartment(department.getDepartmentName()).getUsers().size()));
            departmentStaffLabel.setText(String.valueOf(userListHashMap.get("เจ้าหน้าที่ภาควิชา").getUsersByDepartment(department.getDepartmentName()).getUsers().size()));
            advisorLabel.setText(String.valueOf(userListHashMap.get("อาจารย์ที่ปรึกษา").getUsersByDepartment(department.getDepartmentName()).getUsers().size()));
            studentLabel.setText(String.valueOf(userListHashMap.get("นักศึกษา").getUsersByDepartment(department.getDepartmentName()).getUsers().size()));
            allUserLabel.setText(String.valueOf(userListHashMap.get("ทั้งหมด").getUsersByDepartment(department.getDepartmentName()).getUsers().size()));
        }
    }

    private void loadAppealData(){
        appealDatasource = new AppealListFileDatasource("data" , "appeal-list.csv");
        appealList = appealDatasource.readData();
    }

    private void loadFacultyAndDepartmentData(){
        facultyDatasource = new FacultyListDatasource("data" , "faculties.csv");
        facultyList = facultyDatasource.readData();
        departmentDatasource = new DepartmentListDatasource("data" , "departments.csv");
        departmentList = departmentDatasource.readData();
    }

    private void loadUserData(){
        userListHashMap = new HashMap<>();
        userListHashMap.put("เจ้าหน้าที่คณะ", new UserListDatasource("data" + File.separator + "users", "facultyStaff.csv").readData());
        userListHashMap.put("เจ้าหน้าที่ภาควิชา", new UserListDatasource("data" + File.separator + "users", "departmentStaff.csv").readData());
        userListHashMap.put("อาจารย์ที่ปรึกษา", new UserListDatasource("data" + File.separator + "users", "advisor.csv").readData());
        userListHashMap.put("นักศึกษา", new UserListDatasource("data" + File.separator + "users", "student.csv").readData().getRegisteredStudents());

        userListHashMap.put("ทั้งหมด", new UserList());
        for (String key : userListHashMap.keySet()){
            if (!key.equals("ทั้งหมด")){
                userListHashMap.get("ทั้งหมด").addUserLists(userListHashMap.get(key));
            }
        }
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

    private void startWatchingFiles() {
        Runnable fileWatcherTask = () -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();

                Path userPath = Paths.get("data/users");
                Path appealPath = Paths.get("data");

                userPath.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                appealPath.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey key;
                while (!Thread.currentThread().isInterrupted() && (key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        String fileName = event.context().toString();

                        if (fileName.equals("facultyStaff.csv") ||
                                fileName.equals("departmentStaff.csv") ||
                                fileName.equals("advisor.csv") ||
                                fileName.equals("student.csv") ) {
                            Platform.runLater(() -> {
                                loadUserData();
                                if (treeTableView.getSelectionModel().getSelectedItem() != null) {
                                    showNumUsersInSystem(treeTableView.getSelectionModel().getSelectedItem().getValue());
                                } else {
                                    showNumUsersInSystem(null);
                                }
                            });
                        }else if (fileName.equals("appeal-list.csv")){
                            Platform.runLater(() -> {
                                loadAppealData();
                                showAllStatusAppealsCount();
                                showTreeTable();
                            });
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        fileWatcherThread  = new Thread(fileWatcherTask);
        fileWatcherThread.start();
    }

    public void stopWatchingFilesThread() {
        if (fileWatcherThread != null && fileWatcherThread.isAlive()) {
            fileWatcherThread.interrupt();
        }
    }

}
