package ku.cs.models.users;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class User {
    private String role;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String fullName;
    private boolean ban;

    public User(String username, String password, String role) {
        this.role = role;
        this.username = username;
        this.ban = false;
        setPassword(password);
    }
    public User(String userName, String password, String firstName, String lastName, String role) {
        this(userName, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public boolean isUsername(String username) {
        return this.username.equals(username);
    }

    public void setPassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }

    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }

    public boolean isBanned() { return ban; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public abstract void login();

}
