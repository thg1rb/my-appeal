package ku.cs.models.users;

import ku.cs.services.FXRouter;

import java.io.IOException;

public class Nisit extends User {
    private String faculty;
    private String major;
    private String id;
    private String email;

    public Nisit(String username, String password, String firstName, String lastName, String email, String id, String faculty, String major) {
        super(username, password, firstName, lastName, "นักศึกษา");
        this.email = email;
        this.faculty = faculty;
        this.major = major;
        this.id = id;
    }
    public Nisit(String username, String password, String firstName, String lastName, String email, String id, User creator) {
        this(username, password, firstName, lastName, email, id, ((StaffMajor) creator).getFaculty(), ((StaffMajor) creator).getMajor());
    }

    @Override
    public void login() {
        try {
            FXRouter.goTo("student-track-appeal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
