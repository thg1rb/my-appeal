package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListHardCodeDatasource;
import ku.cs.services.MajorListHardCodedatasource;

import java.io.IOException;

public class AdminFacultyManagementController {
    @FXML private TabPane tabPane;

    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableView<Major> majorTable;

    private Datasource<FacultyList> facultyDatasource;
    private FacultyList facultyList;

    private Datasource<MajorList> majorDatasource;
    private MajorList majorList;

    private Faculty selectedFaculty;
    private Major selectedMajor;

    @FXML
    public void initialize() {
        facultyDatasource = new FacultyListHardCodeDatasource();
        facultyList = facultyDatasource.readData();
        majorDatasource = new MajorListHardCodedatasource();
        majorList = majorDatasource.readData();

        showFacultyTable(facultyList);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                showFacultyTable(facultyList);
                facultyTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Faculty>() {
                    @Override
                    public void changed(ObservableValue<? extends Faculty> observableValue, Faculty oldVal, Faculty newVal) {
                        if (newVal == null) {
                            selectedFaculty = null;
                        } else {
                            selectedFaculty = newVal;
                        }
                    }
                });
            }
            else {
                showMajorTable(majorList);
                majorTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Major>() {

                    @Override
                    public void changed(ObservableValue<? extends Major> observableValue, Major oldVal, Major newVal) {
                        if (newVal == null) {
                            selectedMajor = null;
                        } else{
                            selectedMajor = newVal;
                        }
                    }
                });
            }
        });
    }

    private void showFacultyTable(FacultyList facultyList) {
        TableColumn<Faculty, String> facultyNameColumn = new TableColumn<>("Faculty Name");
        facultyNameColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Faculty, String> facultyIdColumn = new TableColumn<>("Faculty ID");
        facultyIdColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        facultyTable.getColumns().clear();
        facultyTable.getColumns().add(facultyNameColumn);
        facultyTable.getColumns().add((facultyIdColumn));
        facultyIdColumn.setPrefWidth(550);
        facultyNameColumn.setPrefWidth(550);

        facultyTable.getItems().clear();
        for (Faculty faculty : facultyList.getFaculties()){
            facultyTable.getItems().add(faculty);
        }
    }

    private void showMajorTable(MajorList majorList) {
        TableColumn<Major, String> majorNameColumn = new TableColumn<>("Major Name");
        majorNameColumn.setCellValueFactory(new PropertyValueFactory<>("majorName"));

        TableColumn<Major, String> ofFacultyColumn = new TableColumn<>("Belong of Faculty");
        ofFacultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<Major, String> majorIdColumn = new TableColumn<>("major ID");
        majorIdColumn.setCellValueFactory(new PropertyValueFactory<>("majorId"));

        majorTable.getColumns().clear();
        majorTable.getColumns().add((majorIdColumn));
        majorTable.getColumns().add(majorNameColumn);
        majorTable.getColumns().add(ofFacultyColumn);
        majorIdColumn.setPrefWidth(220);
        ofFacultyColumn.setPrefWidth(330);
        majorNameColumn.setPrefWidth(550);

        majorTable.getItems().clear();
        for (Major major : majorList.getMajors()){
            majorTable.getItems().add(major);
        }
    }

    @FXML
    public void onUserButtonClicked() {
        try {
            FXRouter.goTo("admin-user-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onDashboardButtonClicked() {
        try {
            FXRouter.goTo("admin-dashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onStaffButtonClicked() {
        try {
            FXRouter.goTo("admin-staff-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
