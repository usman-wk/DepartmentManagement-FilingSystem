package model;

import java.io.Serializable;

public class Department implements Serializable {
    private int id;
    private String name;
    private String code;
    private int hodId;
    private String description;
    private boolean isActive;

    public Department() {}

    public Department(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.hodId = -1;
        this.description = "";
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getHodId() { return hodId; }
    public void setHodId(int hodId) { this.hodId = hodId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return id + "," + name + "," + code + "," + hodId + "," + 
               (description != null ? description : "") + "," + isActive;
    }

    public static Department fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 6) {
            Department dept = new Department();
            dept.setId(Integer.parseInt(parts[0]));
            dept.setName(parts[1]);
            dept.setCode(parts[2]);
            dept.setHodId(Integer.parseInt(parts[3]));
            dept.setDescription(parts[4]);
            dept.setActive(Boolean.parseBoolean(parts[5]));
            return dept;
        }
        return null;
    }

    public String getDisplayName() {
        return name + " (" + code + ")";
    }
}