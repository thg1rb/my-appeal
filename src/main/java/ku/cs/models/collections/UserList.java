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
    public UserList getRegisteredStudents(){
        UserList registeredStudents = new UserList();
        for (User user : this.users) {
            if (user instanceof Student && ((Student) user).isRegistered()) {
                registeredStudents.addUser(user);
            }
        }
        return registeredStudents;
    }

    public UserList getUsersByFacultyUUID(UUID facultyUUID){
        UserList users = new UserList();
        for (User user : this.users) {
            if (user instanceof FacultyStaff && ((FacultyStaff) user).getFacultyUUID().equals(facultyUUID)) {
                users.addUser(user);
            }else if (user instanceof Student && ((Student) user).getFacultyUUID().equals(facultyUUID)) {
                users.addUser(user);
            }
        }
        return users;
    }

    public UserList getUsersByDepartment(String department){
        UserList users = new UserList();
        for (User user : this.users) {
            if (user instanceof DepartmentStaff) {
                if (((DepartmentStaff) user).getDepartment().equals(department)) {
                    users.addUser(user);
                }
            }else if ((user instanceof Student)) {
                if (((Student) user).getDepartment().equals(department)) {
                    users.addUser(user);
                }
            }
        }
        return users;
    }

    public List<User> getUsers() {
        return users;
    }
}
