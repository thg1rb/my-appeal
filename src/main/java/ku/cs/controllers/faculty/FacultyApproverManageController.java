package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import ku.cs.models.collections.ApproverList;
import ku.cs.models.persons.Approver;
import ku.cs.models.persons.User;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyApproverManageController {

    @FXML
    private TableView<Approver> approverTableView;

    private User user;
    private Datasource<ApproverList> approversDatasource;
    private ApproverList approverList;

    @FXML
    public void initialize() {
        approversDatasource = new ApproverListFileDatasource("data", "approver.csv");
        approverList = approversDatasource.readData();
        user = (User)FXRouter.getData();
        showApproverTable(approverList);
    }

    private void showApproverTable(ApproverList approverList) {
        TableColumn<Approver, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Approver, String> fullNameColumn = new TableColumn<>("Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(roleColumn);
        approverTableView.getColumns().add(fullNameColumn);

        roleColumn.setPrefWidth(550);
        fullNameColumn.setPrefWidth(550);

        approverTableView.getItems().clear();
        for (Approver approver : approverList.getApprovers()) {
            approverTableView.getItems().add(approver);
        }
        approverTableView.setRowFactory(tv -> {
            TableRow<Approver> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    showEditApproverPane();
                }
            });
            return row;
        });
    }

    @FXML
    private Pane backgroundAddApproverPane;

    @FXML
    private Pane addApproverPane;

    @FXML
    private Pane editApproverPane;

    @FXML
    private TextField roleTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField editLastNameTextField;

    @FXML
    private TextField editFirstNameTextField;

    @FXML
    private TextField editRoleTextField;

    @FXML
    public void addApproverButton() {
        roleTextField.clear();
        firstNameTextField.clear();
        lastNameTextField.clear();
        backgroundAddApproverPane.setVisible(true);
        addApproverPane.setVisible(true);
    }

    private void showEditApproverPane() {
        editRoleTextField.clear();
        editFirstNameTextField.clear();
        editLastNameTextField.clear();

        backgroundAddApproverPane.setVisible(true);

        editApproverPane.setVisible(true);
    }

    @FXML
    public void onCloseButtonClick() {
        backgroundAddApproverPane.setVisible(false);
        addApproverPane.setVisible(false);
    }

    @FXML
    public void onCloseEditButtonClick() {
        backgroundAddApproverPane.setVisible(false);
        editApproverPane.setVisible(false);
    }

    @FXML
    public void onAppealButtonClick() {
        try {
            FXRouter.goTo("faculty-appeal-manage");
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

    @FXML
    public void onAddApproverToTable() {
        String role = roleTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String faculty = user.getFaculty();
        String major = user.getMajor();

        if (!role.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
            approverList.addApprover(firstName, lastName, faculty, major, role);

            approversDatasource.writeData(approverList);

            showApproverTable(approverList);

            onCloseButtonClick();
        }
    }

    @FXML
    public void onEditButtonClick() {
        Approver selectedApprover = approverTableView.getSelectionModel().getSelectedItem();

        if (selectedApprover != null) {
            String newRole = editRoleTextField.getText();
            String newFirstName = editFirstNameTextField.getText();
            String newLastName = editLastNameTextField.getText();

            selectedApprover.setRole(newRole);
            selectedApprover.setFirstName(newFirstName);
            selectedApprover.setLastName(newLastName);
            selectedApprover.setFullName();

            showApproverTable(approverList);

            approversDatasource.writeData(approverList);

            onCloseEditButtonClick();
        } else {
            System.out.println("No approver selected.");
        }
    }

}
