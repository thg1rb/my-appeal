package ku.cs.models.appeal;

import ku.cs.services.AppealListFileDatasource;

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
    private String startDate;
    private String endDate;

    // Suspend Fields
    private String semester;
    private String year;

    // General Constructor
    public Appeal(String createDate, String type, String owner, String topic, String reason) {
        this.status = "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
        this.createDate = createDate;
        this.type = type;
        this.owner = owner;
        this.topic = topic;
        this.reason = reason;
    }

    // Break Constructor
    public Appeal(String createDate, String type, String owner, String reason, String purpose, String subjects, String startDate, String endDate) {
        this.status = "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
        this.createDate = createDate;
        this.type = type;
        this.owner = owner;
        this.reason = reason;
        this.purpose = purpose;
        this.subjects = subjects;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Suspend Constructor
    public Appeal(String createDate, String type, String owner, String reason, String semester, String year, String subjects) {
        this.status = "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
        this.createDate = createDate;
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

    public String getStartDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        String startDateString = sdf.format(startDate);
//        return startDateString;
        return startDate;
    }


    public String getEndDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        String endDateString = sdf.format(endDate);
//        return endDateString;
        return endDate;
    }

    public String getSemester() {
        return semester;
    }
    public String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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

    @Override
    public String toString() {
        return createDate + "," + owner + "," + type + "," + status + "," + topic + "," +  reason + "," + purpose + "," + subjects + "," + startDate + "," + endDate + "," + semester + "," + year;
    }
}
