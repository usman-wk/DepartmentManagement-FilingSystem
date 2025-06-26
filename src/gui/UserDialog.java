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

public class UserDialog extends JDialog {
    private User user;
    private boolean confirmed = false;
    
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<Role> roleComboBox;
    private JComboBox<Department> departmentComboBox;
    private JCheckBox activeCheckBox;
    private JButton saveButton, cancelButton;
    
    private UserService userService;
    private DepartmentService departmentService;

    public UserDialog(JFrame parent, User user) {
        super(parent, user == null ? "Add User" : "Edit User", true);
        this.user = user;
        this.userService = UserService.getInstance();
        this.departmentService = DepartmentService.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        setDialogProperties();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        roleComboBox = new JComboBox<>(Role.values());
        departmentComboBox = new JComboBox<>();
        loadDepartments();
        
        activeCheckBox = new JCheckBox("Active", true);
        
        saveButton = new JButton("Save");
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
    }

    private void loadDepartments() {
        departmentComboBox.removeAllItems();
        departmentComboBox.addItem(null);
        
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
        mainPanel.add(new JLabel("Name:"), gbc);
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
        mainPanel.add(new JLabel(user == null ? "Password:" : "New Password (leave blank to keep current):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(roleComboBox, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(departmentComboBox, gbc);
        
        // Active
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(activeCheckBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUser();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadData() {
        if (user != null) {
            nameField.setText(user.getName());
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
            roleComboBox.setSelectedItem(user.getRole());
            activeCheckBox.setSelected(user.isActive());
            
            if (user.getDepartmentId() > 0) {
                Department dept = departmentService.getDepartmentById(user.getDepartmentId());
                departmentComboBox.setSelectedItem(dept);
            }
        }
    }

    private void saveUser() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        Role role = (Role) roleComboBox.getSelectedItem();
        Department department = (Department) departmentComboBox.getSelectedItem();
        boolean active = activeCheckBox.isSelected();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user == null && password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required for new users.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user == null) {
            // Create new user
            User newUser = new User(0, name, username, email, password, role);
            if (department != null) {
                newUser.setDepartmentId(department.getId());
            }
            newUser.setActive(active);
            
            if (userService.addUser(newUser)) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Update existing user
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
            user.setRole(role);
            if (department != null) {
                user.setDepartmentId(department.getId());
            } else {
                user.setDepartmentId(-1);
            }
            user.setActive(active);
            
            if (userService.updateUser(user)) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void setDialogProperties() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
}