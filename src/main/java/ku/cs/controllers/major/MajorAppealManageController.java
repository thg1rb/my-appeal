package ku.cs.controllers.major;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.AppealEditController;
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
    @FXML private Pane navbarAnchorPane;
    @FXML private TableView<Appeal> tableView;
    @FXML private TabPane tabPane;

    private Appeal selectedAppeal;
    private AppealList appealList;
    private Datasource<AppealList> datasource;
    private boolean filter = false;

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

        showTable(appealList,false);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable-> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                showTable(appealList, filter);
            } else {
                showTable(appealList, filter);
            }
        });

        tableView.setRowFactory(v->{
            TableRow<Appeal> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                selectedAppeal = tableView.getSelectionModel().getSelectedItem();
                if(selectedAppeal != null) {
                    showAppealPopup();
                }
            });
            return row;
        });
    }

    public void showAppealPopup(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/appeal-popup.fxml"));
            Parent root = fxmlLoader.load();
            AppealEditController controller = fxmlLoader.getController();

            controller.setRole(user);
            controller.setSelectedAppeal(selectedAppeal);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            datasource.writeData(appealList);
            datasource.readData();
            showTable(appealList, filter);
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
                if(appeal.getStatus().equals("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา")){
                    tableView.getItems().add(appeal);
                }
            }
        } else if (appealList != null && filter) {
            for (Appeal appeal : appealList.getAppeals()) {
                if (appeal.getOwnerDepartment().equals(((DepartmentStaff)user).getDepartment()) &&
                appeal.getStatus().equals("อนุมัติโดยอาจารย์ที่ปรึกษา | คำร้องส่งต่อให้หัวหน้าภาควิชา")) {
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
