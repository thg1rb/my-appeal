package ku.cs.controllers.faculty;

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
import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.io.IOException;


public class FacultyAppealManageController {
    @FXML
    private Pane navbarAnchorPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Appeal> tableView;

    private Appeal selectedAppeal;
    private AppealList appealList;
    private Datasource<AppealList> datasource;
    private AppealList facultyAppealList;
    private boolean preview = true;
    private ModifyDateList modifyDateList;
    private User user;
    private Datasource<ModifyDateList> modifyDateListDatasource;
    //    private Object selectedAppeal;

    @FXML
    public void initialize() {
        user = (FacultyStaff) FXRouter.getData();

        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        facultyAppealList = appealList.getAppealByFaculty(((FacultyStaff) user).getFaculty());

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        showTable(facultyAppealList, false);

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

    public void showAppealPopup(boolean preview) {
        try {
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

            showTable(appealList, tabPane.getSelectionModel().getSelectedIndex() == 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showTable(AppealList appealList, boolean filter) {
        TableColumn<Appeal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        dateColumn.setComparator(new DateTimeService());

        TableColumn<Appeal, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));


        TableColumn<Appeal, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusColumn.setCellFactory(column -> new TableCell<Appeal, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (status.contains("อนุมัติโดยคณบดี") && status.contains("คำร้องดำเนินการครบถ้วน")) {
                        setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        setText("ดำเนินการแล้ว");
                    } else if (status.contains("อนุมัติโดยหัวหน้าภาควิชา") && status.contains("คำร้องส่งต่อให้คณบดี")) {
                        setText("รอดำเนินการ");
                        setStyle("-fx-background-color: eed202; -fx-text-fill: black;");
                    } else if (status.contains("ปฏิเสธโดยคณบดี")) {
                        setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        setText("ถูกปฏิเสธ");
                    } else {
                        setText(status);
                        setStyle("");
                    }
                }
            }
        });
        tableView.getColumns().clear();
        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(ownerColumn);
        tableView.getColumns().add(typeColumn);
        tableView.getColumns().add(statusColumn);
        dateColumn.setPrefWidth(275);
        ownerColumn.setPrefWidth(275);
        typeColumn.setPrefWidth(275);
        statusColumn.setPrefWidth(275);
        tableView.getSortOrder().add(dateColumn);

        tableView.getItems().clear();


        if (appealList != null && !filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (!appeal.getStatus().equals("null") && appeal.getOwnerFaculty().equals(((FacultyStaff) user).getFaculty()) && !modifyDateList.findModifyDateByUuid(appeal.getUuid()).getDepartmentApproveDate().equals("null")
                        && !appeal.getStatus().equals("ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ") && !appeal.getStatus().equals("อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน")) {
                    tableView.getItems().add(appeal);
                }
            }
        } else if (appealList != null && filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (!appeal.getStatus().equals("null") && appeal.getOwnerFaculty().equals(((FacultyStaff) user).getFaculty()) && appeal.getStatus().equals("อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี")) {
                    tableView.getItems().add(appeal);
                }
            }
        }


        tableView.getSortOrder().add(dateColumn);

        tableView.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);
        statusColumn.setSortable(false);

    }

}
