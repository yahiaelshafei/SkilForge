import javax.swing.*;
import Controllers.*;
import Models.*;
import Views.*;

public class Main {
    public static void main(String[] args) {
        // Initialize databases
        UserDatabase udb = new UserDatabase("src/Data/users.json");
        CourseDatabase cdb = new CourseDatabase("src/Data/courses.json");
        LessonDatabase ldb = new LessonDatabase("src/Data/lessons.json");
        
        // Initialize controller
        CourseController courseController = new CourseController(cdb, udb, ldb);

        // Launch login screen
        SwingUtilities.invokeLater(() -> new LoginScreen(udb, courseController).setVisible(true));
    }
}