package ku.cs.models.persons;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.UUID;

public abstract class User {
    private UUID uuid;

    private final String role;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private boolean accessibility;
    private String loginDate;

    private String profileUrl;


    //Constructor
    public User(String role, String username, String password, String firstName, String lastName){
        this.uuid = UUID.randomUUID();
        this.role = role;
        this.username = username;
        setPasswordHash(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessibility = true;
        this.profileUrl = "/images/default-profile.jpg";
    }
    //Constructor for add student in Department responsibility
    public User(String role, String firstName, String lastName){
        this.uuid = UUID.randomUUID();
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessibility = true;
        this.profileUrl = "/images/default-profile.jpg";
    }
    //Constructor for reading file
    public User(String uuid,String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileUrl) {
        this.uuid = UUID.fromString(uuid);
        this.role = role;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.accessibility = access;
        this.loginDate = loginDate;
        this.profileUrl = profileUrl;
    }

    //Authentication
    public boolean isUsername(String username) {
        return this.username.equals(username);
    }
    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }
    public void setPasswordHash(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    //Setter
    public void setUsername(String username){
        this.username = username;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setLoginDate(String loginDate){
        this.loginDate = loginDate;
    }
    public void unbanUser(){
        this.accessibility = false;
    }
    public void banUser(){
        this.accessibility = true;
    }
    public void setProfile(String path){
        this.profileUrl = path;
    }

    //Getter
    public UUID getUuid() {
        return uuid;
    }
    public String getRole(){
        return role;
    }
    public String getUsername(){
        return username;
    }
    public String getProfileUrl(){
        return profileUrl;
    }
    public boolean hasAccessibility(){
        return accessibility;
    }
    public String getAccesibility(){
        return accessibility ? "มีสิทธิ์การใช้งาน" : "ไม่มีสิทธิ์การใช้งาน" ;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getFullName(){
        return firstName + " " + lastName;
    }
    public String getLoginDate(){
        return loginDate;
    }

    @Override
    public String toString(){
        return uuid + "," + role + "," + username + "," + password + "," + firstName + "," + lastName + "," + accessibility + "," + loginDate + "," + profileUrl;
    }

    public abstract String getRoleInEnglish();
}
