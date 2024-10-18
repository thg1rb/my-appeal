package ku.cs.models.dates;

import java.util.UUID;

public class ModifyDate {

    // Fields
    private UUID uuid;
    private String createDate;
    private String advisorApproveDate;
    private String departmentApproveDate;
    private String facultyApproveDate;

    // Constructors
    public ModifyDate(UUID uuid, String createDate) {
        this.uuid = uuid;
        this.createDate = createDate;
    }

    public ModifyDate(UUID uuid, String createDate, String advisorApproveDate, String departmentApproveDate, String facultyApproveDate) {
        this(uuid, createDate);

        if (advisorApproveDate == null || advisorApproveDate.equals("null")) this.advisorApproveDate = null;
        else this.advisorApproveDate = advisorApproveDate;

        if (departmentApproveDate == null || departmentApproveDate.equals("null")) this.departmentApproveDate = null;
        else this.departmentApproveDate = departmentApproveDate;

        if (facultyApproveDate == null || facultyApproveDate.equals("null")) this.facultyApproveDate = null;
        else this.facultyApproveDate = facultyApproveDate;
    }

    // Getters
    public UUID getUuid() {
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

    public String getFacultyApproveDate() {
        return facultyApproveDate;
    }

    // Setters
    public void setAdvisorApproveDate(String advisorApproveDate) {
        this.advisorApproveDate = advisorApproveDate;
    }

    public void setDepartmentApproveDate(String departmentApproveDate) {
        this.departmentApproveDate = departmentApproveDate;
    }

    public void setFacultyApproveDate(String facultyApproveDate) {
        this.facultyApproveDate = facultyApproveDate;
    }

    public boolean isAdvisorRejected(String status) {
        return advisorApproveDate != null && status.contains("ปฏิเสธโดยอาจารย์ที่ปรึกษา");
    }

    public boolean isDepartmentRejected(String status) {
        return departmentApproveDate != null && status.contains("ปฏิเสธโดยหัวหน้าภาควิชา");
    }

    public boolean isFacultyRejected(String status) {
        return facultyApproveDate != null && status.contains("ปฏิเสธโดยคณบดี");
    }

    public boolean isAdvisorPending() {
        return advisorApproveDate == null;
    }

    public boolean isDepartmentPending() {
        return departmentApproveDate == null;
    }

    public boolean isFacultyPending() {
        return facultyApproveDate == null;
    }

    // Overriding Method
    @Override
    public String toString() {
        return uuid + "," + createDate + "," + advisorApproveDate + "," + departmentApproveDate + "," + facultyApproveDate;
    }
}
