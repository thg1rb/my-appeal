package ku.cs.models;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class BreakAppeal extends Appeal {
    private String purpose;        // จุดประสงค์
    private Date startTakingBreak; // วันที่เริ่มต้นขอลา
    private Date endTakingBreak;   // วันที่สิ้นสุดขอลา
    private String reason;         // เหตุผล
    private String subjects;       // วิชาที่ต้องการขอลา



    public BreakAppeal(String purpose, String startTakingBreak, String endTakingBreak, String reason, String subjects, String details) {
        this.purpose = purpose;
        this.reason = reason;
        this.subjects = subjects;
        this.details = details;

        try {
            this.startTakingBreak = formatter.parse(startTakingBreak);
            this.endTakingBreak = formatter.parse(endTakingBreak);
        } catch (ParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
            this.date = null; // or handle accordingly
        }
    }

    // Setter
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setStartTakingBreak(Date startTakingBreak) {
        this.startTakingBreak = startTakingBreak;
    }

    public void setEndTakingBreak(Date endTakingBreak) {
        this.endTakingBreak = endTakingBreak;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    // Getter
    public String getPurpose() {
        return purpose;
    }

    public Date getStartTakingBreak() {
        return startTakingBreak;
    }

    public Date getEndTakingBreak() {
        return endTakingBreak;
    }

    public String getReason() {
        return reason;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getBreakTime(){return formatter.parse(startTakingBreak)}
}
