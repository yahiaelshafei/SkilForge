import java.util.*;
import java.io.*;
import java.io.*;
import org.json.*;// handles json objects and arrays
public class UserController {
     private Map<String, User> users; 

    public UserController() {
        users = new HashMap<>();
    } 
   
    
   public boolean addUser(User user) {
    if (users.containsKey(user.getUserId())) {
        System.out.println("Error: User ID already exists.");
        return false;
    }

    for (User u : users.values()) {
        if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
            System.out.println("Error: Email already exists.");
            return false;
        }
    }

    users.put(user.getUserId(), user);
    System.out.println(user.getRole() + " added successfully: " + user.getUsername());
    return true;
}
   
   public User getUserById(String userId) {
    return users.get(userId); 
}
  public boolean updateUser(String userId, String newUsername, String newEmail) {
    User user = users.get(userId);
    if (user == null) {
        System.out.println("Error: User not found.");
        return false;
    }

    
    for (User u : users.values()) {
        if (!u.getUserId().equals(userId) && u.getEmail().equalsIgnoreCase(newEmail)) {
            System.out.println("Error: Email already exists.");
            return false;
        }
    }

    
    user.setUsername(newUsername);
    user.setEmail(newEmail);

    System.out.println("User updated successfully: " + user.getUsername());
    return true;
} 
  
  public boolean removeUser(String userId) {
    User removed = users.remove(userId); 
    if (removed == null) {
        System.out.println("Error: User not found.");
        return false;
    }
    System.out.println("User removed successfully: " + removed.getUsername());
    return true;
}
    public Collection<User> getAllUsers() {
        return users.values();
    }
    
    
    public void saveUsersToFile(String filename) {
    JSONArray usersArray = new JSONArray();

    for (User user : users.values()) {
        JSONObject obj = new JSONObject();
        obj.put("userId", user.getUserId());
        obj.put("role", user.getRole());
        obj.put("username", user.getUsername());
        obj.put("email", user.getEmail());
        obj.put("passwordHash", user.getPasswordHash());

        
        if (user instanceof Student) {
            Student s = (Student) user;
            obj.put("enrolledCourses", s.getEnrolledCourses());
            obj.put("progress", s.getProgress());
        } else if (user instanceof Instructor) {
            Instructor i = (Instructor) user;
            obj.put("createdCourses", i.getCreatedCourses());
        }

        usersArray.put(obj);
    }

    try (FileWriter file = new FileWriter(filename)) {
        file.write(usersArray.toString(4)); //print with 4-space indentation
        System.out.println("Users saved to " + filename);
    } catch (IOException e) {
        System.out.println("Error saving users: " + e.getMessage());
    }
}
    public void loadUsersFromFile(String filename) {
    try {
        File file = new File(filename);
        if (!file.exists()) return; // file not found, nothing to load

        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        JSONArray usersArray = new JSONArray(content);

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject obj = usersArray.getJSONObject(i);
            String userId = obj.getString("userId");
            String role = obj.getString("role");
            String username = obj.getString("username");
            String email = obj.getString("email");
            String passwordHash = obj.getString("passwordHash");

            if (role.equalsIgnoreCase("student")) {
                List<String> enrolledCourses = new ArrayList<>();
                Map<String, List<String>> progress = new HashMap<>();

                JSONArray ec = obj.getJSONArray("enrolledCourses");
                for (int j = 0; j < ec.length(); j++) enrolledCourses.add(ec.getString(j));

                JSONObject prog = obj.getJSONObject("progress");
                for (String key : prog.keySet()) {
                    JSONArray arr = prog.getJSONArray(key);
                    List<String> lessons = new ArrayList<>();
                    for (int k = 0; k < arr.length(); k++) lessons.add(arr.getString(k));
                    progress.put(key, lessons);
                }

                Student s = new Student(userId, username, email, passwordHash, enrolledCourses, progress);
                users.put(userId, s);

            } else if (role.equalsIgnoreCase("instructor")) {
                List<String> createdCourses = new ArrayList<>();
                JSONArray cc = obj.getJSONArray("createdCourses");
                for (int j = 0; j < cc.length(); j++) createdCourses.add(cc.getString(j));

                Instructor instructor = new Instructor(userId, username, email, passwordHash, createdCourses);
                users.put(userId, instructor);
            }
        }

        System.out.println("Users loaded from " + filename);

    } catch (Exception e) {
        System.out.println("Error loading users: " + e.getMessage());
    }
}

    
}