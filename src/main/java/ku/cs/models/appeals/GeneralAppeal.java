package ku.cs.models.appeals;

import java.util.UUID;

public class GeneralAppeal extends Appeal {

    // A Field
    private String topic;

    // Constructor
    public GeneralAppeal(String modifyDate, UUID uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, UUID ownerDepartmentUuid, UUID ownerFacultyUuid, String departmentSignature, String facultySignature, String reason, String subject, String topic) {
        super(modifyDate, uuid, type, status, rejectedReason, ownerId, ownerFullName, ownerDepartmentUuid, ownerFacultyUuid, departmentSignature, facultySignature, reason, subject);
        this.topic = topic;
    }

    // A Getter
    public String getTopic() {
        return topic;
    }

    // Overriding Method
    @Override
    public String toString() {
        return super.toString() + "," + topic;
    }
}
