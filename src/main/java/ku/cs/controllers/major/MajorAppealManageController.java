package ku.cs.controllers.major;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.general.AppealEditController;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.collections.AppealList;

import ku.cs.models.persons.User;
import ku.cs.services.AppealListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.DateTimeService;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorAppealManageController {
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    @FXML private TableView<Appeal> allAppealTable;
    @FXML private TableView<Appeal> selfAppealTable;

    Appeal selectedAppeal;

    public AppealList appealList;
    public Datasource<AppealList> datasource;

    private User user;
//    private Object selectedAppeal;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());

        datasource = new AppealListFileDatasource("data", "appeal-list.csv");
        appealList = datasource.readData();
        user = (User)FXRouter.getData();

        showTable(appealList);

        allAppealTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appeal>() {
            @Override
            public void changed(ObservableValue<? extends Appeal> observableValue, Appeal oldValue, Appeal newValue) {
                if (newValue != null) {
                    selectedAppeal = newValue;
                    showAppealPopup();
                }
            }
        });
    }

    public void showAppealPopup(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/appeal-popup.fxml"));
            Parent root = fxmlLoader.load();
            AppealEditController controller = fxmlLoader.getController();
            controller.setType(selectedAppeal.getType(), selectedAppeal, user);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            allAppealTable.getSelectionModel().select(null);
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void showTable(AppealList appealList) {
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

        allAppealTable.getColumns().clear();
        allAppealTable.getColumns().add(dateColumn);
        allAppealTable.getColumns().add(ownerColumn);
        allAppealTable.getColumns().add(typeColumn);

        allAppealTable.getSortOrder().add(dateColumn);

        allAppealTable.getItems().clear();
        if (appealList != null) {
            for(Appeal appeal : appealList.getAppeals()){
                allAppealTable.getItems().add(appeal);
            }
        }
        allAppealTable.sort();

        dateColumn.setSortable(false);
        ownerColumn.setSortable(false);
        typeColumn.setSortable(false);


    }

    @FXML
    protected void onApproverManageButtonClick() {
        try {
            FXRouter.goTo("major-approver-manage", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNisitManageButtonClick() {
        try {
            FXRouter.goTo("major-nisit-manage", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        @FXML
        public void onLogoutButtonClick(){
            try{
                FXRouter.goTo("login");
            }
            catch(IOException e){
                throw new RuntimeException(e);
            }
        }
}
