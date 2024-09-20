package ku.cs.models.appeals;

public class BreakAppeal extends Appeal {

    // Fields
    private String purpose;
    private String startDate;
    private String endDate;

    // Constructor
    public BreakAppeal(String modifyDate, String uuid, String type, String status, String ownerId, String ownerFullName, String ownerDepartment, String ownerFaculty, String reason, String subjects, String purpose, String startDate, String endDate) {
        super(modifyDate, uuid, type, status, ownerId, ownerFullName, ownerDepartment, ownerFaculty, reason, subjects);
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
