package ku.cs.models;

import java.util.UUID;

public class Department implements Displayable {
    private UUID uuid;

    private String majorName;
    private String departmentId;

    private UUID facultyUUID;

    //Constructor
    public Department() {
        this.uuid = UUID.randomUUID();
    }
    public Major(String majorName, UUID faculty, String departmentId) {
        this();
        this.majorName = majorName;
        this.facultyUUID = faculty;
        this.departmentId = departmentId;
    }

    //Constructor for reading file
    public Major(String uuid, String majorName, String faculty, String departmentId) {
        this.uuid = UUID.fromString(uuid);
        this.majorName = majorName;
        this.facultyUUID = UUID.fromString(faculty);
        this.departmentId = departmentId;
    }

    //setter
    public void setMajorName(String majorName) { this.majorName = majorName; }

//    public void setFaculty(String faculty) { this.faculty = faculty; }

    public UUID getFacultyUUID() {
        return facultyUUID;
    }

    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    //getter
    public String getMajorName() { return majorName; }

//    public String getFaculty() { return faculty; }

    public void setFacultyUUID(UUID facultyUUID) {
        this.facultyUUID = facultyUUID;
    }

    public String getDepartmentId() { return departmentId; }

    public UUID getUuid() { return uuid; }

    @Override
    public String toString() {
        return uuid.toString() + "," + majorName + "," + facultyUUID + "," + departmentId;
    }

    @Override
    public String getDisplayName() {
        return this.getMajorName();
    }
}
