package ku.cs.controllers.faculty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.AppealEditController;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;

import java.io.IOException;


public class FacultyAppealManageController {
    @FXML private Pane navbarAnchorPane;

    @FXML private TabPane tabPane;

    @FXML private TableView<Appeal> tableView;

    private Appeal selectedAppeal;
    public AppealList appealList;
    public Datasource<AppealList> datasource;
    private AppealList facultyAppealList;
    private boolean preview = true;

    private User user;
//    private Object selectedAppeal;

    @FXML
    public void initialize() {
        user = (FacultyStaff) FXRouter.getData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        facultyAppealList = appealList.getAppealByFaculty(((FacultyStaff) user).getFaculty());

        showTable(facultyAppealList , false);

        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                preview = true;
                showTable(facultyAppealList, false);
            } else {
                preview = false;
                showTable(facultyAppealList, true);
            }
        });

        tableView.setRowFactory(v -> {
            TableRow<Appeal> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                selectedAppeal = tableView.getSelectionModel().getSelectedItem();
                if (selectedAppeal != null) {
                    showAppealPopup(preview);
                }
            });
            return row;
        });
    }

    public void showAppealPopup(boolean preview){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/appeal-popup.fxml"));
            Parent root = fxmlLoader.load();
            AppealEditController controller = fxmlLoader.getController();
            controller.setRole(user);
            controller.setSelectedAppeal(selectedAppeal);
            controller.setMode(preview);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            datasource.writeData(appealList);

            showTable(appealList , false);
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void showTable(AppealList appealList , boolean filter) {
        TableColumn<Appeal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        dateColumn.setComparator(new DateTimeService());

        TableColumn<Appeal, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));


        TableColumn<Appeal, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.getColumns().clear();
        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(ownerColumn);
        tableView.getColumns().add(typeColumn);

        dateColumn.setPrefWidth(367);
        ownerColumn.setPrefWidth(366);
        typeColumn.setPrefWidth(366);

        tableView.getSortOrder().add(dateColumn);

        tableView.getItems().clear();
        if (appealList != null) {
            for(Appeal appeal : appealList.getAppeals()){
                tableView.getItems().add(appeal);
            }
        }
        tableView.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);
    }
    @FXML
    public void confirmOnButtonClick() {

    }

    @FXML
    public void cancleOnButtonClick() {

    }
}
