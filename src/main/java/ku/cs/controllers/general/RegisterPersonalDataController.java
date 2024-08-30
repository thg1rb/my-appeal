package ku.cs.controllers.general;

import javafx.fxml.FXML;

import ku.cs.models.collections.StudentList;

import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentRosterListFileDatasource;

import java.io.IOException;

public class RegisterPersonalDataController {
    private Datasource<StudentList> datasource;
    private StudentList studentList;

    @FXML
    public void initialize() {
        datasource = new StudentRosterListFileDatasource("data", "student");

    }
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