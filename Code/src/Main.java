import java.util.*;

public class Main {
    static final String USER_FILE = "users.txt";
static final String DEPT_FILE = "departments.txt";

static List<User> users = FileHandler.loadFromFile(USER_FILE, User::fromString);
static List<Department> departments = FileHandler.loadFromFile(DEPT_FILE, Department::fromString);



    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option;
        if (users.isEmpty() && departments.isEmpty()) {
    insertDefaultData();
    FileHandler.saveToFile(USER_FILE, users);
    FileHandler.saveToFile(DEPT_FILE, departments);
    System.out.println("Default data inserted successfully.");
}


        System.out.println("==== Department and User Management ====");

        do {
            System.out.println("\n1. Create Department");
            System.out.println("2. Assign HOD");
            System.out.println("3. View Departments");
            System.out.println("4. View Users");
            System.out.println("5. View My Department Info");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.print("Enter Department ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Department Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Department Code: ");
                    String code = sc.nextLine();
                    departments.add(new Department(id, name, code));
                    FileHandler.saveToFile(DEPT_FILE, departments);
                    System.out.println("Department created.");
                }
                case 2 -> {
                    System.out.print("Enter Department ID: ");
                    int deptId = sc.nextInt();
                    System.out.print("Enter User ID to assign as HOD: ");
                    int userId = sc.nextInt();

                    Department dept = findDepartment(deptId);
                    if (dept != null && findUser(userId) != null) {
                        dept.setHodId(userId);
                        FileHandler.saveToFile(DEPT_FILE, departments);
                        System.out.println("HOD assigned successfully.");
                    } else {
                        System.out.println("Invalid Department or User ID.");
                    }
                }
                case 3 -> {
                    System.out.println("\n--- Departments ---");
                    departments.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.println("\n--- Users ---");
                    users.forEach(System.out::println);
                }
                case 5 -> {
                    System.out.print("Enter your User ID: ");
                    int userId = sc.nextInt();
                    boolean found = false;
                    for (Department dept : departments) {
                        if (Objects.equals(dept.getHodId(), userId) || dept.hasStudent(userId)) {
                            System.out.println("Your Department Info:\n" + dept);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("No department info found for your ID.");
                    }
                }
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option.");
            }

        } while (option != 0);
    }

   private static void insertDefaultData() {
    users.clear();
    departments.clear();

    
    Department cs = new Department(1, "Computer Science", "CS");
    Department ee = new Department(2, "Electrical Engineering", "EE");


    users.add(new User(101, "Alice", "HOD", 1));
    users.add(new User(102, "Bob", "Student", 1));
    users.add(new User(103, "Charlie", "Student", 1));
    users.add(new User(201, "David", "HOD", 2));
    users.add(new User(202, "Eva", "Student", 2));

    // Assign HODs
    cs.setHodId(101);
    ee.setHodId(201);

    departments.add(cs);
    departments.add(ee);
}


    private static Department findDepartment(int id) {
        return departments.stream().filter(d -> d.getDeptId() == id).findFirst().orElse(null);
    }

    private static User findUser(int id) {
        return users.stream().filter(u -> u.getUserId() == id).findFirst().orElse(null);
    }
}
