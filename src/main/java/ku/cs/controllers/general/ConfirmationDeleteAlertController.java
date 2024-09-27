package ku.cs.controllers.general;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmationDeleteAlertController {
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private boolean confirmed;

    @FXML
    private void initialize(){
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
