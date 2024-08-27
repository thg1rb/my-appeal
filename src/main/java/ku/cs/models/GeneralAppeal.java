package ku.cs.models;

public class GeneralAppeal extends Appeal {
    private String topic;   // เรื่องที่ต้องการจะต้อง
    private String details; // รายละเอียดคำร้อง

    // Setter
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Getter
    public String getTopic() {
        return topic;
    }

    public String getDetails() {
        return details;
    }

}
