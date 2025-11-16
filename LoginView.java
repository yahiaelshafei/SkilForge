import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JPanel{
    private JLabel title;
    private JButton loginButton;
    private JButton signUpButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView()
    {
        setLayout(null);

        title = new JLabel();
        title.setText("Welcome to Skillforge");
        title.setFont(new Font("Arial",Font.PLAIN,40));
        title.setBounds(200,10,400,50);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial" , Font.PLAIN, 20));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300,30));
        usernameField.setFont(new Font("Arial",Font.PLAIN,18));
        usernameLabel.setBounds(170,200,100,30);
        usernameField.setBounds(290,200,300,30);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial" , Font.PLAIN,20));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300,30));
        passwordField.setFont(new Font("Arial",Font.PLAIN,18));
        passwordLabel.setBounds(170,250,100,30);
        passwordField.setBounds(290,250,300,30);

        loginButton = new JButton("Login");
        loginButton.setBounds(220,330,100,30);

        signUpButton = new JButton("Sign up");
        signUpButton.setBounds(440,330,100,30);

        add(title);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(signUpButton);
    }


    public JButton getLoginButton()
    {
        return loginButton;
    }
    public JButton getSignupButton()
    {
        return signUpButton;
    }
    public String getUsername()
    {
        return usernameField.getText();
    }
    public String getPassword()
    {
        return new String(passwordField.getPassword());
    }

}
