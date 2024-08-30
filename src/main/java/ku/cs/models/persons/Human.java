package ku.cs.models.persons;

public class Human {
    private String firstName;
    private String lastName;
    private String fullName;

    public Human() {}
    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return fullName;
    }
}
