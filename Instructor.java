import java.util.List;

public class Instructor extends User {
    private List<String> createdCourses;

    public Instructor(String userId, String username, String email, String passwordHash,
                      List<String> createdCourses) {
        super(userId, "instructor", username, email, passwordHash);
        this.createdCourses = createdCourses;
    }

    public List<String> getCreatedCourses() { return createdCourses; }
    public void setCreatedCourses(List<String> list) { this.createdCourses = list; }
}
