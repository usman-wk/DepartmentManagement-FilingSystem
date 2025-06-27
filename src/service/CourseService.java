package service;

import model.Course;
import utils.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class CourseService {
    private static CourseService instance;
    private static final String COURSE_FILE = "data/courses.txt";
    private List<Course> courses;

    private CourseService() {
        courses = FileHandler.loadFromFile(COURSE_FILE, Course::fromString);
        if (courses == null) {
            courses = new ArrayList<>();
        }
    }

    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }

    public boolean createCourse(Course course) {
        // Check if course code already exists
        if (courses.stream().anyMatch(c -> c.getCode().equals(course.getCode()))) {
            return false;
        }
        
        // Generate new ID
        int newId = courses.stream().mapToInt(Course::getId).max().orElse(0) + 1;
        course.setId(newId);
        
        courses.add(course);
        saveCourses();
        return true;
    }

    public boolean updateCourse(Course course) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() == course.getId()) {
                courses.set(i, course);
                saveCourses();
                return true;
            }
        }
        return false;
    }

    public boolean deleteCourse(int courseId) {
        boolean removed = courses.removeIf(c -> c.getId() == courseId);
        if (removed) {
            saveCourses();
        }
        return removed;
    }

    public Course getCourseById(int id) {
        return courses.stream()
                .filter(c -> c.getId() == id && c.isActive())
                .findFirst()
                .orElse(null);
    }

    public Course getCourseByCode(String code) {
        return courses.stream()
                .filter(c -> c.getCode().equals(code) && c.isActive())
                .findFirst()
                .orElse(null);
    }

    public List<Course> getAllCourses() {
        return courses.stream()
                .filter(Course::isActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Course> getCoursesByDepartment(int departmentId) {
        return courses.stream()
                .filter(c -> c.getDepartmentId() == departmentId && c.isActive())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private void saveCourses() {
        FileHandler.saveToFile(COURSE_FILE, courses);
    }
}