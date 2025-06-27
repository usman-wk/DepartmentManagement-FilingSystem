package model;

public class Department {
    private final int deptId;
    private final String name;
    private int hodId = -1;          // –1 means “no HOD yet”

    public Department(int deptId, String name) {
        this.deptId = deptId;
        this.name  = name;
    }

    /* ---------- getters ---------- */
    public int    getDeptId() { return deptId; }
    public String getName()   { return name;   }
    public int    getHodId()  { return hodId;  }

    /* ---------- business ops ---------- */
    public void setHod(User user) {
    this.hodId = user.getUserId();
    user.setRole("HOD");            //  ← updates the user instantly
}

    public void setHodId(int hodId)      { this.hodId = hodId; }

    /* ---------- util ---------- */
    @Override
    public String toString() {
        return "ID: " + deptId +
               ", Name: " + name +
               ", HOD-ID: " + (hodId == -1 ? "None" : hodId);
    }

    /** recreate object from `toString()` output (handy for file/db import) */
    public static Department fromString(String line) {
        String[] p = line.split(",");
        int id    = Integer.parseInt(p[0].split(":")[1].trim());
        String nm = p[1].split(":")[1].trim();
        int hod   = Integer.parseInt(p[2].split(":")[1].trim().replace("None","-1"));

        Department d = new Department(id, nm);
        d.setHodId(hod);
        return d;
    }

    public void setName(String newName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
