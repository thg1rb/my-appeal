package ku.cs.models.dates;

import ku.cs.models.appeals.Appeal;

public class ModifyDate {
    private String uuid;
    private String createDate;
    private String advisorApproveDate;
    private String departmentApproveDate;
    private String facultyApproveDate;

    // Constructors
    public ModifyDate(String uuid, String createDate) {
        this.uuid = uuid;
        this.createDate = createDate;
    }

    public ModifyDate(String uuid, String createDate, String advisorApproveDate, String departmentApproveDate, String facultyApproveDate) {
        this(uuid, createDate);
        this.advisorApproveDate = advisorApproveDate;
        this.departmentApproveDate = departmentApproveDate;
        this.facultyApproveDate = facultyApproveDate;
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
        return advisorApproveDate.equals("null");
    }

    public boolean isDepartmentPending() {
        return departmentApproveDate.equals("null");
    }

    public boolean isFacultyPending() {
        return facultyApproveDate.equals("null");
    }

    // Overriding Method
    @Override
    public String toString() {
        return uuid + "," + createDate + "," + advisorApproveDate + "," + departmentApproveDate + "," + facultyApproveDate;
    }
}
