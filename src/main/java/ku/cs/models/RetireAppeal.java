package ku.cs.models;

public class RetireAppeal extends Appeal {
    private double gpa;     // เกรดเฉลี่ยสะสม
    private int tcas;       // เข้า TCAS รอบที่ (1, 2, 3, 4)
    private String details; // รายละเอียด

    // Setter
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setTcas(int tcas) {
        this.tcas = tcas;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Getter
    public double getGpa() {
        return gpa;
    }

    public int getTcas() {
        return tcas;
    }

    public String getDetails() {
        return details;
    }

}
