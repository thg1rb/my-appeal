package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;

import ku.cs.models.persons.User;
import ku.cs.services.*;

import java.io.IOException;

public class AdminFacultyManagementController {
    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;

    @FXML private TableView<Faculty> facultyTableView;
    @FXML private TableView<Major> majorTableView;

    @FXML private Text totalText;

    private User user;

    private Datasource<FacultyList> facultyDatasource;
    private FacultyList facultyList;

    private Datasource<MajorList> majorDatasource;
    private MajorList majorList;

    private String selectingTab;
    private Object selectedObject;

    private boolean popupEditMode;

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

        facultyDatasource = new FacultyListFileDatasource("data", "faculties.csv");
        facultyList = facultyDatasource.readData();
        majorDatasource = new MajorListFileDatasource("data", "majors.csv");
        majorList = majorDatasource.readData();

        showFacultyTable(facultyList);
        selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();

        facultyTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Faculty>() {
            @Override
            public void changed(ObservableValue<? extends Faculty> observableValue, Faculty oldVal, Faculty newVal) {
                if (newVal == null) {
                    selectedObject = null;
                } else {
                    popupEditMode = true;
                    selectedObject = newVal;
                    addEditPopup();
                    facultyTableView.getSelectionModel().select(newVal);
                }
            }
        });

        majorTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Major>() {
            @Override
            public void changed(ObservableValue<? extends Major> observableValue, Major oldVal, Major newVal) {
                if (newVal == null) {
                    selectedObject = null;
                } else{
                    popupEditMode = true;
                    selectedObject = newVal;
                    addEditPopup();
                    majorTableView.getSelectionModel().select(newVal);
                }
            }
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();
                showFacultyTable(facultyList);
            }
            else {
                selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();
                showMajorTable(majorList);
            }
        });

        facultyTableView.getItems().addListener((ListChangeListener<Faculty>) c -> updateTotalText());
        majorTableView.getItems().addListener((ListChangeListener<Major>) c -> updateTotalText());

    }

    private void showFacultyTable(FacultyList facultyList) {
        TableColumn<Faculty, String> facultyNameColumn = new TableColumn<>("Faculty Name");
        facultyNameColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Faculty, String> facultyIdColumn = new TableColumn<>("Faculty ID");
        facultyIdColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        facultyTableView.getColumns().clear();
        facultyTableView.getColumns().add(facultyNameColumn);
        facultyTableView.getColumns().add((facultyIdColumn));
        facultyIdColumn.setPrefWidth(550);
        facultyNameColumn.setPrefWidth(550);

        facultyTableView.getItems().clear();
        for (Faculty faculty : facultyList.getFaculties()){
            facultyTableView.getItems().add(faculty);
        }
    }

    private void showMajorTable(MajorList majorList) {
        TableColumn<Major, String> majorNameColumn = new TableColumn<>("Major Name");
        majorNameColumn.setCellValueFactory(new PropertyValueFactory<>("majorName"));

        TableColumn<Major, String> ofFacultyColumn = new TableColumn<>("Belong of Faculty");
        ofFacultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<Major, String> majorIdColumn = new TableColumn<>("major ID");
        majorIdColumn.setCellValueFactory(new PropertyValueFactory<>("majorId"));

        majorTableView.getColumns().clear();
        majorTableView.getColumns().add((majorIdColumn));
        majorTableView.getColumns().add(majorNameColumn);
        majorTableView.getColumns().add(ofFacultyColumn);
        majorIdColumn.setPrefWidth(220);
        ofFacultyColumn.setPrefWidth(330);
        majorNameColumn.setPrefWidth(550);

        majorTableView.getItems().clear();
        for (Major major : majorList.getMajors()){
            majorTableView.getItems().add(major);
        }
    }

    private void updateTotalText(){
        String text = tabPane.getSelectionModel().getSelectedItem().getText();
        totalText.setText("จำนวน"+text+"ทั้งหมด "+ (text.equals("คณะ") ? facultyTableView.getItems().size():majorTableView.getItems().size())+" "+text);
    }

    private void addEditPopup(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/admin/admin-addEdit-MajorFaculty.fxml"));
        try{
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            MajorFacultyPopupController majorFacultyPopup = fxmlLoader.getController();
            majorFacultyPopup.initPopup(popupEditMode, selectedObject, facultyList, majorList, selectingTab);

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (selectingTab.equals("คณะ")){
                showFacultyTable(facultyList);
            }else{
                showMajorTable(majorList);
            }

            facultyDatasource.writeData(facultyList);
            majorDatasource.writeData(majorList);
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
