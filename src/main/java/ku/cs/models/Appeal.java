package ku.cs.models;

public class Appeal {
    protected String type;  // ประเภทคำร้อง

    public Appeal(){}
    public Appeal(String type){
        this.type = type;
    }
    // Setter
    protected void setType(String type) {
        this.type = type;
    }

    // Getter
    public String getType() {
        return this.type;
    }
}
