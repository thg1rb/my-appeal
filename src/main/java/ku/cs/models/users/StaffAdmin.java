package ku.cs.models.users;

import ku.cs.services.FXRouter;

import java.io.IOException;

public class StaffAdmin extends User {

    public StaffAdmin(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName, "ผู้ดูแลระบบ");
    }

    @Override
    public void login() {
        try {
            FXRouter.goTo("admin-dashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
