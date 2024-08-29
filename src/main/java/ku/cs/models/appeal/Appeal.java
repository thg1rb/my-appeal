package ku.cs.models.appeal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appeal {
    // Shared Fields
    private String type;
    private String owner;
    private String status;
    private String createDate;
    private String createTime;

    // General Fields
    private String topic;
    private String reason;

    // Break Fields
    private String purpose;
    private String subjects;
    private Date startDate;
    private Date endDate;

    // Suspend Fields
    private String semester;
    private String year;

    // General Constructor
    public Appeal(String type, String owner, String topic, String reason) {
        this.status = "";
        this.createDate = new Date().toString();
        this.type = type;
        this.owner = owner;
        this.topic = topic;
        this.reason = reason;
    }

    // Break Constructor
    public Appeal(String type, String owner, String reason, String purpose, String subjects, Date startDate, Date endDate) {
        this.status = "";
        this.createDate = new Date().toString();
        this.type = type;
        this.owner = owner;
        this.reason = reason;
        this.purpose = purpose;
        this.subjects = subjects;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Suspend Constructor
    public Appeal(String type, String owner, String reason, String semester, String year, String subjects) {
        this.status = "";
        this.createDate = new Date().toString();
        this.type = type;
        this.owner = owner;
        this.reason = reason;
        this.semester = semester;
        this.year = year;
        this.subjects = subjects;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public String getTopic() {
        return topic;
    }

    public String getReason() {
        return reason;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getSubjects() {
        return subjects;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getSemester() {
        return semester;
    }
    public String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(this.createDate);
        return date;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreateTime() {
        // return Timestamp
        return null;
    }

    public String getYear() {
        return year;
    }
    // Setter
    public void setStatus(String status) {
        this.status = status;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String test = ("2556-05-23");
        Date date;
        try {
            // Parse the String date into a Date object
            date = sdf.parse(test);
        } catch (ParseException e) {
            // Handle the exception, possibly setting a default date or null
            System.out.println("Invalid date format: " + e.getMessage());
            date = null; // or handle accordingly
        }
        System.out.println(date.toString());
    }
}
