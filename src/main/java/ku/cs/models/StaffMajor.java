package ku.cs.models;

import ku.cs.services.FXRouter;

import java.io.IOException;

public class StaffMajor extends User {
    private String faculty;
    private String major;

    public StaffMajor(String username, String password, String firstName, String lastName, String faculty, String major) {
        super(username, password, firstName, lastName, "เจ้าหน้าที่ภาควิชา");
        this.faculty = faculty;
        this.major = major;
    }

    @Override
    public void login() {
        try {
            FXRouter.goTo("major-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
