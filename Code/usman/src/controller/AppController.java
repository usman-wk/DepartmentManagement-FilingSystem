package controller;

import model.User;
import model.Department;
import service.DepartmentService;
import service.UserService;
import java.util.Scanner;

public class AppController {
    private final Scanner            sc   = new Scanner(System.in);
    private final UserService        userService        = new UserService();
    private final DepartmentService  departmentService  = new DepartmentService();

    /* ------------ entry point ------------ */
    public void start() {
        System.out.println("==== Department & User Management ====");
        seedSampleData();
        mainMenu();
    }

    /* ------------ sample data ------------ */
    private void seedSampleData() {
        // students
        userService.addUser(new User( 1, "Alice Khan",  "Student"));
        userService.addUser(new User( 2, "Bilal Ahmed", "Student"));
        userService.addUser(new User( 3, "Cynthia Lee", "Student"));

        // staff / potential HODs
        userService.addUser(new User(101, "Dr. Rizvi",  "Staff"));
        userService.addUser(new User(102, "Prof. Malik","Staff"));

        // an initial department (no HOD yet)
        departmentService.createDepartment(new Department(10, "Computer Science"));
    }

    /* ------------ menu ------------ */
    private void mainMenu() {
        while (true) {
            System.out.println("""
                  \n1  Create Department
                  2  Assign HOD
                  3  View Departments
                  4  View Users
                  0  Exit""");
            System.out.print("Choose option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> createDepartment();
                case 2 -> assignHOD();
                case 3 -> viewDepartments();
                case 4 -> viewUsers();
                case 0 -> { System.out.println("Good-bye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ------------ helpers ------------ */
    private int getIntInput() {
        while (!sc.hasNextInt()) { System.out.print("Enter a number: "); sc.next(); }
        return sc.nextInt();
    }

    /* ------------ use-cases ------------ */
    private void createDepartment() {
        System.out.print("New Department ID: "); int id = getIntInput(); sc.nextLine();
        System.out.print("Department Name  : "); String name = sc.nextLine();

        if (departmentService.getDepartmentById(id)!=null) {
            System.out.println("ID already exists!");
            return;
        }
        departmentService.createDepartment(new Department(id, name));
        System.out.println("✓ Department created.");
    }

    private void assignHOD() {
        System.out.print("Department ID : "); int deptId = getIntInput();
        Department dept = departmentService.getDepartmentById(deptId);
        if (dept==null) { System.out.println("No such department."); return; }

        System.out.print("User ID to be HOD: "); int uid = getIntInput();
        User user = userService.getUserById(uid);
        if (user==null) { System.out.println("No such user."); return; }

        dept.setHod(user);
        System.out.println("✓ " + user.getName() + " is now HOD of " + dept.getName());
    }

    private void viewDepartments() {
        System.out.println("\n--- Departments ---");
        departmentService.getAllDepartments().forEach(System.out::println);
    }

    private void viewUsers() {
        System.out.println("\n--- Users ---");
        userService.getAllUsers().forEach(System.out::println);
    }
}
