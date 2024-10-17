package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.io.IOException;

public class FacultyAppealManageController {
    private Appeal selectedAppeal;
    private AppealList appealList;
    private Datasource<AppealList> datasource;
    private AppealList facultyAppealList;
    private boolean preview = true;
    private ModifyDateList modifyDateList;
    private User user;
    private Datasource<ModifyDateList> modifyDateListDatasource;

    @FXML private AnchorPane mainPane;
    @FXML private Pane navbarAnchorPane;
    @FXML private TabPane tabPane;
    @FXML private TableView<Appeal> tableView;
    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        user = (FacultyStaff) FXRouter.getData();

        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        facultyAppealList = appealList.getAppealByFaculty(((FacultyStaff) user).getFacultyUUID());

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        ProgramSetting.getInstance().applyStyles(mainPane);

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
        TableColumn<Appeal, String> dateColumn = new TableColumn<>("วันเวลาที่สถานะเปลี่ยน");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        dateColumn.setComparator(new DateTimeService());

        TableColumn<Appeal, String> ownerColumn = new TableColumn<>("ชื่อ-สกุล");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerFullName"));


        TableColumn<Appeal, String> typeColumn = new TableColumn<>("ประเภทของคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Appeal, String> statusColumn = new TableColumn<>("สถานะของคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusColumn.setCellFactory(column -> new TableCell<Appeal, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    switch (status) {
                        case "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา":
                            setText("คำร้องใหม่");
                            setStyle("-fx-background-color: lime; -fx-text-fill: black;");
                            break;
                        case "อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา":
                            setText("รอภาควิชาดำเนินการ");
                            setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                            break;
                        case "อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน":
                            setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            setText("ดำเนินการแล้ว");
                            break;
                        case "อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี":
                            setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                            setText("รอดำเนินการ");
                            break;

                        case "อนุมัติโดยคณบดี | คำร้องดำเนินการครบถ้วน":
                            setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            setText("ดำเนินการแล้ว");
                            break;
                        case "ปฏิเสธโดยอาจารย์ที่ปรึกษา | คำร้องถูกปฏิเสธ":
                        case "ปฏิเสธโดยหัวหน้าภาควิชา | คำร้องถูกปฏิเสธ":
                        case "ปฏิเสธโดยคณบดี | คำร้องถูกปฏิเสธ":
                            setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            setText("ถูกปฏิเสธ");
                            break;
                        default:
                            setText(status);
                            setStyle("");
                            break;
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
                    tableView.getItems().add(appeal);
                }

            updateTotalLabel("จำนวนคำร้องทั้งหมด ");
        } else if (appealList != null && filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (!appeal.getStatus().equals("null") && appeal.getOwnerFacultyUuid().equals(((FacultyStaff) user).getFacultyUUID()) && appeal.getStatus().equals("อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี")) {
                    tableView.getItems().add(appeal);
                }
            }
            updateTotalLabel("จำนวนคำร้องที่รอดำเนินการทั้งหมด ");
        }


        tableView.getSortOrder().add(dateColumn);

        tableView.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);
        statusColumn.setSortable(false);
        tableView.getColumns().forEach(column -> column.setReorderable(false));
    }

    private void updateTotalLabel(String label) {
        totalLabel.setText(label + tableView.getItems().size() + " คำร้อง");
    }
}
