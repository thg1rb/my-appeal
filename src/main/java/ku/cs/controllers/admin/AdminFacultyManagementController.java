package ku.cs.controllers.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ku.cs.models.Faculty;
import ku.cs.models.Major;
import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;
import ku.cs.models.persons.User;

import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.FacultyListDatasource;
import ku.cs.services.datasources.MajorListDatasource;

import java.io.IOException;

public class AdminFacultyManagementController {
    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;
    @FXML private TableView<Object> tableView;

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

        facultyDatasource = new FacultyListDatasource("data", "faculties.csv");
        facultyList = facultyDatasource.readData();
        majorDatasource = new MajorListDatasource("data", "majors.csv");
        majorList = majorDatasource.readData();

        showFacultyTable(facultyList);
        updateTotalText();
        selectingTab = tabPane.getSelectionModel().getSelectedItem().getText();

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

        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                popupEditMode = true;
                selectedObject = tableView.getSelectionModel().getSelectedItem();
                addEditPopup();
            }
        });
        tableView.getItems().addListener((ListChangeListener<Object>) c -> updateTotalText());

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

        tableView.getColumns().clear();
        tableView.getColumns().add((facultyIdColumn));
        tableView.getColumns().add(facultyNameColumn);
        facultyIdColumn.setPrefWidth(550);
        facultyNameColumn.setPrefWidth(550);

        tableView.getItems().clear();
        for (Faculty faculty : facultyList.getFaculties()){
            tableView.getItems().add(faculty);
        }
    }

    private void showMajorTable(MajorList majorList) {
        TableColumn<Object, String> majorNameColumn = new TableColumn<>("Major Name");
        majorNameColumn.setCellValueFactory(cellData ->{
            Major major = (Major) cellData.getValue();
            return new SimpleStringProperty(major.getMajorName());
        });

        TableColumn<Object, String> ofFacultyColumn = new TableColumn<>("Belong of Faculty");
        ofFacultyColumn.setCellValueFactory(cellData ->{
            Major major = (Major) cellData.getValue();
            return new SimpleStringProperty(major.getFaculty());
        });

        TableColumn<Object, String> majorIdColumn = new TableColumn<>("major ID");
        majorIdColumn.setCellValueFactory(cellData -> {
            Major major = (Major) cellData.getValue();
            return new SimpleStringProperty(major.getMajorId());
        });

        tableView.getColumns().clear();
        tableView.getColumns().add((majorIdColumn));
        tableView.getColumns().add(majorNameColumn);
        tableView.getColumns().add(ofFacultyColumn);
        majorIdColumn.setPrefWidth(220);
        ofFacultyColumn.setPrefWidth(330);
        majorNameColumn.setPrefWidth(550);

        tableView.getItems().clear();
        for (Major major : majorList.getMajors()){
            tableView.getItems().add(major);
        }
    }

    private void updateTotalText(){
        String text = tabPane.getSelectionModel().getSelectedItem().getText();
        totalText.setText("จำนวน"+text+"ทั้งหมด "+ tableView.getItems().size() + " " +text);
    }

    private void addEditPopup(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/admin/admin-addEdit-MajorFaculty.fxml"));
        try{
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            AdminMajorFacultyPopupController majorFacultyPopup = fxmlLoader.getController();
            majorFacultyPopup.initPopup(popupEditMode, selectedObject, facultyList, majorList, selectingTab);

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            facultyDatasource.writeData(facultyList);
            majorDatasource.writeData(majorList);

            if (selectingTab.equals("คณะ")){
                showFacultyTable(facultyList);
            }else{
                showMajorTable(majorList);
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
