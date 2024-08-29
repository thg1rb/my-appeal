package ku.cs.models.persons;

public class Student extends Human {
    private String id;
    private String email;
    private String faculty;
    private String major;

    private String advisor;

    public Student(String id, String email, String firstName, String lastName, User adder) {
        super(firstName, lastName);
        this.id = id;
        this.email = email;
        this.faculty = adder.getFaculty();
        this.major = adder.getMajor();
    }

    public Student(String id, String email, String firstName, String lastName, String advisor,User adder) {
        this(id, email, firstName, lastName, adder);
        this.advisor = advisor;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getMajor() {
        return major;
    }

    public String getAdvisor() {
        return advisor;
    }
}
