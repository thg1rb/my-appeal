package ku.cs.models.collections;

import ku.cs.models.persons.User;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public boolean addUser(User user) {
        if (user != null){
            users.add(user);
            return true;
        }
        return false;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public User findUserByPersonalInformation(String firstName, String lastName, String id, String email) {
        for (User user : users) {
            if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName) && user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    public List<User> findUserByRole(String role) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(role)){
                return filteredUsers;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }
}
