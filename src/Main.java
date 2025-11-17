import javax.swing.*;

import Controllers.*;
import Models.*;
import Views.*;

public class Main {
    public static void main(String[] args) {
        UserDatabase udb = new UserDatabase("src/Data/users.json");
        CourseDatabase cdb = new CourseDatabase("src/Data/courses.json");
        CourseController courseController = new CourseController(cdb, udb);

        SwingUtilities.invokeLater(() -> new LoginScreen(udb, courseController).setVisible(true));
    }
}
