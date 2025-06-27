package gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.BACKGROUND);

        JLabel title = new JLabel("Department Management System", SwingConstants.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));
        btnPanel.setBackground(UIStyle.BACKGROUND);

        String[] names = { "Create Department", "Add User", "Assign HOD",
                   "View Departments", "View Users", "Exit" };
        JButton[] buttons = new JButton[names.length];
        for (int i = 0; i < names.length; i++) {
            buttons[i] = new JButton(names[i]);
            UIStyle.styleButton(buttons[i]);
            btnPanel.add(buttons[i]);
        }

        buttons[1].addActionListener(e -> frame.showCreateUserPanel());  // Add User
buttons[2].addActionListener(e -> frame.showAssignHODPanel());   // Assign HOD
buttons[3].addActionListener(e -> frame.showViewDepartmentsPanel());
buttons[4].addActionListener(e -> frame.showViewUsersPanel());
buttons[5].addActionListener(e -> System.exit(0));

        add(title, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
    }
}
