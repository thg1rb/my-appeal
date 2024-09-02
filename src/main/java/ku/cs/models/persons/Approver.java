package ku.cs.models.persons;

public class Approver extends Human{
    private String faculty;
    private String major;
    private String role;

    public Approver(String firstName, String lastName, String faculty, String major,String role) {
        super(firstName, lastName);
        this.faculty = faculty;
        this.major= major;
        this.role = role;
    }

    public Approver(String firstName, String lastName, String role,User adder) {
        super(firstName, lastName);
        this.role = role;
        this.faculty = adder.getFaculty();
        this.major = adder.getRole().equals("เจ้าหน้าที่ภาควิชา") ? adder.getMajor() : null;
    }
    public String getFaculty() {
        return faculty;
    }
    public String getMajor(){
        return major;
    }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return  getFirstName() + "," +
                getLastName() + "," +
                faculty + "," + major + "," + role;
    }
}
