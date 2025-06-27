package gui;

import service.DepartmentService;
import service.UserService;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final UserService userService;
    private final DepartmentService departmentService;

    public MainFrame() {
        this.userService = new UserService();
        this.departmentService = new DepartmentService();
        utils.DataSeeder.seed(userService, departmentService);
        
        setTitle("Department and User Management");
        setSize(700, 500);  
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // IMPORTANT: show main menu panel
        showMainMenu();

        setVisible(true); // Do this AFTER showMainMenu()
    }
    public void showAssignHODPanel() {
    setContentPane(new AssignHODPanel(this));
    revalidate();
    repaint();
}
// in MainFrame.java
public void showCreateUserPanel() {
    setContentPane(new CreateUserPanel(this));
    revalidate();
    repaint();
}

public void showViewDepartmentsPanel() {
    setContentPane(new ViewDepartmentsPanel(this));
    revalidate();
    repaint();
}

public void showViewUsersPanel() {
    setContentPane(new ViewUsersPanel(this));
    revalidate();
    repaint();
}


    public void showMainMenu() {
setContentPane(new MainMenuPanel(this));

        revalidate();
        repaint();
    }

    public void showCreateDepartmentPanel() {
        setContentPane(new CreateDeptPanel(this));
        revalidate();
        repaint();
    }

    public UserService getUserService() {
        return userService;
    }

    public DepartmentService getDepartmentService() {
        departmentService.load(); 
        return departmentService;
        
        
    }
}
