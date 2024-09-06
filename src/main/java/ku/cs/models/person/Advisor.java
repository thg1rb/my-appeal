package ku.cs.models.person;

public class Advisor extends Officer{
    private String advisorId;

    //Constructor
    public Advisor(String role, String username, String password, String firstName, String lastName, String faculty, String department) {
        super(role, username, password, firstName, lastName, faculty, department);
    }
    //Constructor for reading file
    public Advisor(String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, boolean active, String initialPasswordText, String initialPasswordHashed, String faculty, String department, String advisorId) {
        super(role, username, password, firstName, lastName, access, loginDate, profileImg, active, initialPasswordText, initialPasswordHashed, faculty, department);
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
}
