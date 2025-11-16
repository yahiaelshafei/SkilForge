import java.util.ArrayList;
import java.util.List;

public class CourseController {
    private final JsonDatabaseManager db;

    public CourseController(JsonDatabaseManager db) {
        this.db = db;
    }

    public boolean addCourse(Course c) {
        try {
            db.addCourse(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean enrollStudent(String courseId, String studentId) {
        Course c = db.getCourseById(courseId);
        if (c == null) return false;

        List<String> students = c.getStudents();
        if (!students.contains(studentId)) students.add(studentId);
        db.updateCourse(c);

        User u = db.findUserById(studentId);
        if (u instanceof Student) {
            Student s = (Student) u;
            s.getEnrolledCourses().add(courseId);
            db.updateUser(s);
        }
        return true;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(db.getAllCourses());
    }
}
