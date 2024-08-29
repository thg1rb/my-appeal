package ku.cs.models.persons;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User extends Human {
    private String role;

    private String path;

    private String username;
    private String password;
    private boolean ban;

    private String initialPassword;
    private String initialPasswordText;

    private String faculty;
    private String major;

    private String email;

    private String id;

    private String loginDate;

    public User(String role, String username, String password, String firstName, String lastName){
        super(firstName, lastName);
        this.role = role;
        this.username = username;
        this.ban = false;
        setPassword(password);

        if (!role.equals("นักศึกษา") && !role.equals("ผู้ดูแลระบบ")) {
            setInitPassword(password);
            this.initialPasswordText = password;
        }
    }

    public User(String role, String username, String password, String firstName, String lastName, String faculty){
        this(role, username, password, firstName, lastName);
        this.faculty = faculty;
    }

    public User(String role, String username, String password, String firstName, String lastName, String faculty, String major){
        this(role, username, password, firstName, lastName, faculty);
        this.major = major;
    }

    public User(String role, String username, String password, String firstName, String lastName, String faculty, String major, String id){
        this(role, username, password, firstName, lastName, faculty, major);
        this.id = id;
    }

    public User(String role, String username, String password, String firstName, String lastName, String faculty, String major, String id, String email){
        this(role, username, password, firstName, lastName, faculty, major, id);
        this.email = email;
    }

    public User(String username, String password,Student student){
        this("นักศึกษา", username, password,student.getFirstName(), student.getLastName());
    }


    public boolean isUsername(String username) {
        return this.username.equals(username);
    }
    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }
    public boolean validateInitialPassword(String initialPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(initialPassword.toCharArray(), this.initialPassword);
        return result.verified;
    }

    private void setPassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    private void setInitPassword(String password) {
        this.initialPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public void setImage(String path){
        this.path = path;
    }

    public void banUser(){
        this.ban = true;
    }

    public boolean canLogin(String password){
        boolean verified = validatePassword(password);
        return verified && !isBan();
    }

    //Getter
    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public boolean isBan() {
        return ban;
    }

    public String getInitialPasswordText() {
        return initialPasswordText;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getMajor() {
        return major;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getLoginDate() {
        return loginDate;
    }
}
