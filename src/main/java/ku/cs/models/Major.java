package ku.cs.models;

import java.util.UUID;

public class Major {
    private UUID uuid;

    private String majorName;
    private String majorId;

    private String faculty;

    //Constructor
    public Major() {
        this.uuid = UUID.randomUUID();
    }
    public Major(String majorName, String faculty, String majorId) {
        this();
        this.majorName = majorName;
        this.faculty = faculty;
        this.majorId = majorId;
    }

    //Constructor for reading file
    public Major(String uuid, String majorName, String faculty, String majorId) {
        this.uuid = UUID.fromString(uuid);
        this.majorName = majorName;
        this.faculty = faculty;
        this.majorId = majorId;
    }

    //setter
    public void setMajorName(String majorName) { this.majorName = majorName; }

    public void setFaculty(String faculty) { this.faculty = faculty; }

    public void setMajorId(String majorId) { this.majorId = majorId; }

    //getter
    public String getMajorName() { return majorName; }

    public String getFaculty() { return faculty; }

    public String getMajorId() { return majorId; }

    public UUID getUuid() { return uuid; }

    @Override
    public String toString() {
        return uuid.toString() + "," + majorName + "," + faculty + "," + majorId;
    }
}
