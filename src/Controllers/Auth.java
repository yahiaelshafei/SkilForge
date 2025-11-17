package Controllers;

import java.util.regex.*;
import Models.*;

public class Auth {
    private final UserDatabase db;
    private final Pattern emailPattern = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[A-Za-z]{2,}$");

    public Auth(UserDatabase db) {
        this.db = db;
    }

    public boolean signup(String userId, String role, String username, String email, String password) {
        if (db.findUserById(userId) != null) return false;
        if (db.emailExists(email)) return false;
        if (!emailPattern.matcher(email).matches()) return false;

        String hash = HashUtil.sha256(password);

        User u;
        if (role.equalsIgnoreCase("student")) {
            u = new Student(userId, username, email, hash, new java.util.ArrayList<>(), new java.util.HashMap<>());
        } else {
            u = new Instructor(userId, username, email, hash, new java.util.ArrayList<>());
        }

        db.addUser(u);
        db.save();
        return true;
    }

    public User login(String emailOrId, String password) {
        User u = db.findUserByEmail(emailOrId);
        if (u == null) u = db.findUserById(emailOrId);
        if (u == null) return null;

        if (!u.getPasswordHash().equals(HashUtil.sha256(password))) return null;
        return u;
    }
}
