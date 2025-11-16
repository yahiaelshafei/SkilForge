import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        SignUpView signUpView = new SignUpView();


        JFrame frame = new JFrame("Skillforge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(signUpView);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
