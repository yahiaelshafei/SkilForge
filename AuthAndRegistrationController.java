import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class AuthAndRegistrationController {
    private Map<String, User> users;
     public AuthAndRegistrationController() {
        users = new HashMap<>();
        
}

     
     public boolean signup(String userId, String role, String username, String email, String password) {
    
    if (users.containsKey(userId)) {
        System.out.println("Error: User ID already exists.");
        return false;
    }

    
    if (!validateEmail(email)) {
        System.out.println("Error: Invalid email.");
        return false;
    }

    
    String passwordHash = hashPassword(password);

   
    User user;
    if (role.equalsIgnoreCase("student")) {
        user = new Student(userId, username, email, passwordHash, new ArrayList<>(), new HashMap<>());
    } else if (role.equalsIgnoreCase("instructor")) {
        user = new Instructor(userId, username, email, passwordHash, new ArrayList<>());
    } else {
        System.out.println("Error: Invalid role.");
        return false;
    }

    
    users.put(userId, user);
    System.out.println(role + " registered successfully!");
    return true;
}
     
     
     private boolean validateEmail(String email) {
    return email.contains("@") && email.contains(".");
}
     
    private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("SHA-256 algorithm not found.");
    }
} 
    
    
    public User login(String userId, String password) {
    
    User user = users.get(userId);
    if (user == null) {
        System.out.println("Error: User not found.");
        return null;
    }

    
    String passwordHash = hashPassword(password);

    
    if (!user.getPasswordHash().equals(passwordHash)) {
        System.out.println("Error: Incorrect password.");
        return null;
    }

   
    System.out.println("Login successful! Welcome " + user.getUsername());
    return user;
}
  public void logout(User user) {
    System.out.println(user.getUsername() + " logged out successfully.");
}  
  public Collection<User> getAllUsers() {
    return users.values();
}
     
}

}
