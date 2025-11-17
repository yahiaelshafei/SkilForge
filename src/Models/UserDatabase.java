package Models;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

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
            Map<String, User> loadedUsers = gson.fromJson(r,
                    new TypeToken<Map<String, User>>() {
                    }.getType());

            if (loadedUsers != null)
                users.putAll(loadedUsers);

            r.close();

        } catch (Exception e) {
            System.err.println("Failed to load UserDatabase: " + e.getMessage());
        }
    }

    public void save() {
        try (Writer w = new FileWriter(filePath)) {
            gson.toJson(users, w);
        } catch (Exception e) {
            System.err.println("Failed to save UserDatabase: " + e.getMessage());
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
