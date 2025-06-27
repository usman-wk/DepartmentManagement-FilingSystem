import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EnrollmentGUI {
    static class Course {
        String id;
        String name;
        int maxSeats;
        List<String> prerequisites;
        List<Student> enrolledStudents = new ArrayList<>();
        List<Student> waitlist = new ArrayList<>();

        Course(String id, String name, int maxSeats, List<String> prerequisites) {
            this.id = id;
            this.name = name;
            this.maxSeats = maxSeats;
            this.prerequisites = prerequisites;
        }

        boolean hasSeats() {
            return enrolledStudents.size() < maxSeats;
        }
    }

    static class Student {
        String id;
        String name;
        boolean loggedIn;
        Set<String> completedCourses;

        Student(String id, String name, boolean loggedIn, Set<String> completedCourses) {
            this.id = id;
            this.name = name;
            this.loggedIn = loggedIn;
            this.completedCourses = completedCourses;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class EnrollmentSystem {
        Map<String, Course> courses = new HashMap<>();
        Map<String, Student> students = new HashMap<>();
        boolean enrollmentWindowOpen = true;

        void addCourse(Course c) {
            courses.put(c.id, c);
        }

        void addStudent(Student s) {
            students.put(s.id, s);
        }

        String enroll(Student student, Course course) {
            if (!student.loggedIn)
                return "Student must be logged in.";
            if (!enrollmentWindowOpen)
                return "Enrollment window is closed.";

            List<String> missing = new ArrayList<>();
            for (String prereq : course.prerequisites) {
                if (!student.completedCourses.contains(prereq))
                    missing.add(prereq);
            }

            if (!missing.isEmpty())
                return "Missing prerequisites: " + String.join(", ", missing);

            if (course.hasSeats()) {
                course.enrolledStudents.add(student);
                return "Enrollment successful for " + student.name + " in " + course.name;
            } else {
                course.waitlist.add(student);
                return "Course is full. " + student.name + " added to waitlist. Position: " + course.waitlist.size();
            }
        }
    }

    public static void main(String[] args) {
        EnrollmentSystem system = new EnrollmentSystem();

        // Setup courses
        Course cs101 = new Course("CS101", "Intro to CS", 2, List.of());
        Course cs201 = new Course("CS201", "Data Structures", 1, List.of("CS101"));
        system.addCourse(cs101);
        system.addCourse(cs201);

        // Setup students
        Student alice = new Student("s1", "Alice", true, new HashSet<>(List.of("CS101")));
        Student bob = new Student("s2", "Bob", true, new HashSet<>());
        system.addStudent(alice);
        system.addStudent(bob);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Course Enrollment");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new GridLayout(6, 1));

            JComboBox<Student> studentCombo = new JComboBox<>(system.students.values().toArray(new Student[0]));
            JComboBox<Course> courseCombo = new JComboBox<>(system.courses.values().toArray(new Course[0]));

            JButton enrollButton = new JButton("Enroll");
            JTextArea resultArea = new JTextArea(5, 20);
            resultArea.setEditable(false);
            JScrollPane scroll = new JScrollPane(resultArea);

            enrollButton.addActionListener(e -> {
                Student selectedStudent = (Student) studentCombo.getSelectedItem();
                Course selectedCourse = (Course) courseCombo.getSelectedItem();
                if (selectedStudent != null && selectedCourse != null) {
                    String result = system.enroll(selectedStudent, selectedCourse);
                    resultArea.append(result + "\n");
                }
            });

            frame.add(new JLabel("Select Student:"));
            frame.add(studentCombo);
            frame.add(new JLabel("Select Course:"));
            frame.add(courseCombo);
            frame.add(enrollButton);
            frame.add(scroll);

            frame.setVisible(true);
        });
    }
}