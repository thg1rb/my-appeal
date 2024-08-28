package ku.cs.models.users;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class Professor extends User {
    private String faculty;
    private String major;
    private String id;
    private String initPassword;
    private String initPasswordText;

    public Professor(String username, String password, String firstName, String lastName, String faculty, String major, String id) {
        super(username, password, firstName, lastName, "อาจารย์ที่ปรึกษา");
        setInitPassword(password);
        this.initPasswordText = password;
        this.faculty = faculty;
        this.major = major;
        this.id = id;
    }

    public String getFaculty() { return faculty; }
    public String getMajor() { return major; }
    public String getId() { return id; }
    public String getInitPasswordText() { return initPasswordText; }

    public void setInitPassword(String password) {
        this.initPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
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
