package gui;

import model.User;
import model.Role;
import service.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton forgotPasswordButton;
    private UserService userService;

    public LoginFrame() {
        userService = UserService.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        forgotPasswordButton = new JButton("Forgot Password?");
        
        // Style buttons
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        signupButton.setBackground(new Color(40, 167, 69));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        
        forgotPasswordButton.setBackground(new Color(255, 193, 7));
        forgotPasswordButton.setForeground(Color.BLACK);
        forgotPasswordButton.setFocusPainted(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 58, 64));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🏛️ DeptTrack", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Department Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(173, 181, 189));
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(loginButton, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(signupButton, gbc);
        
        gbc.gridy = 4;
        mainPanel.add(forgotPasswordButton, gbc);
        
        // Demo credentials panel
        JPanel demoPanel = createDemoCredentialsPanel();
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(demoPanel, gbc);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createDemoCredentialsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Demo Credentials"));
        panel.setBackground(new Color(248, 249, 250));
        
        JLabel adminLabel = new JLabel("Admin: admin / admin123");
        JLabel hodLabel = new JLabel("HOD: hod_cs / hod123");
        JLabel studentLabel = new JLabel("Student: john_smith / student123");
        
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        adminLabel.setFont(font);
        hodLabel.setFont(font);
        studentLabel.setFont(font);
        
        panel.add(adminLabel);
        panel.add(hodLabel);
        panel.add(studentLabel);
        
        return panel;
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignupDialog();
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPasswordResetDialog();
            }
        });

        // Enter key support
        getRootPane().setDefaultButton(loginButton);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", 
                                        "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            // Login successful - open appropriate dashboard
            openDashboard(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", 
                                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboard(user).setVisible(true);
        });
    }

    private void openSignupDialog() {
        SignupDialog dialog = new SignupDialog(this);
        dialog.setVisible(true);
    }

    private void openPasswordResetDialog() {
        PasswordResetDialog dialog = new PasswordResetDialog(this);
        dialog.setVisible(true);
    }

    private void setFrameProperties() {
        setTitle("DeptTrack - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}