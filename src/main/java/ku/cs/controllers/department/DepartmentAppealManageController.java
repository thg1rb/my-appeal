package ku.cs.controllers.major;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.AppealEditController;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;

import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.User;
import ku.cs.services.ProgramSetting;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.ModifyDateListFileDatasource;

import java.io.IOException;

public class MajorAppealManageController {
    @FXML private AnchorPane mainPane;
    @FXML private Pane navbarAnchorPane;
    @FXML private TableView<Appeal> tableView;
    @FXML private TabPane tabPane;
    @FXML private Label totalLabel;

    private Appeal selectedAppeal;
    private AppealList appealList;
    private AppealList departmentAppealList;
    private Datasource<AppealList> datasource;
    private boolean preview = true;

    private Datasource<ModifyDateList> modifyDateListDatasource;
    private ModifyDateList modifyDateList;
    private User user;

    @FXML
    public void initialize() {
        user = (DepartmentStaff) FXRouter.getData();

        modifyDateListDatasource = new ModifyDateListFileDatasource("data", "modify-date.csv");
        modifyDateList = modifyDateListDatasource.readData();

        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        departmentAppealList = appealList.getAppealByDepartment(((DepartmentStaff) user).getDepartmentUUID());

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

        showTable(departmentAppealList, false);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                preview = true;
                showTable(departmentAppealList, false);
            } else {
                preview = false;
                showTable(departmentAppealList, true);
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
            GaussianBlur blur = new GaussianBlur(10);

            controller.setRole(user);
            controller.setMode(preview);
            controller.setSelectedAppeal(selectedAppeal);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            mainPane.setEffect(blur);
            stage.showAndWait();
            mainPane.setEffect(null);

            datasource.writeData(appealList);
            appealList = datasource.readData();
            departmentAppealList = appealList.getAppealByDepartment(((DepartmentStaff) user).getDepartmentUUID());
            showTable(departmentAppealList, tabPane.getSelectionModel().getSelectedIndex() == 1);

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

        tableView.getColumns().clear();
        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(ownerColumn);
        tableView.getColumns().add(typeColumn);
        tableView.getColumns().add(statusColumn);

        int size = tableView.getColumns().size();
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setPrefWidth((double) 1100 / size);
        }

        tableView.getSortOrder().add(dateColumn);
        tableView.getItems().clear();
        if (appealList != null && !filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (modifyDateList.findModifyDateByUuid(appeal.getUuid()).getAdvisorApproveDate() != null && !appeal.getStatus().equals("ปฏิเสธโดยอาจารย์ที่ปรึกษา | คำร้องถูกปฏิเสธ")){
                    tableView.getItems().add(appeal);
                }
            }
            updateTotalLabel("จำนวนคำร้องทั้งหมด ");
        } else if (appealList != null && filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (appeal.getStatus().equals("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา")) {
                    tableView.getItems().add(appeal);
                }
            }
            updateTotalLabel("จำนวนคำร้องที่รอดำเนินการทั้งหมด ");
        }

        // Custom cell factory for the status column
        statusColumn.setCellFactory(column -> new TableCell<Appeal, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // Simplify status text based on specific conditions
                    switch (status) {
                        case "อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา":
                            setText("รอดำเนินการ");
                            setStyle("-fx-background-color: eed202; -fx-text-fill: black;");
                            break;
                        case "อนุมัติโดยหัวหน้าภาควิชา | คำร้องดำเนินการครบถ้วน":
                        case "อนุมัติโดยหัวหน้าภาควิชา | คำร้องส่งต่อให้คณบดี":
                            setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            setText("ดำเนินการแล้ว");
                            break;
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

        tableView.getSortOrder().add(dateColumn);
        tableView.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);
        statusColumn.setSortable(false);
    }

    private void updateTotalLabel(String label) {
        totalLabel.setText(label + tableView.getItems().size() + " คำร้อง");
    }
}
