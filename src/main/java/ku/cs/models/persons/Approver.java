package ku.cs.models.persons;

import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;

public class Approver {
    private String tier;
    private String firstName;
    private String lastName;
    private String role;

    //Constructor
    public Approver(String firstName, String lastName, String role, User adder) {
        this.tier = adder.getRole().equals("เจ้าหน้าที่คณะ") ? "ผู้อนุมัติคำร้องระดับคณะ" : "ผู้อนุมัติคำร้องระดับภาควิชา";
        this.firstName = firstName;
        this.lastName = lastName;
        setRole(role, adder);
    }
    //Constructor for reading file
    public Approver(String tier, String firstName, String lastName, String role) {
        this.tier = tier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    //Getter
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName(){return firstName + " " + lastName;}
    public String getTier(){
        return tier;
    }

    public String getRole() {
        return role;
    }

    //Setter
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setRole(String role, User adder){
        this.role = role;
        if (adder instanceof DepartmentStaff){
            this.role += ((DepartmentStaff) adder).getDepartment();
        }else if (adder instanceof FacultyStaff){
            this.role += "คณะ" + ((FacultyStaff) adder).getFaculty();
        }
    }

    @Override
    public String toString() {
        return tier + "," + firstName + "," + lastName + "," + role;
    }

}
