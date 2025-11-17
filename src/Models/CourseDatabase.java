package Models;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

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

            Map<String, Course> loaded = gson.fromJson(r,
                    new TypeToken<Map<String, Course>>() {
                    }.getType());

            if (loaded != null)
                courses.putAll(loaded);

            r.close();

        } catch (Exception e) {
            System.err.println("Failed to load CourseDatabase: " + e.getMessage());
        }
    }

    private void save() {
        try (Writer w = new FileWriter(filePath)) {
            gson.toJson(courses, w);
        } catch (Exception e) {
            System.err.println("Failed to save CourseDatabase: " + e.getMessage());
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
