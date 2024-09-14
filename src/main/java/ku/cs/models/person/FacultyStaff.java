package ku.cs.models.person;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class FacultyStaff extends User{
    private String initialPasswordText;
    private String initialPasswordHashed;

    private String faculty;

    //Constructor
    public FacultyStaff(String role, String username, String password, String firstName, String lastName, String faculty) {
        super(role, username, password, firstName, lastName);
        this.faculty = faculty;
        this.initialPasswordText = password;
        setInitPassword(password);
    }
    // Constructor for reading file
    public FacultyStaff(String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, String initialPasswordText, String initialPasswordHashed,String faculty) {
        super(role, username, password, firstName, lastName, access, loginDate, profileImg);
        this.initialPasswordText = initialPasswordText;
        this.initialPasswordHashed = initialPasswordHashed;
        this.faculty = faculty;
    }

    //Authentication
    private void setInitPassword(String password) {
        this.initialPasswordHashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public boolean validateInitialPassword(String initialPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(initialPassword.toCharArray(), this.initialPasswordHashed);
        return result.verified;
    }

    //Getter
    public String getInitialPasswordText() {
        return initialPasswordText;
    }
    public String getFaculty() {
        return faculty;
    }

    //Setter
    public void setInitialPassword(String password){
        setInitPassword(password);
        this.initialPasswordText = password;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    @Override
    public String toString() {
        return super.toString() + "," + initialPasswordText + "," + initialPasswordHashed + "," + faculty;
    }
}
