public class UserController {
    private final JsonDatabaseManager db;

    public UserController(JsonDatabaseManager db) {
        this.db = db;
    }

    public User getUserById(String id) { return db.findUserById(id); }

    public boolean updateUser(String id, String newName, String newEmail) {
        User u = db.findUserById(id);
        if (u == null) return false;
        u.setUsername(newName);
        u.setEmail(newEmail);
        db.updateUser(u);
        return true;
    }
}
