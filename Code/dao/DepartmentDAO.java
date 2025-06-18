package dao;


import models.Department;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        list.add(new Department("Computer Science", "AI", "Dr. Ahsan", 12));
        list.add(new Department("Computer Science", "Cyber Security", "Dr. Ahsan", 9));
        list.add(new Department("Electrical Engineering", "Power Systems", "Dr. Nadeem", 7));
        list.add(new Department("IT", "Software Engineering", "Dr. Salman", 10));
        return list;
    }
}