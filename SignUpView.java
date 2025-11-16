import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignUpView extends JPanel{
    private JLabel title;
    private JButton backButton;
    private JButton signUpButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JComboBox roleComboBox;

    public SignUpView()
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
        usernameLabel.setBounds(150,200,100,30);
        usernameField.setBounds(320,200,300,30);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial" , Font.PLAIN,20));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300,30));
        passwordField.setFont(new Font("Arial",Font.PLAIN,18));
        passwordLabel.setBounds(150,250,100,30);
        passwordField.setBounds(320,250,300,30);

        JLabel repeatPasswordLabel = new JLabel("Repeat Password");
        repeatPasswordLabel.setFont(new Font("Arial" , Font.PLAIN,20));
        repeatPasswordField = new JPasswordField();
        repeatPasswordField.setPreferredSize(new Dimension(300,30));
        repeatPasswordField.setFont(new Font("Arial",Font.PLAIN,18));
        repeatPasswordLabel.setBounds(150,300,200,30);
        repeatPasswordField.setBounds(320,300,300,30);

        String[] roles = {"Student","Instructor"};
        JLabel roleLabel = new JLabel("Role ");
        roleLabel.setFont(new Font("Arial",Font.PLAIN,20));
        roleComboBox = new JComboBox(roles);
        roleComboBox.setFont(new Font("Arial",Font.PLAIN,18));
        roleLabel.setBounds(150,350,100,30);
        roleComboBox.setBounds(320,350,100,30);



        backButton = new JButton("Back");
        backButton.setBounds(220,430,100,30);

        signUpButton = new JButton("Sign up");
        signUpButton.setBounds(440,430,100,30);

        add(title);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(repeatPasswordLabel);
        add(repeatPasswordField);
        add(roleLabel);
        add(roleComboBox);
        add(backButton);
        add(signUpButton);
    }

}
