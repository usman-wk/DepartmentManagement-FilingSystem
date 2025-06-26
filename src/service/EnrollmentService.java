package service;

import model.Enrollment;
import model.Enrollment.EnrollmentStatus;
import utils.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class EnrollmentService {
    private static EnrollmentService instance;
    private static final String ENROLLMENT_FILE = "data/enrollments.txt";
    private List<Enrollment> enrollments;

    private EnrollmentService() {
        enrollments = FileHandler.loadFromFile(ENROLLMENT_FILE, Enrollment::fromString);
        if (enrollments == null) {
            enrollments = new ArrayList<>();
        }
    }

    public static EnrollmentService getInstance() {
        if (instance == null) {
            instance = new EnrollmentService();
        }
        return instance;
    }

    public boolean enrollStudent(int studentId, int courseId) {
        // Check if already enrolled
        if (enrollments.stream().anyMatch(e -> e.getStudentId() == studentId && 
                                              e.getCourseId() == courseId)) {
            return false;
        }
        
        // Generate new ID
        int newId = enrollments.stream().mapToInt(Enrollment::getId).max().orElse(0) + 1;
        Enrollment enrollment = new Enrollment(newId, studentId, courseId);
        
        enrollments.add(enrollment);
        saveEnrollments();
        return true;
    }

    public boolean updateEnrollmentStatus(int enrollmentId, EnrollmentStatus status, String remarks) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        if (enrollment != null) {
            enrollment.setStatus(status);
            enrollment.setRemarks(remarks);
            saveEnrollments();
            return true;
        }
        return false;
    }

    public Enrollment getEnrollmentById(int id) {
        return enrollments.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId() == studentId)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        return enrollments.stream()
                .filter(e -> e.getCourseId() == courseId)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollments.stream()
                .filter(e -> e.getStatus() == status)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private void saveEnrollments() {
        FileHandler.saveToFile(ENROLLMENT_FILE, enrollments);
    }
}