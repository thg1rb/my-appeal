package ku.cs.models.appeals;

public class GeneralAppeal extends Appeal {

    // A Field
    private String topic;

    // Constructor
    public GeneralAppeal(String modifyDate, String uuid, String type, String status, String ownerId, String ownerFullName, String ownerDepartment, String ownerFaculty, String reason, String topic) {
        super(modifyDate, uuid, type, status, ownerId, ownerFullName, ownerDepartment, ownerFaculty, reason, null);
        this.topic = topic;
    }

    // A Getter
    public String getTopic() {
        return topic;
    }

    // Overriding Method
    @Override
    public String toString() {
        return super.toString() + "," + topic;
    }
}
