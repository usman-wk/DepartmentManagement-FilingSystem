package model;

public class Department {
    private int deptId;
    private String name;
    private String code;
    private int hodId;

    public Department(int deptId, String name, String code) {
        this.deptId = deptId;
        this.name = name;
        this.code = code;
        this.hodId = -1; // default, no HOD assigned
    }

    public int getDeptId() {
        return deptId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getHodId() {
        return hodId;
    }

    public void setHodId(int hodId) {
        this.hodId = hodId;
    }

    public boolean hasStudent(int userId) {
        return false;
    }

    @Override
    public String toString() {
        return "ID: " + deptId + ", Name: " + name + ", Code: " + code + ", HOD ID: " + hodId;
    }

    public static Department fromString(String line) {
    String[] parts = line.split(",");
    int id = Integer.parseInt(parts[0].split(":")[1].trim());
    String name = parts[1].split(":")[1].trim();
    String code = parts[2].split(":")[1].trim();
    int hodId = Integer.parseInt(parts[3].split(":")[1].trim());

    Department dept = new Department(id, name, code);
    dept.setHodId(hodId);
    return dept;

    }

    public void setHod(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
