package Models;

import java.io.*;
import java.util.*;
import com.google.gson.*;

public class UserDatabase {
    private final String filePath;
    private final Map<String, User> users = new HashMap<>();
    private final Gson gson;

    public UserDatabase(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        load();
    }

    private void load() {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                save();
                return;
            }

            Reader r = new FileReader(f);
            
            // Read as JsonObject first
            JsonObject jsonObject = JsonParser.parseReader(r).getAsJsonObject();
            r.close();
            
            // Manually deserialize each user based on their role
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String userId = entry.getKey();
                JsonObject userJson = entry.getValue().getAsJsonObject();
                
                String role = userJson.get("role").getAsString();
                
                User user;
                if (role.equalsIgnoreCase("student")) {
                    // Deserialize as Student
                    String username = userJson.get("username").getAsString();
                    String email = userJson.get("email").getAsString();
                    String passwordHash = userJson.get("passwordHash").getAsString();
                    
                    // Parse enrolledCourses
                    List<String> enrolledCourses = new ArrayList<>();
                    if (userJson.has("enrolledCourses") && !userJson.get("enrolledCourses").isJsonNull()) {
                        JsonArray coursesArray = userJson.getAsJsonArray("enrolledCourses");
                        for (JsonElement courseElement : coursesArray) {
                            enrolledCourses.add(courseElement.getAsString());
                        }
                    }
                    
                    // Parse progress
                    Map<String, List<String>> progress = new HashMap<>();
                    if (userJson.has("progress") && !userJson.get("progress").isJsonNull()) {
                        JsonObject progressObj = userJson.getAsJsonObject("progress");
                        for (Map.Entry<String, JsonElement> progressEntry : progressObj.entrySet()) {
                            List<String> lessonIds = new ArrayList<>();
                            JsonArray lessonsArray = progressEntry.getValue().getAsJsonArray();
                            for (JsonElement lessonElement : lessonsArray) {
                                lessonIds.add(lessonElement.getAsString());
                            }
                            progress.put(progressEntry.getKey(), lessonIds);
                        }
                    }
                    
                    user = new Student(userId, username, email, passwordHash, enrolledCourses, progress);
                    
                } else if (role.equalsIgnoreCase("instructor")) {
                    // Deserialize as Instructor
                    String username = userJson.get("username").getAsString();
                    String email = userJson.get("email").getAsString();
                    String passwordHash = userJson.get("passwordHash").getAsString();
                    
                    // Parse createdCourses
                    List<String> createdCourses = new ArrayList<>();
                    if (userJson.has("createdCourses") && !userJson.get("createdCourses").isJsonNull()) {
                        JsonArray coursesArray = userJson.getAsJsonArray("createdCourses");
                        for (JsonElement courseElement : coursesArray) {
                            createdCourses.add(courseElement.getAsString());
                        }
                    }
                    
                    user = new Instructor(userId, username, email, passwordHash, createdCourses);
                    
                } else {
                    System.err.println("Unknown role for user: " + userId);
                    continue;
                }
                
                users.put(userId, user);
            }

            System.out.println("Loaded " + users.size() + " users from database.");

        } catch (Exception e) {
            System.err.println("Failed to load UserDatabase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        try (Writer w = new FileWriter(filePath)) {
            gson.toJson(users, w);
            System.out.println("Saved " + users.size() + " users to database.");
        } catch (Exception e) {
            System.err.println("Failed to save UserDatabase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addUser(User u) {
        users.put(u.getUserId(), u);
        save();
    }

    public User findUserById(String id) {
        return users.get(id);
    }

    public User findUserByEmail(String email) {
        for (User u : users.values()) {
            if (u.getEmail().equalsIgnoreCase(email))
                return u;
        }
        return null;
    }

    public boolean emailExists(String email) {
        return findUserByEmail(email) != null;
    }

    public void updateUser(User u) {
        users.put(u.getUserId(), u);
        save();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}