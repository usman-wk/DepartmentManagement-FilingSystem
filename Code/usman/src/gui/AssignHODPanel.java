package gui;

import model.Department;
import model.User;
import service.DepartmentService;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class AssignHODPanel extends JPanel {
    public AssignHODPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.BACKGROUND);

        JLabel title = new JLabel("Assign HOD", SwingConstants.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField deptIdField = new JTextField(15);
        JTextField userIdField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; form.add((Component) UIStyle.label("Department ID:"), gbc);
        gbc.gridx = 1; form.add(deptIdField, gbc);

        gbc.gridx = 0; gbc.gridy++; form.add((Component) UIStyle.label("User ID:"), gbc);
        gbc.gridx = 1; form.add(userIdField, gbc);

        JButton assignBtn = new JButton("Assign");
        JButton backBtn = new JButton("Back");
        UIStyle.styleButton(assignBtn);
        UIStyle.styleButton(backBtn);

        JPanel btnPanel = new JPanel();
        btnPanel.add(assignBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        form.add(btnPanel, gbc);

        assignBtn.addActionListener(e -> {
            try {
                int deptId = Integer.parseInt(deptIdField.getText().trim());
                int userId = Integer.parseInt(userIdField.getText().trim());
                Department dept = frame.getDepartmentService().getDepartmentById(deptId);
                User user = frame.getUserService().getUserById(userId);
                if (dept == null) {
                    JOptionPane.showMessageDialog(this, "Department not found.");
                    return;
                }
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "User not found.");
                    return;
                }
                dept.setHod(user);                 // now swaps role to HOD too
                JOptionPane.showMessageDialog(this, "HOD assigned.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        });

        backBtn.addActionListener(e -> frame.showMainMenu());

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
    }
}

