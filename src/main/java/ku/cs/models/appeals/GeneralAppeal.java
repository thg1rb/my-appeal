package ku.cs.models.appeals;

public class GeneralAppeal extends Appeal {

    // A Field
    private String topic;

    // Constructor
    public GeneralAppeal(String modifyDate, String uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, String ownerDepartmentUuid, String ownerFacultyUuid, String departmentSignature, String facultySignature, String reason, String subject, String topic) {
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
