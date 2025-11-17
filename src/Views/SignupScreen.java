package Views;

import javax.swing.*;
import java.awt.*;
import Controllers.*;
import Models.*;

public class SignupScreen extends JFrame {
    private final Auth auth;

    public SignupScreen(UserDatabase udb) {
        this.auth = new Auth(udb);

        setTitle("Signup");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        String[] roles = { "student", "instructor" };
        JComboBox<String> roleBox = new JComboBox<>(roles);
        JButton signupButton = new JButton("Create Account");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);

        panel.add(new JLabel(""));
        panel.add(signupButton);

        add(panel);

        signupButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = roleBox.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String userId = "user-" + System.currentTimeMillis();

            boolean success = auth.signup(userId, role, username, email, password);

            if (!success) {
                JOptionPane.showMessageDialog(this,
                        "Email or ID already exists / Invalid email!",
                        "Signup Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Account created successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
    }
}
