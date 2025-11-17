package Models;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

public class LessonDatabase {
    private final String filePath;
    private final Map<String, Lesson> lessons = new HashMap<>();
    private final Gson gson;

    public LessonDatabase(String filePath) {
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
                
                // Parse each lesson manually
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String lessonId = entry.getKey();
                    JsonObject lessonJson = entry.getValue().getAsJsonObject();
                    
                    String title = lessonJson.get("title").getAsString();
                    String content = lessonJson.get("content").getAsString();
                    
                    // Parse resources array
                    List<String> resources = new ArrayList<>();
                    if (lessonJson.has("resources") && !lessonJson.get("resources").isJsonNull()) {
                        JsonArray resourcesArray = lessonJson.getAsJsonArray("resources");
                        for (JsonElement resourceElement : resourcesArray) {
                            resources.add(resourceElement.getAsString());
                        }
                    }
                    
                    Lesson lesson = new Lesson(lessonId, title, content, resources);
                    lessons.put(lessonId, lesson);
                }
            } else if (element.isJsonArray()) {
                // Handle empty array case []
                JsonArray jsonArray = element.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    System.out.println("Lesson database is empty.");
                }
            }

            System.out.println("Loaded " + lessons.size() + " lessons from database.");

        } catch (Exception e) {
            System.err.println("Failed to load LessonDatabase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void save() {
        try (Writer w = new FileWriter(filePath)) {
            gson.toJson(lessons, w);
        } catch (Exception e) {
            System.err.println("Failed to save LessonDatabase: " + e.getMessage());
        }
    }

    public void addLesson(Lesson lesson) {
        lessons.put(lesson.getLessonId(), lesson);
        save();
    }

    public Lesson getLessonById(String id) {
        return lessons.get(id);
    }

    public void updateLesson(Lesson lesson) {
        lessons.put(lesson.getLessonId(), lesson);
        save();
    }

    public void deleteLesson(String id) {
        lessons.remove(id);
        save();
    }

    public List<Lesson> getAllLessons() {
        return new ArrayList<>(lessons.values());
    }

    public List<Lesson> getLessonsByCourse(String courseId, List<String> lessonIds) {
        List<Lesson> result = new ArrayList<>();
        for (String id : lessonIds) {
            Lesson l = lessons.get(id);
            if (l != null) {
                result.add(l);
            }
        }
        return result;
    }

    public boolean lessonExists(String id) {
        return lessons.containsKey(id);
    }
}