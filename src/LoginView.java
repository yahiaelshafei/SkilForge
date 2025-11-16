import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(loginButton);
        panel.add(signupButton);

        add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = UserBackend.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            } else {
                JOptionPane.showMessageDialog(this, "Login successful!");

                dispose();

                if (user.getRole().equals("student")) {
                    new StudentDashboard().setVisible(true);
                } else {
                    new InstructorDashboard().setVisible(true);
                }
            }
        });

        signupButton.addActionListener(e -> {
            new SignupScreen().setVisible(true);
        });
    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}
