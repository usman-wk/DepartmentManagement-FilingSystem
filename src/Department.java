import java.io.Serializable;

/**
 * Department class represents a department entity in the management system.
 * Implements Serializable for file-based data persistence.
 */
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String code;
    private String description;
    private String hod; // Head of Department
    
    /**
     * Default constructor
     */
    public Department() {
        this("", "", "", "");
    }
    
    /**
     * Parameterized constructor
     * @param name Department name
     * @param code Department code
     * @param description Department description
     * @param hod Head of Department name
     */
    public Department(String name, String code, String description, String hod) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.hod = hod;
    }
    
    // Getter methods
    public String getName() {
        return name;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getHod() {
        return hod;
    }
    
    // Setter methods
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setHod(String hod) {
        this.hod = hod;
    }
    
    /**
     * String representation of the department
     */
    @Override
    public String toString() {
        return String.format("Department{name='%s', code='%s', description='%s', hod='%s'}", 
                           name, code, description, hod);
    }
    
    /**
     * Check equality based on department code (unique identifier)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Department that = (Department) obj;
        return code != null ? code.equals(that.code) : that.code == null;
    }
    
    /**
     * Hash code based on department code
     */
    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}