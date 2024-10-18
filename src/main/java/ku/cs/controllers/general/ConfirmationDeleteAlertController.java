package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.services.ProgramSetting;

public class ConfirmationDeleteAlertController {

    @FXML private AnchorPane mainPane;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private boolean confirmed;

    @FXML
    private void initialize(){
        ProgramSetting.getInstance().applyStyles(mainPane);

        confirmButton.setOnAction(event -> {
            confirmed = true;
            closeAlert();
        });

        cancelButton.setOnAction(event -> {
            confirmed = false;
            closeAlert();
        });
    }


    public boolean isConfirmed() {
        return confirmed;
    }

    public void closeAlert() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
