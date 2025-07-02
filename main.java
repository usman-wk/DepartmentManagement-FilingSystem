import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Entry point of the Assign Course application implementing MVC.
 */
public class AssignCourseApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Model model = new Model();
            View view = new View();
            Controller controller = new Controller(model, view);
            view.setVisible(true);
        });
    }
}

/**
 * The Model class holds data and business logic.
 */
class Model {
    // Sample data
    private final List<String> terms;
    private final Map<String, List<Course>> coursesByTerm;
    private final Map<String, List<Teacher>> teachersByTerm;
    private final Map<Course, Teacher> courseAssignments;

    public Model() {
        terms = Arrays.asList("Spring 2024", "Summer 2024", "Fall 2024");

        // Courses available per term
        coursesByTerm = new HashMap<>();
        coursesByTerm.put("Spring 2024", Arrays.asList(
                new Course("CS101", "Introduction to Computer Science"),
                new Course("MATH201", "Calculus I"),
                new Course("ENG105", "English")
        ));
        coursesByTerm.put("Summer 2024", Arrays.asList(
                new Course("CS102", "Data Structures"),
                new Course("PHYS101", "Physics")
        ));
        coursesByTerm.put("Fall 2024", Arrays.asList(
                new Course("CS201", "AI"),
                new Course("SPM101", "SPM"),
                new Course("SDA202", "SDA")
        ));

        // Eligible teachers per term
        teachersByTerm = new HashMap<>();
        teachersByTerm.put("Spring 2024", Arrays.asList(
                new Teacher("BUSHRA MUSHTAQ"),
                new Teacher("GHANWA BATOOL")
        ));
        teachersByTerm.put("Summer 2024", Arrays.asList(
                new Teacher("SONIA GHAZAL"),
                new Teacher("MOHSIN ABBAS")
        ));
        teachersByTerm.put("Fall 2024", Arrays.asList(
                new Teacher("MUkHTYAR ZAMIN"),
                new Teacher("IHSAN KHAN")
        ));

        courseAssignments = new HashMap<>();
    }

    public List<String> getTerms() {
        return terms;
    }

    public List<Course> getCoursesForTerm(String term) {
        return coursesByTerm.getOrDefault(term, Collections.emptyList());
    }

    public List<Teacher> getTeachersForTerm(String term) {
        return teachersByTerm.getOrDefault(term, Collections.emptyList());
    }

    public boolean assignCourseToTeacher(Course course, Teacher teacher) {
        if (courseAssignments.containsKey(course)) {
            // Already assigned
            return false;
        }
        courseAssignments.put(course, teacher);
        return true;
    }

    public Teacher getAssignedTeacher(Course course) {
        return courseAssignments.get(course);
    }

    public Map<Course, Teacher> getAllAssignments() {
        return Collections.unmodifiableMap(courseAssignments);
    }
}

/**
 * Represents a Course entity.
 */
class Course {
    private final String code;
    private final String name;

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

/**
 * Represents a Teacher entity.
 */
class Teacher {
    private final String name;

    public Teacher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

/**
 * The View class builds and manages the GUI.
 */
class View extends JFrame {
    JComboBox<String> termComboBox;
    JComboBox<Course> courseComboBox;
    JComboBox<Teacher> teacherComboBox;
    JButton assignButton;
    JTextArea assignmentsTextArea;

    public View() {
        setTitle("Course Assignment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));

        // Top panel: selections
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Assign Course to Teacher"));
        topPanel.setBackground(new Color(250, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // Term label and combo box
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Select Term:"), gbc);
        termComboBox = new JComboBox<>();
        termComboBox.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        topPanel.add(termComboBox, gbc);

        // Course label and combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Available Courses:"), gbc);
        courseComboBox = new JComboBox<>();
        courseComboBox.setPreferredSize(new Dimension(300, 25));
        gbc.gridx = 1;
        topPanel.add(courseComboBox, gbc);

        // Teacher label and combo box
        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Eligible Teachers:"), gbc);
        teacherComboBox = new JComboBox<>();
        teacherComboBox.setPreferredSize(new Dimension(300, 25));
        gbc.gridx = 1;
        topPanel.add(teacherComboBox, gbc);

        // Assign button
        assignButton = new JButton("Assign Course");
        assignButton.setPreferredSize(new Dimension(150, 30));
        assignButton.setEnabled(false); // Disabled initially
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        topPanel.add(assignButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Bottom panel: assignments display
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Course Assignments"));
        bottomPanel.setBackground(new Color(250, 250, 255));

        assignmentsTextArea = new JTextArea();
        assignmentsTextArea.setEditable(false);
        assignmentsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        assignmentsTextArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(assignmentsTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.CENTER);
    }
}

/**
 * The Controller connects the Model and the View, handling logic and user interaction.
 */
class Controller {
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        // Populate term combo box
        for (String term : model.getTerms()) {
            view.termComboBox.addItem(term);
        }

        // Listeners
        view.termComboBox.addActionListener(e -> termSelected());
        view.courseComboBox.addActionListener(e -> enableAssignButtonIfReady());
        view.teacherComboBox.addActionListener(e -> enableAssignButtonIfReady());
        view.assignButton.addActionListener(e -> assignCourse());

        // Select first term by default
        if (view.termComboBox.getItemCount() > 0) {
            view.termComboBox.setSelectedIndex(0);
            termSelected();
        }
    }

    private void termSelected() {
        String selectedTerm = (String) view.termComboBox.getSelectedItem();
        if (selectedTerm == null) return;

        // Update courses for term
        view.courseComboBox.removeAllItems();
        List<Course> courses = model.getCoursesForTerm(selectedTerm);
        for (Course course : courses) {
            view.courseComboBox.addItem(course);
        }

        // Disable courseComboBox if no courses
        view.courseComboBox.setEnabled(!courses.isEmpty());

        // Update teachers for term
        view.teacherComboBox.removeAllItems();
        List<Teacher> teachers = model.getTeachersForTerm(selectedTerm);
        for (Teacher teacher : teachers) {
            view.teacherComboBox.addItem(teacher);
        }
        view.teacherComboBox.setEnabled(!teachers.isEmpty());

        // Reset assign button
        view.assignButton.setEnabled(false);

        // Show assignments for this term
        updateAssignmentsDisplay(selectedTerm);
    }

    private void enableAssignButtonIfReady() {
        boolean ready = (view.courseComboBox.getSelectedItem() != null) &&
                        (view.teacherComboBox.getSelectedItem() != null);
        view.assignButton.setEnabled(ready);
    }

    private void assignCourse() {
        Course selectedCourse = (Course) view.courseComboBox.getSelectedItem();
        Teacher selectedTeacher = (Teacher) view.teacherComboBox.getSelectedItem();
        if (selectedCourse == null || selectedTeacher == null) {
            showError("Please select both a course and a teacher to proceed.");
            return;
        }

        boolean assigned = model.assignCourseToTeacher(selectedCourse, selectedTeacher);
        if (!assigned) {
            showError("This course has already been assigned to teacher: " +
                    model.getAssignedTeacher(selectedCourse));
            return;
        }

        showConfirmation("Course '" + selectedCourse + "' successfully assigned to teacher '" + selectedTeacher + "'.");

        // Refresh assignments view for current term
        String selectedTerm = (String) view.termComboBox.getSelectedItem();
        if (selectedTerm != null) {
            updateAssignmentsDisplay(selectedTerm);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showConfirmation(String message) {
        JOptionPane.showMessageDialog(view, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateAssignmentsDisplay(String term) {
        List<Course> courses = model.getCoursesForTerm(term);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %-40s %-30s%n", "Course Code", "Course Name", "Assigned Teacher"));
        sb.append("--------------------------------------------------------------------------------\n");
        for (Course course : courses) {
            Teacher teacher = model.getAssignedTeacher(course);
            String teacherName = teacher == null ? "(Not Assigned)" : teacher.getName();
            sb.append(String.format("%-20s %-40s %-30s%n",
                    course.getCode(), course.getName(), teacherName));
        }
        view.assignmentsTextArea.setText(sb.toString());
    }
}