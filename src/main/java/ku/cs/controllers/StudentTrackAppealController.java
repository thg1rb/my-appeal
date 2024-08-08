package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentTrackAppealController {

    @FXML private Circle profileImageCircle;

    @FXML
    public void initialize() {
        // แสดงโปรไฟล์ผู้ใช้งาน
        Image profileImage = new Image(getClass().getResource("/images/student-profile.jpeg").toString());
        profileImageCircle.setFill(new ImagePattern(profileImage));
    }

    // ไปที่หน้าสร้างใบคำร้อง
    @FXML
    private void onCreateAppealButtonClick() {
        try {
            FXRouter.goTo("student-create-appeal");
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