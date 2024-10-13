package ku.cs.models.appeals;

import java.util.UUID;

public class BreakAppeal extends Appeal {

    // Fields
    private String purpose;
    private String startDate;
    private String endDate;

    // Constructor
    public BreakAppeal(String modifyDate, UUID uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, UUID ownerDepartmentUuid, UUID ownerFacultyUuid, String departmentSignature, String facultySignature, String reason, String subjects, String purpose, String startDate, String endDate) {
        super(modifyDate, uuid, type, status, rejectedReason, ownerId, ownerFullName, ownerDepartmentUuid, ownerFacultyUuid, departmentSignature, facultySignature, reason, subjects);
        this.purpose = purpose;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getPurpose() {
        return purpose;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    // Overriding Method
    @Override
    public String toString() {
        return super.toString() + "," + purpose + "," + startDate + "," + endDate;
    }
}
