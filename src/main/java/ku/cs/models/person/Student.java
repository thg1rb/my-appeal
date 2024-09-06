package ku.cs.models.person;

public class Student extends User{
    private String studentId;
    private String email;
    private String faculty;
    private String department;
    private String advisor;


    //Constructor
    public Student(String firstName, String lastName, String studentId, String email, String faculty, String department) {
        super("นักศึกษา", "NO-ACCOUNT", "NO-ACCOUNT", firstName, lastName);
        this.studentId = studentId;
        this.email = email;
        this.faculty = faculty;
        this.department = department;
    }
    public Student(String firstName, String lastName, String studentId, String email, String faculty, String department, String advisor) {
        this(firstName, lastName, studentId, email, faculty, department);
        this.advisor = advisor;
    }
    //Constructor for reading file
    public Student(String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileUrl, boolean active, String StudentId, String email, String faculty, String department, String advisor) {
        super(role, username, password, firstName, lastName, access, loginDate, profileUrl, active);
        this.studentId = StudentId;
        this.email = email;
        this.faculty = faculty;
        this.department = department;
        this.advisor = advisor;
    }

    //Setter
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    //Getter
    public String getStudentId() {
        return studentId;
    }
    public String getEmail() {
        return email;
    }
    public String getFaculty() {
        return faculty;
    }
    public String getDepartment() {
        return department;
    }
    public String getAdvisor() {
        return advisor;
    }
}
