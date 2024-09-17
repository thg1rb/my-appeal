package ku.cs.models.persons;

public class Approver{
    private String firstName;
    private String lastName;
    private String fullName;
    private String faculty;
    private String department;
    private String role;

    public Approver(String firstName, String lastName, String faculty, String major,String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.faculty = faculty;
        this.department= major;
        this.role = role;
    }

    public Approver(String firstName, String lastName, String role,User adder) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        if (adder instanceof FacultyStaff) {
            this.faculty = ((FacultyStaff) adder).getFaculty();
        }
        if (adder instanceof DepartmentStaff) {
            this.role += ((DepartmentStaff) adder).getDepartment();
            this.department = ((DepartmentStaff) adder).getDepartment();
        }
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setRole(String role) { this.role = role; }
    public void setFullName(){
        this.fullName = firstName + " " + lastName;
    }

    public String getFaculty() {
        return faculty;
    }
    public String getDepartment(){
        return department;
    }
    public String getRole() { return role; }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName(){
        return firstName+" "+lastName;
    }

    @Override
    public String toString() {
        return  firstName + "," +
                lastName + "," +
                faculty + "," + department + "," + role;
    }


}
