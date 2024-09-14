package ku.cs.models.collection;

import ku.cs.models.person.*;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<User> users;

    //Constructor
    public UserList() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addUser(String[] data){
        switch (data[0]){
            case "ผู้ดูแลระบบ":
                addUser(new AdminUser(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]), data[6], data[7]));
                break;
            case "เจ้าหน้าที่คณะ":
                addUser(new FacultyStaff(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]), data[6], data[7], data[8], data[9], data[10]));
                break;
            case "เจ้าหน้าที่ภาควิชา":
                addUser(new DepartmentStaff(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]), data[6], data[7], data[8], data[9], data[10], data[11]));
                break;
            case "อาจารย์ที่ปรึกษา":
                addUser(new Advisor(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]), data[6], data[7], data[8], data[9], data[10], data[11], data[12]));
                break;
            case "นักศึกษา":
                addUser(new Student(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]), data[6], data[7], data[8], data[9], data[10], data[11], data[12], Boolean.parseBoolean(data[13])));
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

    //Getter
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

    public List<User> getUsers() {
        return users;
    }
}
