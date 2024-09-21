package ku.cs.models.collections;

import ku.cs.models.persons.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserList {
    private List<User> users;

    //Constructor
    public UserList() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addUserLists(UserList userList) {
        this.users.addAll(userList.users);
    }

    public void addUser(String[] data){
        switch (data[1]){
            case "ผู้ดูแลระบบ":
                addUser(new AdminUser(data[0] ,data[1], data[2], data[3], data[4], data[5], Boolean.parseBoolean(data[6]), data[7], data[8]));
                break;
            case "เจ้าหน้าที่คณะ":
                addUser(new FacultyStaff(data[0], data[1], data[2], data[3], data[4], data[5], Boolean.parseBoolean(data[6]), data[7], data[8], data[9], data[10], data[11]));
                break;
            case "เจ้าหน้าที่ภาควิชา":
                addUser(new DepartmentStaff(data[0], data[1], data[2], data[3], data[4], data[5], Boolean.parseBoolean(data[6]), data[7], data[8], data[9], data[10], data[11], data[12]));
                break;
            case "อาจารย์ที่ปรึกษา":
                addUser(new Advisor(data[0], data[1], data[2], data[3], data[4], data[5], Boolean.parseBoolean(data[6]), data[7], data[8], data[9], data[10], data[11], data[12], data[13]));
                break;
            case "นักศึกษา":
                addUser(new Student(data[0], data[1], data[2], data[3], data[4], data[5], Boolean.parseBoolean(data[6]), data[7], data[8], data[9], data[10], data[11], data[12], data[13], Boolean.parseBoolean(data[14])));
                break;
        }
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User findStudentUserByInformation(String firstName, String lastName, String personalId, String email) {
        for (User user : users) {
            if (user instanceof Student student) {
                if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName) && student.getStudentId().equals(personalId) && student.getEmail().equals(email)) {
                    return student;
                }
            }
        }
        return null;
    }

    public User findUserByUUID(UUID uuid){
        for (User user : users) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    public void deleteUser(User user) {
        this.users.remove(user);
    }

    //Getter
    public UserList getSpecificRoleUser(String role){
        return switch (role) {
            case "admin" -> getAdmins();
            case "facultyStaff" -> getFacultyStaffs();
            case "majorStaff" -> getDepartmentStaffs();
            case "advisor" -> getAdvisors();
            case "student" -> getStudents();
            default -> null;
        };
    }

    public UserList getAdmins(){
        UserList admins = new UserList();
        for (User user : users) {
            if (user instanceof AdminUser) {
                admins.addUser(user);
            }
        }
        return admins;
    }

    public UserList getFacultyStaffs() {
        UserList facultyStaff = new UserList();
        for (User user : this.users) {
            if (user instanceof FacultyStaff && !(user instanceof DepartmentStaff)) {
                facultyStaff.addUser(user);
            }
        }
        return facultyStaff;
    }

    public UserList getDepartmentStaffs() {
        UserList departmentStaff = new UserList();
        for (User user : this.users) {
            if (user instanceof DepartmentStaff && !(user instanceof Advisor)) {
                departmentStaff.addUser(user);
            }
        }
        return departmentStaff;
    }

    public UserList getAdvisors() {
        UserList advisor = new UserList();
        for (User user : this.users) {
            if (user instanceof Advisor) {
                advisor.addUser(user);
            }
        }
        return advisor;
    }

    public UserList getStudents() {
        UserList students = new UserList();
        for (User user : this.users) {
            if (user instanceof Student) {
                students.addUser(user);
            }
        }
        return students;
    }

    public UserList getRegisteredStudents(){
        UserList registeredStudents = new UserList();
        for (User user : this.users) {
            if (user instanceof Student && ((Student) user).isRegistered()) {
                registeredStudents.addUser(user);
            }
        }
        return registeredStudents;
    }

    public UserList getActiveUser(){
        UserList activeUsers = new UserList();
        for (User user : this.users) {
            if (user instanceof Student && ((Student) user).isRegistered()) {
                activeUsers.addUser(user);
            } else if (!(user instanceof Student)){
                activeUsers.addUser(user);
            }
        }
        return activeUsers;
    }

    public List<User> getUsers() {
        return users;
    }
}
