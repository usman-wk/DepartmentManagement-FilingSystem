package model;

public class User {
    private int userId;
    private String name;
    private String role; // "HOD" or "Student"
    private int deptId;

    public User(int userId, String name, String role, int deptId) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.deptId = deptId;
    }

    User(int i, String alice, Role admin) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getDeptId() {
        return deptId;
    }

    @Override
    public String toString() {
        return userId + "," + name + "," + role + "," + deptId;
    }

    public static User fromString(String line) {
    String[] parts = line.split(",");
    int id = Integer.parseInt(parts[0].split(":")[1].trim());
    String name = parts[1].split(":")[1].trim();
    String role = parts[2].split(":")[1].trim();
    String deptPart = parts[3].split(":")[1].trim();

    int deptId = -1; // default if null
    if (!deptPart.equalsIgnoreCase("null")) {
        deptId = Integer.parseInt(deptPart);
    }

    return new User(id, name, role, deptId);

    }
}
