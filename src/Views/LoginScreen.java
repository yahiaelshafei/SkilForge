package Views;

import javax.swing.*;
import java.awt.*;

import Controllers.*;
import Models.*;

public class LoginScreen extends JFrame {
    private final Auth auth;
    private final CourseController courseController;

    public LoginScreen(UserDatabase udb, CourseController courseController) {
        this.auth = new Auth(udb);
        this.courseController = courseController;

        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        panel.add(new JLabel("Email or ID:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(loginButton);
        panel.add(signupButton);

        add(panel);

        loginButton.addActionListener(e -> {
            String input = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = auth.login(input, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid email/ID or password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Login successful!",
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();

                if (user.getRole().equalsIgnoreCase("student")) {
                    Student student = (Student) user;
                    new StudentDashboard(student, courseController).setVisible(true);
                } else if (user.getRole().equalsIgnoreCase("instructor")) {
                    Instructor instructor = (Instructor) user;
                    new InstructorDashboard(instructor, courseController).setVisible(true);
                }
            }
        });

        signupButton.addActionListener(e -> {
            new SignupScreen(udb).setVisible(true);
        });
    }
}
