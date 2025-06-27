package main;

import gui.LoginFrame;
import service.UserService;
import service.DepartmentService;
import service.CourseService;
import model.User;
import model.Department;
import model.Course;
import model.Role;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize services and sample data
        initializeDefaultData();
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
    private static void initializeDefaultData() {
        UserService userService = UserService.getInstance();
        DepartmentService departmentService = DepartmentService.getInstance();
        CourseService courseService = CourseService.getInstance();
        
        // Create default departments if they don't exist
        if (departmentService.getAllDepartments().isEmpty()) {
            departmentService.createDepartment(new Department(1, "Computer Science", "CS"));
            departmentService.createDepartment(new Department(2, "Electrical Engineering", "EE"));
            departmentService.createDepartment(new Department(3, "Business Administration", "BBA"));
            departmentService.createDepartment(new Department(4, "Information Technology", "IT"));
        }
        
        // Create default users if they don't exist
        if (userService.getAllUsers().isEmpty()) {
            // Admin user
            User admin = new User(1, "Administrator", "admin", "admin@depttrack.com", "admin123", Role.ADMIN);
            userService.addUser(admin);
            
            // HOD users
            User hodCS = new User(2, "Dr. Ahmed Khan", "hod_cs", "ahmed@depttrack.com", "hod123", Role.HOD);
            hodCS.setDepartmentId(1);
            userService.addUser(hodCS);
            
            User hodEE = new User(3, "Dr. Sarah Ali", "hod_ee", "sarah@depttrack.com", "hod123", Role.HOD);
            hodEE.setDepartmentId(2);
            userService.addUser(hodEE);
            
            // Student users
            User student1 = new User(4, "John Smith", "john_smith", "john@student.com", "student123", Role.STUDENT);
            student1.setDepartmentId(1);
            userService.addUser(student1);
            
            User student2 = new User(5, "Alice Johnson", "alice_j", "alice@student.com", "student123", Role.STUDENT);
            student2.setDepartmentId(2);
            userService.addUser(student2);
        }
        
        // Create default courses if they don't exist
        if (courseService.getAllCourses().isEmpty()) {
            courseService.createCourse(new Course(1, "Data Structures", "CS101", 1, 30));
            courseService.createCourse(new Course(2, "Object Oriented Programming", "CS102", 1, 25));
            courseService.createCourse(new Course(3, "Database Systems", "CS201", 1, 20));
            courseService.createCourse(new Course(4, "Circuit Analysis", "EE101", 2, 35));
            courseService.createCourse(new Course(5, "Digital Logic", "EE102", 2, 30));
            courseService.createCourse(new Course(6, "Business Management", "BBA101", 3, 40));
        }
    }
}