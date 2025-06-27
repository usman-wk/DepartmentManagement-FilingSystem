package model;

public class User {
    private final int    userId;
    private final String name;
    private String role;   // “Student”, “Staff”, “Admin”, …
    private       int    deptId; // -1 = unassigned

    public User(int userId, String name, String role, int deptId) {
        this.userId = userId;
        this.name   = name;
        this.role   = role;
        this.deptId = deptId;
    }
public void setRole(String role) { this.role = role; }
    /* handy constructor for “not yet in a dept” */
    public User(int userId, String name, String role) {
        this(userId, name, role, -1);
        
    }

    /* getters */
    public int    getUserId() { return userId; }
    public String getName()   { return name;   }
    public String getRole()   { return role;   }
    public int    getDeptId() { return deptId; }

    /* util */
    @Override
    public String toString() {
        return "ID:" + userId +
               ", Name:" + name +
               ", Role:" + role +
               ", DeptID:" + (deptId == 0 ? "None" : deptId);
    }

    public static User fromString(String line) {
        String[] p = line.split(",");
        int id    = Integer.parseInt(p[0].split(":")[1].trim());
        String nm = p[1].split(":")[1].trim();
        String rl = p[2].split(":")[1].trim();
        String d  = p[3].split(":")[1].trim();
        int dept  = d.equals("None") ? 0 : Integer.parseInt(d);
        return new User(id, nm, rl, dept);
    }
}
