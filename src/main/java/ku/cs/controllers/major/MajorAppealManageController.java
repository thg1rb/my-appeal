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
import ku.cs.controllers.general.SetPasswordController;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;

import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.User;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.AppealListFileDatasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorAppealManageController {
    @FXML private AnchorPane mainPane;
    @FXML private Pane navbarAnchorPane;
    @FXML private TableView<Appeal> tableView;
    @FXML private TabPane tabPane;

    private Appeal selectedAppeal;
    private AppealList appealList;
    private AppealList departmentAppealList;
    private Datasource<AppealList> datasource;
    private boolean preview = true;

    private User user;

    @FXML
    public void initialize() {
        user = (DepartmentStaff) FXRouter.getData();

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
        departmentAppealList = appealList.getAppealByDepartment(((DepartmentStaff)user).getDepartment());

        showTable(departmentAppealList,false);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                preview = true;
                showTable(departmentAppealList, false);
            } else {
                preview = false;
                showTable(departmentAppealList, true);
            }
        });

        tableView.setRowFactory(v->{
            TableRow<Appeal> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                selectedAppeal = tableView.getSelectionModel().getSelectedItem();
                if(selectedAppeal != null) {
                    showAppealPopup(preview);
                }
            });
            return row;
        });
    }

    public void test(){
        try{
            FXMLLoader testLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/set-password.fxml"));
            Parent root = testLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            stage.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showAppealPopup(boolean preview){
        try{
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
            datasource.readData();
            tableView.refresh();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void showTable(AppealList appealList, boolean filter) {
        TableColumn<Appeal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("modifyDate"));

        dateColumn.setComparator((date1, date2)-> {
            int result = DateTimeService.compareDate(date1, date2);
            return result;
        });

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

        tableView.getItems().clear();
        if (appealList != null && !filter) {
            for(Appeal appeal : appealList.getAppeals()){
                if((!appeal.getStatus().equals("null")) && appeal.getOwnerDepartment().equals(((DepartmentStaff)user).getDepartment())){
                    tableView.getItems().add(appeal);
                }
            }
        } else if (appealList != null && filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (appeal.getStatus().equals("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา")) {
                    tableView.getItems().add(appeal);
                }
            }
        }
        tableView.getSortOrder().add(dateColumn);
        tableView.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);
    }
}
