import java.util.*;
public class CourseController {
    private Map<String ,Course> courses;
      public CourseController() {
        courses = new HashMap<>();
    }
      
      
     public boolean addCourse(Course course){
      if (courses.containsKey(course.getCourseId())) {
        System.out.println("Error: Course id already exists.");
        return false;
    }
    courses.put(course.getCourseId(), course);
    System.out.println("Course added successfully: " + course.getTitle());
    return true;
     } 
     
     
     public boolean editCourse(String courseId, String newTitle, String newDescription) {
    Course course = courses.get(courseId);
    if (course == null) {
        System.out.println("Error: Course not found.");
        return false;
    }
    course.setTitle(newTitle);
    course.setDescription(newDescription);
    System.out.println("Course updated successfully:" + course.getTitle());
    return true;
}
 public boolean removeCourse(String courseId) {
    if (!courses.containsKey(courseId)) {
        System.out.println("Error: Course not found.");
        return false;
    }
    courses.remove(courseId);
    System.out.println("Course removed successfully: " + courseId);
    return true;
}
  
 
 public boolean addLessonToCourse(String courseId, String lessonId) {
    Course course = courses.get(courseId);
    if (course == null) {
        System.out.println("Error: Course not found.");
        return false;
    }
    List<String> lessons = course.getLessons();
    if (lessons.contains(lessonId)) {
         System.out.println("Error: lesson already exists in course.");
        return false;
    }
    lessons.add(lessonId);
    course.setLessons(lessons);
    System.out.println("Lesson added to course: " + lessonId);
    return true;
}
 public boolean removeLessonFromCourse(String courseId, String lessonId) {
    Course course = courses.get(courseId);
    if (course == null) {
        System.out.println("Error: Course not found.");
        return false;
    }
    List<String> lessons = course.getLessons();
    if (!lessons.contains(lessonId)) {
        System.out.println("Error: Lesson not found in course.");
        return false;
    }
    lessons.remove(lessonId);
    course.setLessons(lessons);
    System.out.println("Lesson removed from course: " + lessonId);
    return true;
}
  public boolean enrollStudent(String courseId, String studentId) {
    Course course = courses.get(courseId);
    if (course == null) {
        System.out.println("Error: Course not found.");
        return false;
    }
    List<String> students = course.getStudents();
    if (students.contains(studentId)) {
        System.out.println("Student already enrolled.");
        return false;
    }
    students.add(studentId);
    course.setStudents(students);
    System.out.println("Student enrolled successfully: " + studentId);
    return true;
}
  public Course getCourse(String courseId) {
    return courses.get(courseId);
}
 public List<Course> getAllCourses() {
    return new ArrayList<>(courses.values());
}
 
     
}
