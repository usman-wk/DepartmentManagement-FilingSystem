package controller;

import model.Role;
import model.User;
import model.Department;
import service.DepartmentService;
import service.UserService;
import java.util.Scanner;

public class AppController {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final DepartmentService departmentService = new DepartmentService();

    public void start() {
        System.out.println("==== Department and User Management ====");
        createSampleRolesAndUsers();
        mainMenu();
    }

    private void createSampleRolesAndUsers() {
        Role admin = new Role(1, "Admin");
        Role hod = new Role(2, "HOD");

        //userService.addUser(new User(100, "Alice", admin));
        //userService.addUser(new User(101, "Bob", hod));
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n1. Create Department");
            System.out.println("2. Assign HOD");
            System.out.println("3. View Departments");
            System.out.println("4. View Users");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> createDepartment();
                case 2 -> assignHOD();
                case 3 -> viewDepartments();
                case 4 -> viewUsers();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next(); // discard invalid input
        }
        return scanner.nextInt();
    }

    private void createDepartment() {
        System.out.print("Enter Department ID: ");
        int id = getIntInput();
        scanner.nextLine(); // consume newline

        System.out.print("Enter Department Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Department Code: ");
        String code = scanner.nextLine();

        departmentService.createDepartment(new Department(id, name, code));
        System.out.println("Department created.");
    }

    private void assignHOD() {
        System.out.print("Enter Department ID: ");
        int deptId = getIntInput();
        Department dept = departmentService.getDepartmentById(deptId);
        if (dept == null) {
            System.out.println("Department not found.");
            return;
        }

        System.out.print("Enter User ID to assign as HOD: ");
        int userId = getIntInput();
        User user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        dept.setHod(user);
        System.out.println("HOD assigned successfully.");
    }

    private void viewDepartments() {
        System.out.println("\n--- All Departments ---");
        for (Department d : departmentService.getAllDepartments()) {
            System.out.println(d);
        }
    }

    private void viewUsers() {
        System.out.println("\n--- All Users ---");
        for (User u : userService.getAllUsers()) {
            System.out.println(u);
        }
    }
}
