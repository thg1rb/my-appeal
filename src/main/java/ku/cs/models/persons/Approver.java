package ku.cs.models.persons;

public class Approver extends Human{
    private String role;

    public Approver(String firstName, String lastName, String role,User adder) {
        super(firstName, lastName);
        this.role = role;
        this.role += adder.getRole();
    }
    public String getRole() { return role; }
}
