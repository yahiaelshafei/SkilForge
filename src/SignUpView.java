import javax.swing.*;
import java.awt.*;

public class SignupScreen extends JFrame {

    public SignupScreen() {
        setTitle("Signup");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        String[] roles = {"student", "instructor"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        JButton signupButton = new JButton("Create Account");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);

        panel.add(new JLabel(""));
        panel.add(signupButton);

        add(panel);

        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleBox.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            boolean success = UserBackend.addUser(new User(username, password, role));

            if (!success) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            } else {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                dispose();
            }
        });
    }
}
