package ku.cs.models.users;

import ku.cs.services.FXRouter;

import java.io.IOException;

public class Professor extends User {
    private String faculty;
    private String major;
    private String id;

    public Professor(String username, String password, String firstName, String lastName, String faculty, String major, String id) {
        super(username, password, firstName, lastName, "อาจารย์ที่ปรึกษา");
        this.faculty = faculty;
        this.major = major;
        this.id = id;
    }


    @Override
    public void login() {
        try {
            FXRouter.goTo("professor-student-appeal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
