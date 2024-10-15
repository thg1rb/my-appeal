package ku.cs.models;

import java.util.UUID;

public class Faculty implements Displayable {
    private UUID uuid;

    private String facultyName;
    private String facultyId;

    //Constructor
    public Faculty() {
        this.uuid = UUID.randomUUID();
    }
    public Faculty(String facultyName, String facultyId) {
        this();
        this.facultyName = facultyName;
        this.facultyId = facultyId;
    }
    //Constructor for read file
    public Faculty(String uuid, String facultyName, String facultyId) {
        this.uuid = UUID.fromString(uuid);
        this.facultyName = facultyName;
        this.facultyId = facultyId;
    }

    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }

    public String getFacultyName() { return facultyName; }

    public String getFacultyId() { return facultyId; }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return uuid.toString() + "," + facultyName + "," + facultyId;
    }

    @Override
    public String getDisplayName() {
        return this.getFacultyName();
    }
}
