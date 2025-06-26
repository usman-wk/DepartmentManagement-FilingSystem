package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Enrollment implements Serializable {
    private int id;
    private int studentId;
    private int courseId;
    private EnrollmentStatus status;
    private LocalDateTime enrollmentDate;
    private String remarks;

    public Enrollment() {}

    public Enrollment(int id, int studentId, int courseId) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = EnrollmentStatus.PENDING;
        this.enrollmentDate = LocalDateTime.now();
        this.remarks = "";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public enum EnrollmentStatus {
        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        COMPLETED("Completed");

        private final String displayName;

        EnrollmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    @Override
    public String toString() {
        return id + "," + studentId + "," + courseId + "," + status + "," + 
               enrollmentDate + "," + (remarks != null ? remarks : "");
    }

    public static Enrollment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 6) {
            Enrollment enrollment = new Enrollment();
            enrollment.setId(Integer.parseInt(parts[0]));
            enrollment.setStudentId(Integer.parseInt(parts[1]));
            enrollment.setCourseId(Integer.parseInt(parts[2]));
            enrollment.setStatus(EnrollmentStatus.valueOf(parts[3]));
            enrollment.setEnrollmentDate(LocalDateTime.parse(parts[4]));
            enrollment.setRemarks(parts[5]);
            return enrollment;
        }
        return null;
    }
}