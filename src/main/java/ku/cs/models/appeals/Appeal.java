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
    protected String ownerDepartmentUuid;
    protected String ownerFacultyUuid;
    protected String departmentSignature;
    protected String facultySignature;
    protected String reason;
    protected String subjects;

    // Constructors
    public Appeal(String modifyDate, String uuid, String type, String status, String rejectedReason, String ownerId, String ownerFullName, String ownerDepartmentUuid, String ownerFacultyUuid, String departmentSignature, String facultySignature, String reason, String subjects) {
        this.modifyDate = modifyDate;
        this.uuid = uuid;
        this.type = type;
        this.status = status;

        if (rejectedReason == null || rejectedReason.equals("null")) this.rejectedReason = null;
        else this.rejectedReason = rejectedReason;

        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
        this.ownerDepartmentUuid = ownerDepartmentUuid;
        this.ownerFacultyUuid = ownerFacultyUuid;

        if (departmentSignature == null || departmentSignature.equals("null")) this.departmentSignature = null;
        else this.departmentSignature = departmentSignature;

        if (facultySignature == null || facultySignature.equals("null")) this.facultySignature = null;
        else this.facultySignature = facultySignature;

        this.reason = reason;
        this.subjects = subjects;
    }

    public Appeal(Appeal appeal) {
        this(appeal.modifyDate, appeal.getUuid(), appeal.getType(), appeal.getStatus(), appeal.getRejectedReason(), appeal.getOwnerId(), appeal.getOwnerFullName(), appeal.getOwnerDepartmentUuid(), appeal.getOwnerFacultyUuid(), appeal.getDepartmentSignature(), appeal.getFacultySignature(), appeal.getReason(), appeal.getSubjects());
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

    public String getOwnerDepartmentUuid() {
        return ownerDepartmentUuid;
    }

    public String getOwnerFacultyUuid() {
        return ownerFacultyUuid;
    }

    public String getReason() {
        return reason;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getDepartmentSignature() {
        return departmentSignature;
    }

    public String getFacultySignature() {
        return facultySignature;
    }

    // Setters
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRejectedReason(String rejectedReason) { this.rejectedReason = rejectedReason; }

    public void setDepartmentSignature(String path) { this.departmentSignature = path; }

    public void setFacultySignature(String path) { this.facultySignature = path; }

    // Overriding Method
    @Override
    public String toString() {
        return modifyDate + ","
                + uuid + ","
                + type + ","
                + status + ","
                + rejectedReason + ","
                + ownerId + ","
                + ownerFullName + ","
                + ownerDepartmentUuid + ","
                + ownerFacultyUuid + ","
                + departmentSignature + ","
                + facultySignature + ","
                + reason + ","
                + subjects;
    }
}
