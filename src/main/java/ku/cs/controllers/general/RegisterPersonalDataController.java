package ku.cs.controllers.general;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class RegisterPersonalDataController {

    // ไปที่หน้าลงทะเบียนถัดไป
    @FXML
    public void onNextButtonClick() {
        try {
            FXRouter.goTo("register-username-password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}