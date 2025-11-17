package Models;

import java.io.*;
import java.util.*;
import com.google.gson.*;

public class CourseDatabase {
    private final String filePath;
    private final Map<String, Course> courses = new HashMap<>();
    private final Gson gson;

    public CourseDatabase(String filePath) {
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
            JsonElement element = JsonParser.parseReader(r);
            r.close();
            
            // Check if it's an object or array
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                
                // Parse each course manually
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String courseId = entry.getKey();
                    JsonObject courseJson = entry.getValue().getAsJsonObject();
                    
                    String title = courseJson.get("title").getAsString();
                    String description = courseJson.get("description").getAsString();
                    String instructorId = courseJson.get("instructorId").getAsString();
                    
                    // Parse lessons array
                    List<String> lessons = new ArrayList<>();
                    if (courseJson.has("lessons") && !courseJson.get("lessons").isJsonNull()) {
                        JsonArray lessonsArray = courseJson.getAsJsonArray("lessons");
                        for (JsonElement lessonElement : lessonsArray) {
                            lessons.add(lessonElement.getAsString());
                        }
                    }
                    
                    // Parse students array
                    List<String> students = new ArrayList<>();
                    if (courseJson.has("students") && !courseJson.get("students").isJsonNull()) {
                        JsonArray studentsArray = courseJson.getAsJsonArray("students");
                        for (JsonElement studentElement : studentsArray) {
                            students.add(studentElement.getAsString());
                        }
                    }
                    
                    Course course = new Course(courseId, title, description, instructorId, lessons, students);
                    courses.put(courseId, course);
                }
            } else if (element.isJsonArray()) {
                // Handle empty array case []
                JsonArray jsonArray = element.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    // Empty database, nothing to load
                    System.out.println("Course database is empty.");
                }
            }

            System.out.println("Loaded " + courses.size() + " courses from database.");

        } catch (Exception e) {
            System.err.println("Failed to load CourseDatabase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void save() {
        try (Writer w = new FileWriter(filePath)) {
            gson.toJson(courses, w);
            System.out.println("Saved " + courses.size() + " courses to database.");
        } catch (Exception e) {
            System.err.println("Failed to save CourseDatabase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addCourse(Course c) {
        courses.put(c.getCourseId(), c);
        save();
    }

    public Course getCourseById(String id) {
        return courses.get(id);
    }

    public void updateCourse(Course c) {
        courses.put(c.getCourseId(), c);
        save();
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public boolean courseExists(String id) {
        return courses.containsKey(id);
    }
}