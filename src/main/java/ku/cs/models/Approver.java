package ku.cs.models;

import ku.cs.models.users.StaffFaculty;
import ku.cs.models.users.StaffMajor;
import ku.cs.models.users.User;

public class Approver {
    private String firstName;
    private String lastName;
    private String fullName;

    private String role;

    public Approver(String firstName, String lastName, String role, User creator) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.role = role;
        if (creator instanceof StaffFaculty) {
            this.role += ((StaffFaculty) creator).getFaculty();
        }
        else if (creator instanceof StaffMajor){
            this.role += ((StaffMajor) creator).getMajor();
        }
    }

    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }



}
