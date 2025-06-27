package service;

import model.Department;
import utils.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class DepartmentService {
    private static DepartmentService instance;
    private static final String DEPARTMENT_FILE = "data/departments.txt";
    private List<Department> departments;

    private DepartmentService() {
        departments = FileHandler.loadFromFile(DEPARTMENT_FILE, Department::fromString);
        if (departments == null) {
            departments = new ArrayList<>();
        }
    }

    public static DepartmentService getInstance() {
        if (instance == null) {
            instance = new DepartmentService();
        }
        return instance;
    }

    public boolean createDepartment(Department department) {
        // Check if department code already exists
        if (departments.stream().anyMatch(d -> d.getCode().equals(department.getCode()))) {
            return false;
        }
        
        // Generate new ID
        int newId = departments.stream().mapToInt(Department::getId).max().orElse(0) + 1;
        department.setId(newId);
        
        departments.add(department);
        saveDepartments();
        return true;
    }

    public boolean updateDepartment(Department department) {
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == department.getId()) {
                departments.set(i, department);
                saveDepartments();
                return true;
            }
        }
        return false;
    }

    public boolean deleteDepartment(int departmentId) {
        boolean removed = departments.removeIf(d -> d.getId() == departmentId);
        if (removed) {
            saveDepartments();
        }
        return removed;
    }

    public Department getDepartmentById(int id) {
        return departments.stream()
                .filter(d -> d.getId() == id && d.isActive())
                .findFirst()
                .orElse(null);
    }

    public Department getDepartmentByCode(String code) {
        return departments.stream()
                .filter(d -> d.getCode().equals(code) && d.isActive())
                .findFirst()
                .orElse(null);
    }

    public List<Department> getAllDepartments() {
        return departments.stream()
                .filter(Department::isActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public boolean assignHOD(int departmentId, int hodId) {
        Department department = getDepartmentById(departmentId);
        if (department != null) {
            department.setHodId(hodId);
            updateDepartment(department);
            return true;
        }
        return false;
    }

    private void saveDepartments() {
        FileHandler.saveToFile(DEPARTMENT_FILE, departments);
    }
}