package ku.cs.models.users;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StaffFaculty extends User {
    private String faculty;
    private String initPassword;
    private String initPasswordText;

    public StaffFaculty(String username, String password, String firstName, String lastName, String faculty) {
        super(username, password, firstName, lastName, "เจ้าหน้าที่คณะ");
        setInitPassword(password);
        this.initPasswordText = password;
        this.faculty = faculty;
    }

    public String getFaculty() { return faculty; }
    public String getInitPasswordText() { return initPasswordText; }

    public void setInitPassword(String password) {
        this.initPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    @Override
    public void login() {
        try {
            FXRouter.goTo("faculty-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
