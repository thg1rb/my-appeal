package ku.cs.models.users;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StaffMajor extends User {
    private String faculty;
    private String major;
    private String initPassword;
    private String initPasswordText;

    public StaffMajor(String username, String password, String firstName, String lastName, String faculty, String major) {
        super(username, password, firstName, lastName, "เจ้าหน้าที่ภาควิชา");
        setInitPassword(password);
        this.initPasswordText = password;
        this.faculty = faculty;
        this.major = major;
    }

    public String getFaculty() { return faculty; }
    public String getMajor() { return major; }
    public String getInitPasswordText() { return initPasswordText; }

    public void setInitPassword(String password) {
        this.initPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
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
