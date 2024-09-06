package ku.cs.models.person;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Officer extends User{
    private String initialPasswordText;
    private String initialPasswordHashed;

    private String faculty;
    private String department;

    //Constructor
    public Officer(String role, String username, String password, String firstName, String lastName, String faculty) {
        super(role, username, password, firstName, lastName);
        this.faculty = faculty;
        this.initialPasswordText = password;
        setInitPassword(password);
    }
    public Officer(String role, String username, String password, String firstName, String lastName, String faculty, String department) {
      this(role, username, password, firstName, lastName, faculty);
      this.department = department;
    }
    // Constructor for reading file
    public Officer(String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, boolean active, String initialPasswordText, String initialPasswordHashed,String faculty, String department) {
        super(role, username, password, firstName, lastName, access, loginDate, profileImg, active);
        this.initialPasswordText = initialPasswordText;
        this.initialPasswordHashed = initialPasswordHashed;
        this.faculty = faculty;
        this.department = department;
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
    public String getDepartment() {
        return department;
    }

    //Setter
    public void setInitialPassword(String password){
        setInitPassword(password);
        this.initialPasswordText = password;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
}
