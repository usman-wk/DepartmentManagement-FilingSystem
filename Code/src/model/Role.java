package model;

import java.io.Serializable;

public class Role implements Serializable {
    private int roleId;
    private String name;

    public Role(int roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Role{" + "roleId=" + roleId + ", name='" + name + "'}";
    }
}
