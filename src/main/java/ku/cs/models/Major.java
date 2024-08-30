package ku.cs.models;

public class Major {
    private String majorName;
    private String faculty;
    private String majorId;

    public Major() { }
    public Major(String majorName, String faculty, String majorId) {
        this.majorName = majorName;
        this.faculty = faculty;
        this.majorId = majorId;
    }

    public void setMajorName(String majorName) { this.majorName = majorName; }

    public void setFaculty(String faculty) { this.faculty = faculty; }

    public void setMajorId(String majorId) { this.majorId = majorId; }

    public String getMajorName() { return majorName; }

    public String getFaculty() { return faculty; }

    public String getMajorId() { return majorId; }

    @Override
    public String toString() {
        return majorName + "," + faculty + "," + majorId;
    }
}
