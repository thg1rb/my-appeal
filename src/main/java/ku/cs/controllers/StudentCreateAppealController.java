package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentCreateAppealController {

    @FXML private Circle profileImageCircle;

    @FXML private ChoiceBox<String> appealChoiceBox;
    private String[] appeals = {"ทั่วไป", "ขอลาออก", "ขอพักการศึกษา", "ลาป่วยหรือลากิจ", "ขอผ่อนผันการชำระ"};

    @FXML
    public void initialize() {
        // แสดงโปรไฟล์ผู้ใช้งาน
        Image profileImage = new Image(getClass().getResource("/images/student-profile.jpeg").toString());
        profileImageCircle.setFill(new ImagePattern(profileImage));

        appealChoiceBox.getItems().addAll(appeals);
    }

    // ไปที่หน้าติดตามคำร้อง
    @FXML
    private void onTrackAppealButtonClick() {
        try {
            FXRouter.goTo("student-track-appeal");
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
