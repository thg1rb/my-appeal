package ku.cs.models.appeals;

public class Appeal {

    // Fields
    protected String modifyDate;
    protected String uuid;
    protected String type;
    protected String status;
    protected String ownerId;
    protected String ownerFullName;
    protected String reason;
    protected String subjects;

    // Constructor
    public Appeal(String modifyDate, String uuid, String type, String status, String ownerId, String ownerFullName, String reason, String subjects) {
        this.modifyDate = modifyDate;
        this.uuid = uuid;
        this.type = type;
        this.status = status;
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
        this.reason = reason;
        this.subjects = subjects;
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

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public String getReason() {
        return reason;
    }

    public String getSubjects() {
        return subjects;
    }

    // A setter
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Overriding Method
    @Override
    public String toString() {
        return modifyDate + "," + uuid + "," + type + "," + status + "," + ownerId + "," + ownerFullName + "," + reason + "," + subjects;
    }
}
