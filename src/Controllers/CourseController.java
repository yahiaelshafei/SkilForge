package Controllers;

import java.util.*;
import Models.*;

public class CourseController {
    private final CourseDatabase courseDb;
    private final UserDatabase userDb;
    private final LessonDatabase lessonDb;

    public CourseController(CourseDatabase courseDb, UserDatabase userDb, LessonDatabase lessonDb) {
        this.courseDb = courseDb;
        this.userDb = userDb;
        this.lessonDb = lessonDb;
    }

    // Course Management
    public boolean addCourse(Course c) {
        try {
            // Check if course already exists
            if (courseDb.courseExists(c.getCourseId())) {
                courseDb.updateCourse(c);
            } else {
                courseDb.addCourse(c);
            }
            
            // Update instructor's createdCourses list (avoid duplicates)
            User instructor = userDb.findUserById(c.getInstructorId());
            if (instructor instanceof Instructor) {
                Instructor inst = (Instructor) instructor;
                if (!inst.getCreatedCourses().contains(c.getCourseId())) {
                    inst.getCreatedCourses().add(c.getCourseId());
                    userDb.updateUser(inst);
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Course c) {
        try {
            courseDb.updateCourse(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Course getCourseById(String courseId) {
        return courseDb.getCourseById(courseId);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courseDb.getAllCourses());
    }

    public boolean deleteCourse(String courseId) {
        try {
            Course c = courseDb.getCourseById(courseId);
            if (c == null) return false;

            // Delete all lessons in this course
            for (String lessonId : c.getLessons()) {
                lessonDb.deleteLesson(lessonId);
            }

            // Remove from instructor's created courses
            User instructor = userDb.findUserById(c.getInstructorId());
            if (instructor instanceof Instructor) {
                ((Instructor) instructor).getCreatedCourses().remove(courseId);
                userDb.updateUser(instructor);
            }

            // Remove from students' enrolled courses
            for (String studentId : c.getStudents()) {
                User student = userDb.findUserById(studentId);
                if (student instanceof Student) {
                    ((Student) student).getEnrolledCourses().remove(courseId);
                    ((Student) student).getProgress().remove(courseId);
                    userDb.updateUser(student);
                }
            }

            courseDb.updateCourse(c); // This will effectively remove it
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Student Enrollment
    public boolean enrollStudent(String courseId, String studentId) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null)
            return false;

        User u = userDb.findUserById(studentId);
        if (!(u instanceof Student))
            return false;
        Student s = (Student) u;

        boolean updated = false;

        // Add student to course
        if (!c.getStudents().contains(studentId)) {
            c.getStudents().add(studentId);
            courseDb.updateCourse(c);
            updated = true;
        }

        // Add course to student's enrolled courses
        if (!s.getEnrolledCourses().contains(courseId)) {
            s.getEnrolledCourses().add(courseId);
            s.getProgress().put(courseId, new ArrayList<>());
            userDb.updateUser(s);
            updated = true;
            System.out.println("Student " + studentId + " enrolled in course " + courseId);
        }

        return updated;
    }

    public List<Student> getEnrolledStudents(String courseId) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return new ArrayList<>();

        List<Student> students = new ArrayList<>();
        for (String studentId : c.getStudents()) {
            User u = userDb.findUserById(studentId);
            if (u instanceof Student) {
                students.add((Student) u);
            }
        }
        return students;
    }

    // Lesson Management
    public boolean addLesson(String courseId, Lesson lesson) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return false;

        lessonDb.addLesson(lesson);
        c.getLessons().add(lesson.getLessonId());
        courseDb.updateCourse(c);
        return true;
    }

    public boolean updateLesson(Lesson lesson) {
        try {
            lessonDb.updateLesson(lesson);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteLesson(String courseId, String lessonId) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return false;

        c.getLessons().remove(lessonId);
        courseDb.updateCourse(c);
        lessonDb.deleteLesson(lessonId);
        return true;
    }

    public Lesson getLessonById(String lessonId) {
        return lessonDb.getLessonById(lessonId);
    }

    public List<Lesson> getCourseLessons(String courseId) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return new ArrayList<>();
        return lessonDb.getLessonsByCourse(courseId, c.getLessons());
    }

    // Progress Tracking
    public boolean markLessonComplete(String studentId, String courseId, String lessonId) {
        User u = userDb.findUserById(studentId);
        if (!(u instanceof Student)) return false;

        Student s = (Student) u;
        List<String> progress = s.getProgress().getOrDefault(courseId, new ArrayList<>());
        
        if (!progress.contains(lessonId)) {
            progress.add(lessonId);
            s.getProgress().put(courseId, progress);
            userDb.updateUser(s);
            return true;
        }
        return false;
    }

    public boolean isLessonCompleted(String studentId, String courseId, String lessonId) {
        User u = userDb.findUserById(studentId);
        if (!(u instanceof Student)) return false;

        Student s = (Student) u;
        List<String> progress = s.getProgress().getOrDefault(courseId, new ArrayList<>());
        return progress.contains(lessonId);
    }

    public int getCompletedLessonsCount(String studentId, String courseId) {
        User u = userDb.findUserById(studentId);
        if (!(u instanceof Student)) return 0;

        Student s = (Student) u;
        List<String> progress = s.getProgress().getOrDefault(courseId, new ArrayList<>());
        return progress.size();
    }

    // Helper method to get user by ID
    public User getUserById(String userId) {
        return userDb.findUserById(userId);
    }
}