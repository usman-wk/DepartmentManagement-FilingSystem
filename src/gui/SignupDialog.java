package gui;

import model.User;
import model.Role;
import model.Department;
import service.UserService;
import service.DepartmentService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SignupDialog extends JDialog {
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<Role> roleComboBox;
    private JComboBox<Department> departmentComboBox;
    private JButton signupButton;
    private JButton cancelButton;
    
    private UserService userService;
    private DepartmentService departmentService;

    public SignupDialog(JFrame parent) {
        super(parent, "Sign Up - DeptTrack", true);
        userService = UserService.getInstance();
        departmentService = DepartmentService.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDialogProperties();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        
        roleComboBox = new JComboBox<>(Role.values());
        roleComboBox.setSelectedIndex(-1);
        
        departmentComboBox = new JComboBox<>();
        loadDepartments();
        
        signupButton = new JButton("Create Account");
        signupButton.setBackground(new Color(40, 167, 69));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
    }

    private void loadDepartments() {
        departmentComboBox.removeAllItems();
        departmentComboBox.addItem(null); // No department option
        
        List<Department> departments = departmentService.getAllDepartments();
        for (Department dept : departments) {
            departmentComboBox.addItem(dept);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(nameField, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(emailField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(confirmPasswordField, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(roleComboBox, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(departmentComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSignup();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        roleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Role selectedRole = (Role) roleComboBox.getSelectedItem();
                departmentComboBox.setEnabled(selectedRole != Role.ADMIN);
            }
        });
    }

    private void performSignup() {
        // Validate input
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        Role role = (Role) roleComboBox.getSelectedItem();
        Department department = (Department) departmentComboBox.getSelectedItem();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || 
            password.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user
        User newUser = new User(0, name, username, email, password, role);
        if (department != null) {
            newUser.setDepartmentId(department.getId());
        }

        if (userService.addUser(newUser)) {
            JOptionPane.showMessageDialog(this, "Account created successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", 
                                        "Signup Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDialogProperties() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
}