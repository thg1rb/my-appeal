package ku.cs.models;

import java.util.Date;

public class Appeal {
    protected String type;  // ประเภทคำร้อง
    protected String status;
    protected Date date;
    protected String topic;
    protected String details;



    public Appeal(){}
    public Appeal(String type){
        this.type = type;
    }
    // Setter
    protected void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    // Getter
    public String getType() {
        return this.type;
    }
    public String getStatus() {return this.status;}
    public String getDetails() {return this.details;
    }
}
