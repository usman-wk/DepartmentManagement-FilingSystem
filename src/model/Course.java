package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private int id;
    private String title;
    private String code;
    private int departmentId;
    private int capacity;
    private String description;
    private List<Integer> prerequisites;
    private boolean isActive;

    public Course() {
        this.prerequisites = new ArrayList<>();
    }

    public Course(int id, String title, String code, int departmentId, int capacity) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.departmentId = departmentId;
        this.capacity = capacity;
        this.description = "";
        this.prerequisites = new ArrayList<>();
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Integer> getPrerequisites() { return prerequisites; }
    public void setPrerequisites(List<Integer> prerequisites) { this.prerequisites = prerequisites; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public void addPrerequisite(int courseId) {
        if (!prerequisites.contains(courseId)) {
            prerequisites.add(courseId);
        }
    }

    public void removePrerequisite(int courseId) {
        prerequisites.remove(Integer.valueOf(courseId));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",")
          .append(title).append(",")
          .append(code).append(",")
          .append(departmentId).append(",")
          .append(capacity).append(",")
          .append(description != null ? description : "").append(",");
        
        // Prerequisites as semicolon-separated values
        for (int i = 0; i < prerequisites.size(); i++) {
            sb.append(prerequisites.get(i));
            if (i < prerequisites.size() - 1) sb.append(";");
        }
        sb.append(",").append(isActive);
        
        return sb.toString();
    }

    public static Course fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 8) {
            Course course = new Course();
            course.setId(Integer.parseInt(parts[0]));
            course.setTitle(parts[1]);
            course.setCode(parts[2]);
            course.setDepartmentId(Integer.parseInt(parts[3]));
            course.setCapacity(Integer.parseInt(parts[4]));
            course.setDescription(parts[5]);
            
            // Parse prerequisites
            if (!parts[6].isEmpty()) {
                String[] prereqParts = parts[6].split(";");
                for (String prereq : prereqParts) {
                    if (!prereq.trim().isEmpty()) {
                        course.addPrerequisite(Integer.parseInt(prereq.trim()));
                    }
                }
            }
            
            course.setActive(Boolean.parseBoolean(parts[7]));
            return course;
        }
        return null;
    }

    public String getDisplayName() {
        return title + " (" + code + ")";
    }
}