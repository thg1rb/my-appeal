package ku.cs.models.appeals;

public class SuspendAppeal extends Appeal {

    // Fields
    private String semester;
    private String year;

    // Constructor
    public SuspendAppeal(String modifyDate, String uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, String ownerDepartment, String ownerFaculty, String reason, String subjects, String semester, String year) {
        super(modifyDate, uuid, type, status, rejectedReason, ownerId, ownerFullName, ownerDepartment, ownerFaculty, reason, subjects, null, null);
        this.semester = semester;
        this.year = year;
    }

    // Getters
    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    // Overriding Method
    @Override
    public String toString() {
        return super.toString() + "," + semester + "," + year;
    }
}
