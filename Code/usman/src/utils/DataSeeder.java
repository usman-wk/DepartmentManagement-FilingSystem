package utils;

import model.Department;
import model.User;
import service.DepartmentService;
import service.UserService;

/** Populates services with demo data exactly once. */
public final class DataSeeder {

    private DataSeeder() {}   // utility class

    public static void seed(UserService uSvc, DepartmentService dSvc) {
        if (!uSvc.getAllUsers().isEmpty()) return; // already seeded

        // -- sample users
        uSvc.addUser(new User( 1, "Alice Khan",   "Student"));
        uSvc.addUser(new User( 2, "Bilal Ahmed",  "Student"));
        uSvc.addUser(new User( 3, "Cynthia Lee",  "Student"));

        uSvc.addUser(new User(101, "Dr. Rizvi",   "Staff"));
        uSvc.addUser(new User(102, "Prof. Malik", "Staff"));
        uSvc.addUser(new User(999, "System Admin","Admin"));

        // -- sample department
        dSvc.createDepartment(new Department(10, "Computer Science"));
    }
}
