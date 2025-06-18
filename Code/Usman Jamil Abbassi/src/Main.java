import java.util.ArrayList;
import java.util.List;

 class Student {
    private final int id;
    private final String name;
    private final List<Course> completedCourses;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.completedCourses = new ArrayList<>();
    }

    public void completeCourse(Course course) {
        completedCourses.add(course);
    }

    public boolean hasPrerequisite(Course course) {
        return completedCourses.containsAll(course.getPrerequisites());
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Course> getCompletedCourses() { return completedCourses; }
}

class Course {
    private int id;
    private String title;
    private int capacity;
    private List<Course> prerequisites;
    private List<Enrollment> enrollments;

    public Course(int id, String title, int capacity) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        this.prerequisites = new ArrayList<>();
        this.enrollments = new ArrayList<>();
    }

    public boolean hasSeatsAvailable() {
        return enrollments.size() < capacity;
    }

    public void addPrerequisite(Course course) {
        prerequisites.add(course);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public List<Course> getPrerequisites() { return prerequisites; }
    public List<Enrollment> getEnrollments() { return enrollments; }
}

class Enrollment {
    private final Student student;
    private final Course course;
    private String status; // "PENDING", "APPROVED", "REJECTED"

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = "PENDING";
    }

    public void approve() {
        status = "APPROVED";
    }

    public void reject() {
        status = "REJECTED";
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public String getStatus() { return status; }
}

class CourseManagementSystem {
    private final List<Course> courses = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();

    public String enrollStudent(Student student, Course course) {
        if (!student.hasPrerequisite(course)) {
            return "Enrollment failed: Missing prerequisites";
        }

        if (!course.hasSeatsAvailable()) {
            return "Enrollment failed: Course is full";
        }

        Enrollment enrollment = new Enrollment(student, course);
        course.getEnrollments().add(enrollment);
        return "Enrollment successful. Status: PENDING";
    }

    public void approveEnrollment(Student student, Course course) {
        for (Enrollment e : course.getEnrollments()) {
            if (e.getStudent().getId() == student.getId()) {
                e.approve();
                break;
            }
        }
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addStudent(Student student) {
        students.add(student);
    }
}

public class Main {
    public static void main(String[] args) {
        CourseManagementSystem cms = new CourseManagementSystem();

        // Create courses
        Course math101 = new Course(101, "Mathematics 101", 30);
        Course cs201 = new Course(201, "Computer Science 201", 20);
        cs201.addPrerequisite(math101);

        cms.addCourse(math101);
        cms.addCourse(cs201);

        // Create students
        Student alice = new Student(1, "Alice");
        alice.completeCourse(math101);

        Student bob = new Student(2, "Bob");

        cms.addStudent(alice);
        cms.addStudent(bob);

        // Test enrollments
        System.out.println("Alice: " + cms.enrollStudent(alice, cs201));
        System.out.println("Bob: " + cms.enrollStudent(bob, cs201));

        // Approve Alice's enrollment
        cms.approveEnrollment(alice, cs201);
        System.out.println("Alice's enrollment status: " +
                cs201.getEnrollments().get(0).getStatus());
    }
}