package ku.cs.models.dates;

public class ModifyDate {
    private String uuid;
    private String createDate;
    private String advisorApproveDate;
    private String departmentApproveDate;
    private String deanApproveDate;

    // Constructors
    public ModifyDate(String uuid, String createDate) {
        this.uuid = uuid;
        this.createDate = createDate;
    }

    public ModifyDate(String uuid, String createDate, String advisorApproveDate, String departmentApproveDate, String deanApproveDate) {
        this(uuid, createDate);
        this.advisorApproveDate = advisorApproveDate;
        this.departmentApproveDate = departmentApproveDate;
        this.deanApproveDate = deanApproveDate;
    }

    // Getters
    public String getUuid() {
        return uuid;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getAdvisorApproveDate() {
        return advisorApproveDate;
    }

    public String getDepartmentApproveDate() {
        return departmentApproveDate;
    }

    public String getDeanApproveDate() {
        return deanApproveDate;
    }

    // Setters
    public void setAdvisorApproveDate(String advisorApproveDate) {
        this.advisorApproveDate = advisorApproveDate;
    }

    public void setDepartmentApproveDate(String departmentApproveDate) {
        this.departmentApproveDate = departmentApproveDate;
    }

    public void setDeanApproveDate(String deanApproveDate) {
        this.deanApproveDate = deanApproveDate;
    }

    // Overriding Method
    @Override
    public String toString() {
        return uuid + "," + createDate + "," + advisorApproveDate + "," + departmentApproveDate + "," + deanApproveDate;
    }
}
