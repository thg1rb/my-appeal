package ku.cs.models;

public class User {
    private String userName;
    private String initialPassword;
    private String password;
    private String firstName;
    private String lastName;

    public User() {}
    public User(String userName, String initialPassword, String firstName, String lastName) {
        this.userName = userName;
        this.initialPassword = initialPassword;
        this.password = initialPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserId() {
        return userName;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
