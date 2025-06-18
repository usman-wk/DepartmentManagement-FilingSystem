package services;

import models.Department;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    public List<Department> getDepartments() {
        List<Department> list = new ArrayList<>();
        list.add(new Department("CS", "Software Engineering", "Dr. Rehman", 10));
        list.add(new Department("EE", "Electrical", "Dr. Khan", 8));
        list.add(new Department("BBA", "Business", "Ms. Fatima", 6));
        return list;
    }
}
