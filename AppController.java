import javax.swing.JFrame;


public class AppController {

    private JFrame frame;
    private LoginView loginView;
    private SignUpView signUpView;

    public AppController(JFrame frame){
        this.frame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);

        loginView = new LoginView();
        signUpView = new SignUpView();

        loginView.getSignupButton().addActionListener(e -> showSignUp());
        signUpView.getBackButton().addActionListener(e -> showLogin());

        showLogin();



    }

    private void showLogin() {
        frame.setContentPane(loginView);
        frame.revalidate();
        frame.repaint();
        //frame.setVisible(true);
    }

    private void showSignUp() {
        frame.setContentPane(signUpView);
        frame.revalidate();
        frame.repaint();
    }


}
