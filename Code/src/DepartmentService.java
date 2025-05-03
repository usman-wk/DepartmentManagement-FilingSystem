import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    private final List<Department> departments = new ArrayList<>();

    public void createDepartment(Department department) {
        departments.add(department);
    }

    public Department getDepartmentById(int id) {
        return departments.stream().filter(d -> d.getDeptId() == id).findFirst().orElse(null);
    }

    public List<Department> getAllDepartments() {
        return departments;
    }
}
