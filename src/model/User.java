package model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Role role;
    private int departmentId;
    private boolean isActive;

    public User() {}

    public User(int id, String name, String username, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.departmentId = -1;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return id + "," + name + "," + username + "," + email + "," + password + "," + 
               role + "," + departmentId + "," + isActive;
    }

    public static User fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 8) {
            User user = new User();
            user.setId(Integer.parseInt(parts[0]));
            user.setName(parts[1]);
            user.setUsername(parts[2]);
            user.setEmail(parts[3]);
            user.setPassword(parts[4]);
            user.setRole(Role.valueOf(parts[5]));
            user.setDepartmentId(Integer.parseInt(parts[6]));
            user.setActive(Boolean.parseBoolean(parts[7]));
            return user;
        }
        return null;
    }
}