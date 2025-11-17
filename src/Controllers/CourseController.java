package Controllers;

import java.util.*;
import Models.*;

public class CourseController {
    private final CourseDatabase db;
    private final UserDatabase udb;

    public CourseController(CourseDatabase db, UserDatabase udb) {
        this.db = db;
        this.udb = udb;
    }

    public boolean addCourse(Course c) {
        try {
            db.addCourse(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateCourse(Course c) {
        try {
            db.addCourse(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean enrollStudent(String courseId, String studentId) {
        Course c = db.getCourseById(courseId);
        if (c == null)
            return false;

        User u = udb.findUserById(studentId);
        if (!(u instanceof Student))
            return false;
        Student s = (Student) u;

        if (!c.getStudents().contains(studentId)) {
            c.getStudents().add(studentId);
            db.updateCourse(c);
        }

        if (!s.getEnrolledCourses().contains(courseId)) {
            s.getEnrolledCourses().add(courseId);
            udb.updateUser(s);
        }

        return true;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(db.getAllCourses());
    }
}
