package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class ProfessorStudentListController {

    // ไปที่หน้าคำร้องของนิสิตในที่ปรึกษา
    @FXML
    public void onStudentAppealButtonClick() {
        try {
            FXRouter.goTo("professor-student-appeal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ออกจากระบบ (กลับไปที่หน้าเข้าสู่ระบบ)
    @FXML
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}