package ku.cs.models;

import java.util.UUID;

public class Department implements Displayable {
    private UUID uuid;

    private String departmentName;
    private String departmentId;

    private UUID facultyUUID;

    //Constructor
    public Department() {
        this.uuid = UUID.randomUUID();
    }
    public Department(String departmentName, UUID faculty, String departmentId) {
        this();
        this.departmentName = departmentName;
        this.facultyUUID = faculty;
        this.departmentId = departmentId;
    }

    //Constructor for reading file
    public Department(String uuid, String departmentName, String faculty, String departmentId) {
        this.uuid = UUID.fromString(uuid);
        this.departmentName = departmentName;
        this.facultyUUID = UUID.fromString(faculty);
        this.departmentId = departmentId;
    }

    //setter
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

//    public void setFaculty(String faculty) { this.faculty = faculty; }

    public UUID getFacultyUUID() {
        return facultyUUID;
    }

    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    //getter
    public String getDepartmentName() { return departmentName; }

//    public String getFaculty() { return faculty; }

    public void setFacultyUUID(UUID facultyUUID) {
        this.facultyUUID = facultyUUID;
    }

    public String getDepartmentId() { return departmentId; }

    public UUID getUuid() { return uuid; }

    @Override
    public String toString() {
        return uuid.toString() + "," + departmentName + "," + facultyUUID + "," + departmentId;
    }

    @Override
    public String getDisplayName() {
        return this.getDepartmentName();
    }
}
