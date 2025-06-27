package gui;

import model.Department;
import service.DepartmentService;

import javax.swing.*;
import java.awt.*;

public class CreateDeptPanel extends JPanel {

    public CreateDeptPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.BACKGROUND);

        // ----- title -----
        JLabel title = new JLabel("Create Department", SwingConstants.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ----- form -----
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill  = GridBagConstraints.HORIZONTAL;

        JTextField idField   = new JTextField(15);
        JTextField nameField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; form.add(UIStyle.label("Department ID:"), gbc);
        gbc.gridx = 1;              form.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(UIStyle.label("Name:"), gbc);
        gbc.gridx = 1;               form.add(nameField, gbc);

        // ----- buttons -----
        JButton createBtn = new JButton("Create");
        JButton backBtn   = new JButton("Back");
        UIStyle.styleButton(createBtn);
        UIStyle.styleButton(backBtn);

        JPanel btnPanel = new JPanel();
        btnPanel.add(createBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(btnPanel, gbc);

        add(form, BorderLayout.CENTER);

        /* ---------- actions ---------- */
        createBtn.addActionListener(e -> {
            try {
                int    id   = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name is required.");
                    return;
                }
                DepartmentService ds = frame.getDepartmentService();
                if (ds.getDepartmentById(id) != null) {
                    JOptionPane.showMessageDialog(this, "ID already exists.");
                    return;
                }
                ds.createDepartment(new Department(id, name));
                JOptionPane.showMessageDialog(this, "Department created.");
                idField.setText("");
                nameField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID.");
            }
        });

        backBtn.addActionListener(e -> frame.showMainMenu());
    }
}
