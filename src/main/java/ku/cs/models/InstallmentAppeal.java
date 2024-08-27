package ku.cs.models;

public class InstallmentAppeal extends Appeal {
    private String academicTerm;    // ภาคการศึกษา (ต้น, กลาง, ปลาย)
    private int year;               // ปีการศึกษา
    private String reason;          // เหตุผล

    // Setter
    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // Getter
    public String getAcademicTerm() {
        return academicTerm;
    }

    public int getYear() {
        return year;
    }

    public String getReason() {
        return reason;
    }
}
