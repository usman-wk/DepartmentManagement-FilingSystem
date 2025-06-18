import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordResetAuthorization extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JButton submitButton;
    private JLabel messageLabel;

    public PasswordResetAuthorization() {
        setTitle("Password Reset Authorization");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        
        submitButton = new JButton("Submit");
        messageLabel = new JLabel("");

        // Add action listener to the button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePasswordReset();
            }
        });

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(emailLabel);
        add(emailField);
        add(submitButton);
        add(messageLabel);
    }

    private void handlePasswordReset() {
        String username = usernameField.getText();
        String email = emailField.getText();

        // Here you would typically check the username and email against a database
        // For demonstration, we'll just check if they are not empty
        if (username.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
        } else {
            // Simulate a successful password reset authorization
            messageLabel.setText("Authorization successful! Please check your email.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PasswordResetAuthorization frame = new PasswordResetAuthorization();
            frame.setVisible(true);
        });
    }
}
