package ku.cs.models;

public class SuspendAppeal extends Appeal {
    private String reason;          // เหตุผล
    private String academicTerm;    // ภาคการศึกษา
    private int year;               // ปีการศึกษา
    private String subjects;        // วิชาที่ลงทะเบียนไปในเทอมนี้
    public SuspendAppeal(String reason, String academicTerm, int year, String subjects, String topic, String details) {
        this.reason = reason;
        this.academicTerm = academicTerm;
        this.year = year;
        this.subjects = subjects;
        this.topic = topic;
        this.details = details;
    }

    // Setter
    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    // Getter
    public String getReason() {
        return reason;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public int getYear() {
        return year;
    }

    public String getSubjects() {
        return subjects;
    }
}
