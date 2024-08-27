package ku.cs.models.collections;

import ku.cs.models.User;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;

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

    public User findUserByRole(String role) {
        for (User user : users) {
            if (user.getRole().equals(role)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
