package service;

import model.Department;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DepartmentService {

    private final List<Department> departments = new ArrayList<>();
    private static final Path FILE = Paths.get("departments.txt");

    /* ------------ CRUD in memory ------------ */
    public void createDepartment(Department d) { departments.add(d); }
    public Department getDepartmentById(int id){
        return departments.stream().filter(x -> x.getDeptId()==id).findFirst().orElse(null);
    }
    public List<Department> getAllDepartments() { return departments; }

    public void updateDepartment(int id, String newName, int newHod){
        Department d = getDepartmentById(id);
        if (d!=null){
            d.setName(newName);
            d.setHodId(newHod);
        }
    }

    /* ------------ persistence ------------ */
    public void load() {
        if (!Files.exists(FILE)) return;
        try (BufferedReader br = Files.newBufferedReader(FILE)) {
            br.lines()
              .filter(l -> !l.isBlank())
              .map(Department::fromString)
              .forEach(departments::add);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public void save() {
        try (BufferedWriter bw = Files.newBufferedWriter(FILE)) {
            for (Department d : departments) bw.write(d.toString() + System.lineSeparator());
        } catch (IOException ex) { ex.printStackTrace(); }
    }
}
