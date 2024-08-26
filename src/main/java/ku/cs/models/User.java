package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public User() {}
    public User(String username, String password) {
        this.username = username;
        setPassword(password);
    }
    public User(String userName, String password, String firstName, String lastName) {
        this(userName, password);
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getUserId() { return username; }

    public String getPassword() { return password; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
