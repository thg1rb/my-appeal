package ku.cs.models;

public class Faculty {
    private String facultyName;
    private String facultyId;

    public Faculty() { }
    public Faculty(String facultyName, String facultyId) {
        this();
        this.facultyName = facultyName;
        this.facultyId = facultyId;
    }

    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }

    public String getFacultyName() { return facultyName; }

    public String getFacultyId() { return facultyId; }

    @Override
    public String toString() {
        return facultyName + "," + facultyId;
    }
}
