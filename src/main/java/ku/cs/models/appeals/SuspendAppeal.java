package ku.cs.models.appeals;

import java.util.UUID;

public class SuspendAppeal extends Appeal {

    // Fields
    private String semester;
    private String year;

    // A Constructor
    public SuspendAppeal(String modifyDate, UUID uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, UUID ownerDepartmentUuid, UUID ownerFacultyUuid, String departmentSignature, String facultySignature, String reason, String subjects, String semester, String year) {
        super(modifyDate, uuid, type, status, rejectedReason, ownerId, ownerFullName, ownerDepartmentUuid, ownerFacultyUuid, departmentSignature, facultySignature, reason, subjects);
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
