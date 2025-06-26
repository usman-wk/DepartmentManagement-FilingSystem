package gui;

import model.User;
import model.Role;
import service.UserService;
import service.DepartmentService;
import service.CourseService;
import service.EnrollmentService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private JLabel welcomeLabel;
    
    private UserService userService;
    private DepartmentService departmentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    public MainDashboard(User user) {
        this.currentUser = user;
        this.userService = UserService.getInstance();
        this.departmentService = DepartmentService.getInstance();
        this.courseService = CourseService.getInstance();
        this.enrollmentService = EnrollmentService.getInstance();
        
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setFrameProperties();
    }

    private void initializeComponents() {
        welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getRole().getDisplayName() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        tabbedPane = new JTabbedPane();
        setupTabs();
    }

    private void setupTabs() {
        // Overview tab - available to all users
        tabbedPane.addTab("📊 Overview", createOverviewPanel());
        
        // Role-based tabs
        if (currentUser.getRole() == Role.ADMIN) {
            tabbedPane.addTab("👥 Users", new UserManagementPanel(currentUser));
            tabbedPane.addTab("🏢 Departments", new DepartmentManagementPanel(currentUser));
            tabbedPane.addTab("📚 Courses", new CourseManagementPanel(currentUser));
            tabbedPane.addTab("📝 Enrollments", new EnrollmentManagementPanel(currentUser));
            tabbedPane.addTab("📈 Reports", new ReportsPanel(currentUser));
        } else if (currentUser.getRole() == Role.HOD) {
            tabbedPane.addTab("🏢 My Department", new DepartmentManagementPanel(currentUser));
            tabbedPane.addTab("📚 Department Courses", new CourseManagementPanel(currentUser));
            tabbedPane.addTab("📝 Enrollments", new EnrollmentManagementPanel(currentUser));
            tabbedPane.addTab("📈 Reports", new ReportsPanel(currentUser));
        } else if (currentUser.getRole() == Role.STUDENT) {
            tabbedPane.addTab("📚 Available Courses", new CourseManagementPanel(currentUser));
            tabbedPane.addTab("📝 My Enrollments", new EnrollmentManagementPanel(currentUser));
            tabbedPane.addTab("👤 My Profile", new ProfilePanel(currentUser));
        }
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        // Create stat cards based on user role
        if (currentUser.getRole() == Role.ADMIN) {
            statsPanel.add(createStatCard("Total Users", String.valueOf(userService.getAllUsers().size()), "👥"));
            statsPanel.add(createStatCard("Total Departments", String.valueOf(departmentService.getAllDepartments().size()), "🏢"));
            statsPanel.add(createStatCard("Total Courses", String.valueOf(courseService.getAllCourses().size()), "📚"));
            statsPanel.add(createStatCard("Total Enrollments", String.valueOf(enrollmentService.getAllEnrollments().size()), "📝"));
        } else if (currentUser.getRole() == Role.HOD) {
            int deptCourses = courseService.getCoursesByDepartment(currentUser.getDepartmentId()).size();
            int deptUsers = userService.getUsersByDepartment(currentUser.getDepartmentId()).size();
            statsPanel.add(createStatCard("Department Users", String.valueOf(deptUsers), "👥"));
            statsPanel.add(createStatCard("Department Courses", String.valueOf(deptCourses), "📚"));
            statsPanel.add(createStatCard("Total Departments", String.valueOf(departmentService.getAllDepartments().size()), "🏢"));
            statsPanel.add(createStatCard("All Courses", String.valueOf(courseService.getAllCourses().size()), "📚"));
        } else {
            int myEnrollments = enrollmentService.getEnrollmentsByStudent(currentUser.getId()).size();
            statsPanel.add(createStatCard("My Enrollments", String.valueOf(myEnrollments), "📝"));
            statsPanel.add(createStatCard("Available Courses", String.valueOf(courseService.getAllCourses().size()), "📚"));
            statsPanel.add(createStatCard("Total Departments", String.valueOf(departmentService.getAllDepartments().size()), "🏢"));
            statsPanel.add(createStatCard("System Users", String.valueOf(userService.getAllUsers().size()), "👥"));
        }
        
        panel.add(statsPanel, BorderLayout.NORTH);
        
        // Welcome message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBorder(BorderFactory.createTitledBorder("Welcome"));
        JTextArea welcomeText = new JTextArea();
        welcomeText.setEditable(false);
        welcomeText.setOpaque(false);
        welcomeText.setText(getWelcomeMessage());
        welcomePanel.add(welcomeText);
        
        panel.add(welcomePanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createStatCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        return card;
    }

    private String getWelcomeMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to DeptTrack, ").append(currentUser.getName()).append("!\n\n");
        
        switch (currentUser.getRole()) {
            case ADMIN:
                sb.append("As an Administrator, you have full access to:\n");
                sb.append("• Manage all users and their roles\n");
                sb.append("• Create and manage departments\n");
                sb.append("• Oversee all courses and enrollments\n");
                sb.append("• Generate comprehensive reports\n");
                break;
            case HOD:
                sb.append("As a Head of Department, you can:\n");
                sb.append("• Manage your department information\n");
                sb.append("• Oversee courses in your department\n");
                sb.append("• Review student enrollments\n");
                sb.append("• Generate department reports\n");
                break;
            case STUDENT:
                sb.append("As a Student, you can:\n");
                sb.append("• Browse available courses\n");
                sb.append("• Enroll in courses\n");
                sb.append("• View your enrollment status\n");
                sb.append("• Update your profile information\n");
                break;
        }
        
        return sb.toString();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 58, 64));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> logout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Profile Menu
        JMenu profileMenu = new JMenu("Profile");
        JMenuItem viewProfileItem = new JMenuItem("View Profile");
        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        
        viewProfileItem.addActionListener(e -> showProfileDialog());
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        
        profileMenu.add(viewProfileItem);
        profileMenu.add(changePasswordItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(profileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }

    private void showProfileDialog() {
        ProfileDialog dialog = new ProfileDialog(this, currentUser);
        dialog.setVisible(true);
    }

    private void showChangePasswordDialog() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(this, currentUser);
        dialog.setVisible(true);
    }

    private void showAboutDialog() {
        String message = "DeptTrack - Department Management System\n\n" +
                        "Version 1.0\n" +
                        "A comprehensive solution for managing university departments,\n" +
                        "courses, users, and enrollments.\n\n" +
                        "Developed as part of Software Design and Architecture project.";
        
        JOptionPane.showMessageDialog(this, message, "About DeptTrack", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setFrameProperties() {
        setTitle("DeptTrack - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
}