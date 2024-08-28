package ku.cs.models.users;

import ku.cs.services.FXRouter;

import java.io.IOException;

public class StaffFaculty extends User {
    private String faculty;

    public StaffFaculty(String username, String password, String firstName, String lastName, String faculty) {
        super(username, password, firstName, lastName, "เจ้าหน้าที่คณะ");
        this.faculty = faculty;
    }

    public String getFaculty() { return faculty; }

    @Override
    public void login() {
        try {
            FXRouter.goTo("faculty-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
