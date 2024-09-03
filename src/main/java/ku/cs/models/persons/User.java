package ku.cs.models.persons;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.HashMap;

public class User extends Human {
    private final String role;

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

    private String advisor;

    public User(HashMap<String, String> user){
        super(user.get("firstName"), user.get("lastName"));
        this.role = user.get("role");
        this.path = user.get("path");

        this.username = user.get("username");
        this.password = user.get("password");
        this.initialPassword = user.get("initialPassword");
        this.initialPasswordText = user.get("initialPasswordText");

        this.faculty = user.get("faculty");
        this.major = user.get("major");

        this.email = user.get("email");
        this.id = user.get("id");

        this.loginDate = user.get("loginDate");
        this.advisor = user.get("advisor");
    }

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
    public User(String role, String username, String password, String firstName, String lastName, String faculty, String major, String id, String email, String advisor){
        this(role, username, password, firstName, lastName, faculty, major, id);
        this.email = email;
        this.advisor = advisor;
    }

    public User(String username, String password,Student student){
        this("นักศึกษา", username, password,student.getFirstName(), student.getLastName());
        this.advisor = student.getAdvisor();
    }

    // Constructor for Create object from file
    public User(String role, String username, String password, String initialPassword, String initialPasswordText, String firstName, String lastName, String faculty, String major, String id, String email, String loginDate, String advisor, boolean ban, String imgUrl){
        this(role, username, password, firstName, lastName, faculty, major, id);
        this.password = password;
        this.initialPassword = initialPassword;
        this.initialPasswordText = initialPasswordText;
        this.email = email;
        this.loginDate = loginDate;
        this.advisor = advisor;
        this.ban = ban;
        this.path = imgUrl;
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

    public void setUsername(String username){
        this.username = username;
    }

    public void setImage(String path){
        this.path = path;
    }

    public void setAdvisor(String advisor){
        this.advisor = advisor;
    }

    public void setInitialPasswordText(String initialPasswordText) {
        this.initialPasswordText = initialPasswordText;
        setInitPassword(initialPasswordText);
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public void banUser(){
        this.ban = true;
    }

    public void unbanUser(){
        this.ban = false;
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

    public String getBan(){
        return ban ? "ไม่มีสิทธิ์การใช้งาน" : "มีสิทธิ์การใช้งาน";
    }

    public String getAdvisor() {
        return advisor;
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put("role", role);
        map.put("firstName", this.getFirstName());
        map.put("lastName", this.getLastName());
//        map.put("fullName", this.getFullName());
        map.put("username", username);
        map.put("password", password);
        map.put("initialPassword", initialPassword);
        map.put("initialPasswordText", initialPasswordText);
        map.put("faculty", faculty);
        map.put("major", major);
        map.put("email", email);
        map.put("id", id);
        map.put("loginDate", loginDate);
        map.put("ban", Boolean.toString(ban));
        map.put("path", path);
        map.put("advisor", advisor);
        return map;
    }

    @Override
    public String toString() {
        return  role + "," +
                getFirstName() + "," +
                getLastName() + "," +
                username + "," +
                password + "," +
                initialPassword + "," +
                initialPasswordText + "," +
                faculty + "," +
                major + "," +
                email + "," +
                id + "," +
                loginDate+ "," +
                ban + "," +
                path + "," + advisor;

    }
}
