package ku.cs.controllers.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ku.cs.models.Faculty;
import ku.cs.models.Department;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.DepartmentList;
import ku.cs.models.persons.AdminUser;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.FacultyListDatasource;
import ku.cs.services.datasources.DepartmentListDatasource;

import java.io.IOException;
import java.util.Comparator;

public class AdminFacultyManagementController {
    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;
    @FXML private TableView<Object> tableView;

    @FXML private Text totalText;

    private User user;

    private Datasource<FacultyList> facultyDatasource;
    private FacultyList facultyList;

    private Datasource<DepartmentList> departmentDatasource;
    private DepartmentList departmentList;

    private String selectingTab;
    private Object selectedObject;

    private boolean popupEditMode;

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

        facultyDatasource = new FacultyListDatasource("data", "faculties.csv");
        departmentDatasource = new DepartmentListDatasource("data", "departments.csv");
        readData();

        showFacultyTable(facultyList);
        updateTotalText();
        selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> {
            readData();
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();
                showFacultyTable(facultyList);
            }
            else {
                selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();
                showDepartmentTable(departmentList);
            }
        });

        tableView.setRowFactory(v ->{
            TableRow<Object> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                popupEditMode = true;
                selectedObject = tableView.getSelectionModel().getSelectedItem();
                addEditPopup();
            });
            return row;
        });

        tableView.getItems().addListener((ListChangeListener<Object>) c -> updateTotalText());
    }

    private void readData(){
        facultyList = facultyDatasource.readData();
        departmentList = departmentDatasource.readData();
    }
    private void writeData(){
        facultyDatasource.writeData(facultyList);
        departmentDatasource.writeData(departmentList);
    }

    private void showFacultyTable(FacultyList facultyList) {
        TableColumn<Object, String> facultyNameColumn = new TableColumn<>("Faculty Name");
        facultyNameColumn.setCellValueFactory(cellData ->{
            Faculty faculty = (Faculty) cellData.getValue();
            return new SimpleStringProperty(faculty.getFacultyName());
        });

        TableColumn<Object, String> facultyIdColumn = new TableColumn<>("Faculty ID");
        facultyIdColumn.setCellValueFactory(cellData ->{
            Faculty faculty = (Faculty) cellData.getValue();
            return new SimpleStringProperty(faculty.getFacultyId());
        });
        facultyIdColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        tableView.getColumns().clear();
        tableView.getColumns().add((facultyIdColumn));
        tableView.getColumns().add(facultyNameColumn);
        facultyIdColumn.setPrefWidth(550);
        facultyNameColumn.setPrefWidth(550);

        tableView.getItems().clear();
        for (Faculty faculty : facultyList.getFaculties()){
            tableView.getItems().add(faculty);
        }
        tableView.getSortOrder().add(facultyIdColumn);
        tableView.sort();

        facultyIdColumn.setSortable(false);
        facultyNameColumn.setSortable(false);
    }

    private void showDepartmentTable(DepartmentList departmentList) {
        TableColumn<Object, String> departmentNameColumn = new TableColumn<>("Department Name");
        departmentNameColumn.setCellValueFactory(cellData ->{
            Department department = (Department) cellData.getValue();
            return new SimpleStringProperty(department.getDepartmentName());
        });

        TableColumn<Object, String> ofFacultyColumn = new TableColumn<>("Belong of Faculty");
        ofFacultyColumn.setCellValueFactory(cellData ->{
            Department department = (Department) cellData.getValue();
            return new SimpleStringProperty(facultyList.findFacultyByUUID(department.getFacultyUUID()).getFacultyName());
        });

        TableColumn<Object, String> departmentIdColumn = new TableColumn<>("Department ID");
        departmentIdColumn.setCellValueFactory(cellData -> {
            Department department = (Department) cellData.getValue();
            return new SimpleStringProperty(department.getDepartmentId());
        });
        departmentIdColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        tableView.getColumns().clear();
        tableView.getColumns().add((departmentIdColumn));
        tableView.getColumns().add(departmentNameColumn);
        tableView.getColumns().add(ofFacultyColumn);
        departmentIdColumn.setPrefWidth(220);
        ofFacultyColumn.setPrefWidth(330);
        departmentNameColumn.setPrefWidth(550);

        tableView.getItems().clear();
        for (Department department : departmentList.getDepartments()){
            tableView.getItems().add(department);
        }
        tableView.getSortOrder().add(departmentIdColumn);
        tableView.sort();
        departmentIdColumn.setSortable(false);
        departmentNameColumn.setSortable(false);
        ofFacultyColumn.setSortable(false);
    }

    private void updateTotalText(){
        String text = tabPane.getSelectionModel().getSelectedItem().getText();
        totalText.setText("จำนวน" + text + "ทั้งหมด " + tableView.getItems().size() + " " +text);
    }

    private void addEditPopup(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/admin/admin-addEdit-DepartmentFaculty.fxml"));
        try{
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            AdminDepartmentFacultyPopupController departmentFacultyPopup = fxmlLoader.getController();
            departmentFacultyPopup.initPopup(popupEditMode, selectedObject, facultyList, departmentList, selectingTab);

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (departmentFacultyPopup.isDeleted()){
                if (selectedObject instanceof Faculty){
                    facultyList.deleteFaculty((Faculty) selectedObject);
                    departmentList.deleteAllDepartmentsOfFaculty(((Faculty) selectedObject).getUuid());
                }
                else if (selectedObject instanceof Department){
                    departmentList.deleteDepartment((Department) selectedObject);
                }
            }

            writeData();
            readData();
            if (selectingTab.equals("คณะ")){
                showFacultyTable(facultyList);
            }else{
                showDepartmentTable(departmentList);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddButtonClicked(){
        popupEditMode = false;
        selectedObject = null;
        addEditPopup();
    }
}
