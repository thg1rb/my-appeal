package ku.cs.models.persons;

import ku.cs.models.collections.FacultyList;
import ku.cs.models.collections.MajorList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.FacultyListDatasource;
import ku.cs.services.datasources.MajorListDatasource;

import java.util.UUID;

public class Student extends User {
    private String studentId;
    private String email;
    private UUID facultyUUID;
    private UUID departmentUUID;
    private UUID advisorUUID;

    private boolean registered;

    // Constructor
    // without advisor init
    public Student(String firstName, String lastName, String studentId, String email, UUID faculty, UUID department) {
        super("นักศึกษา", firstName, lastName);
        this.studentId = studentId;
        this.email = email;
        this.facultyUUID = faculty;
        this.departmentUUID = department;
        this.registered = false;
    }

    // with advisor init
    public Student(String firstName, String lastName, String studentId, String email, UUID faculty, UUID department, UUID advisorUUID) {
        this(firstName, lastName, studentId, email, faculty, department);
        this.advisorUUID = advisorUUID;
    }

    //Constructor for reading file
    public Student(String uuid, String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileUrl, String StudentId, String email, String faculty, String department, String advisorUUID, boolean registered) {
        super(uuid, role, username, password, firstName, lastName, access, loginDate, profileUrl);
        this.studentId = StudentId;
        this.email = email;
        this.facultyUUID = UUID.fromString(faculty);
        this.departmentUUID = UUID.fromString(department);
        if (advisorUUID == null || advisorUUID.equals("null")){
            this.advisorUUID = null;
        }else {
            this.advisorUUID = UUID.fromString(advisorUUID);
        }
        this.registered = registered;
    }

    public void registration(String username, String password){
        setUsername(username);
        setPasswordHash(password);
        setRegistered();
    }

    // Setters
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFacultyUUID(UUID facultyUUID) {
        this.facultyUUID = facultyUUID;
    }
    public void setDepartmentUUID(UUID departmentUUID) {
        this.departmentUUID = departmentUUID;
    }
    public void setAdvisor(UUID advisor) {
        this.advisorUUID = advisor;
    }
    private void setRegistered() {
        this.registered = true;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }
    public String getEmail() {
        return email;
    }
    public String getFaculty() {
        Datasource<FacultyList> facultyListDatasource = new FacultyListDatasource("data", "faculties.csv");
        String faculty = facultyListDatasource.readData().findFacultyByUUID(this.facultyUUID).getFacultyName();
        return faculty;
    }
    public UUID getFacultyUUID() {
        return facultyUUID;
    }
    public UUID getDepartmentUUID() { return departmentUUID; }
    public String getDepartment() {
        Datasource<MajorList> datasource = new MajorListDatasource("data", "majors.csv");
        String department = datasource.readData().findMajorByUUID(this.departmentUUID).getMajorName();
        return department;
    }
    public UUID getAdvisorUUID() {
        return advisorUUID;
    }
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public String toString() {
        return super.toString() +","+ studentId + "," + email + "," + facultyUUID + "," + departmentUUID + "," + advisorUUID + "," + registered;
    }

    @Override
    public String getRoleInEnglish() {
        return "student";
    }
}
