package Models;

import java.util.*;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private List<String> lessons;
    private List<String> students;

    public Course(String courseId, String title, String description,
            String instructorId, List<String> lessons, List<String> students) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = lessons;
        this.students = students;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLessons(List<String> lessons) {
        this.lessons = lessons;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }
}
