package models;

public class Department {
    private String name;
    private String discipline;
    private String head;
    private int totalCourses;

    public Department(String name, String discipline, String head, int totalCourses) {
        this.name = name;
        this.discipline = discipline;
        this.head = head;
        this.totalCourses = totalCourses;
    }

    public String getName() { return name; }
    public String getDiscipline() { return discipline; }
    public String getHead() { return head; }
    public int getTotalCourses() { return totalCourses; }

    public void setName(String name) { this.name = name; }
    public void setDiscipline(String discipline) { this.discipline = discipline; }
    public void setHead(String head) { this.head = head; }
    public void setTotalCourses(int totalCourses) { this.totalCourses = totalCourses; }
}
