package ku.cs.models.person;

public class DepartmentStaff extends FacultyStaff{
    private String department;

    // Constructor
    public DepartmentStaff(String role, String username, String password, String firstName, String lastName, String faculty, String department) {
        super(role, username, password, firstName, lastName, faculty);
        this.department = department;
    }
    // Constructor for reading file
    public DepartmentStaff(String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, String initialPasswordText, String initialPasswordHashed, String faculty, String department) {
        super(role, username, password, firstName, lastName, access, loginDate, profileImg, initialPasswordText, initialPasswordHashed, faculty);
        this.department = department;
    }

    //Getter
    public String getDepartment() {
        return department;
    }
    //Setter
    public void setDepartment(String department) {
        this.department = department;
    }
}
