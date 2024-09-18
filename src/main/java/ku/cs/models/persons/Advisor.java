package ku.cs.models.persons;

public class Advisor extends DepartmentStaff {
    private String advisorId;

    //Constructor
    public Advisor(String role, String username, String password, String firstName, String lastName, String faculty, String department, String advisorId) {
        super(role, username, password, firstName, lastName, faculty, department);
        this.advisorId = advisorId;
    }
    //Constructor for reading file
    public Advisor(String uuid, String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, String initialPasswordText, String initialPasswordHashed, String faculty, String department, String advisorId) {
        super(uuid, role, username, password, firstName, lastName, access, loginDate, profileImg, initialPasswordText, initialPasswordHashed, faculty, department);
        this.advisorId = advisorId;
    }

    //Getter
    public String getAdvisorId() {
        return advisorId;
    }

    //Setter
    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    @Override
    public String toString() {
        return super.toString() + "," + advisorId;
    }

    @Override
    public String getRoleInEnglish() {
        return "advisor";
    }
}
