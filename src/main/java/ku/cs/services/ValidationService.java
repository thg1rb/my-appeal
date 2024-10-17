package ku.cs.services;

public class ValidationService {
    public boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches("^[^\\u0E00-\\u0E7F]+$");
    }
    public boolean validateUsername(String username) {
        return username.length() >= 6 && username.matches("^[^\\u0E00-\\u0E7F]+$");
    }
}
