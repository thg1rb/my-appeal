package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyApproverManageController {


    @FXML private Pane backgroundAddApproverPane;

    @FXML private Pane addApproverPane;


    @FXML
    public void addApproverButton(){
        backgroundAddApproverPane.setVisible(true);
        addApproverPane.setVisible(true);
    }

    @FXML
    public void onCloseButtonClick(){
        backgroundAddApproverPane.setVisible(false);
        addApproverPane.setVisible(false);
    }

    @FXML
    public void onAppealButtonClick(){
        try {
            FXRouter.goTo("faculty-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutButtonClick(){

        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






}
