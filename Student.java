import java.util.List;
import java.util.Map;

public class Student extends User {
    private List<String> enrolledCourses;
    private Map<String, List<String>> progress;

    public Student(String userId, String username, String email, String passwordHash,
                   List<String> enrolledCourses, Map<String, List<String>> progress) {
        super(userId, "student", username, email, passwordHash);
        this.enrolledCourses = enrolledCourses;
        this.progress = progress;
    }

    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public Map<String, List<String>> getProgress() { return progress; }

    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }
    public void setProgress(Map<String, List<String>> progress) { this.progress = progress; }
}
