package ku.cs.models.appeals;

public class Appeal {

    // Fields
    protected String modifyDate;
    protected String uuid;
    protected String type;
    protected String status;
    protected String rejectedReason;
    protected String ownerId;
    protected String ownerFullName;
    protected String ownerDepartment;
    protected String ownerFaculty;
    protected String reason;
    protected String subjects;

    // Constructors
    public Appeal(String modifyDate, String uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, String ownerDepartment, String ownerFaculty, String reason, String subjects) {
        this.modifyDate = modifyDate;
        this.uuid = uuid;
        this.type = type;
        this.status = status;
        this.rejectedReason = rejectedReason;
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
        this.ownerDepartment = ownerDepartment;
        this.ownerFaculty = ownerFaculty;
        this.reason = reason;
        this.subjects = subjects;
    }

    public Appeal(Appeal appeal) {
        this(appeal.modifyDate, appeal.getUuid(), appeal.getType(), appeal.getStatus(), appeal.getRejectedReason(), appeal.getOwnerId(), appeal.getOwnerFullName(), appeal.getOwnerDepartment(), appeal.getOwnerFaculty(), appeal.getReason(), appeal.getSubjects());
    }

    // ตรวจสอบเป็นคำร้องทั่วไปหรือไม่?
    public Boolean isGeneralAppeal() {
        return this instanceof GeneralAppeal;
    }

    // ตรวจสอบเป็นคำร้องขอพักการศึกษาหรือไม่?
    public Boolean isSuspendAppeal() {
        return this instanceof SuspendAppeal;
    }

    // ตรวจสอบเป็นคำร้องขอลาป่วยหรือลากิจหรือไม่?
    public Boolean isBreakAppeal() {
        return this instanceof BreakAppeal;
    }

    // Getters
    public String getModifyDate() {
        return modifyDate;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public String getOwnerDepartment() {
        return ownerDepartment;
    }

    public String getOwnerFaculty() {
        return ownerFaculty;
    }

    public String getReason() {
        return reason;
    }

    public String getSubjects() {
        return subjects;
    }

    // Setters
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRejectedReason(String rejectedReason) { this.rejectedReason = rejectedReason; }

    // Overriding Method
    @Override
    public String toString() {
        return modifyDate + "," + uuid + "," + type + "," + status + "," + rejectedReason + "," + ownerId + "," + ownerFullName + "," + ownerDepartment + "," + ownerFaculty + "," + reason + "," + subjects;
    }
}
