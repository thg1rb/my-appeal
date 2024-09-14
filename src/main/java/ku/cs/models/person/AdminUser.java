package ku.cs.models.person;

public class AdminUser extends User {
    //Constructor
    public AdminUser(String role, String username, String password, String firstName, String lastName) {
        super(role, username, password, firstName, lastName);
    }
    //Constructor for reading file
    public AdminUser(String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileUrl) {
        super(role, username, password, firstName, lastName, access, loginDate, profileUrl);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
