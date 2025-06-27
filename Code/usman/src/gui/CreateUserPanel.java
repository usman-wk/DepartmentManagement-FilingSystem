package gui;

import model.User;
import service.UserService;
import service.DepartmentService;

import javax.swing.*;
import java.awt.*;

public class CreateUserPanel extends JPanel {

    public CreateUserPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.BACKGROUND);

        JLabel title = new JLabel("Add User / Student / Staff", SwingConstants.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(20,10,10,10));
        add(title, BorderLayout.NORTH);

        /* ------------- form ------------ */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill  = GridBagConstraints.HORIZONTAL;

        JTextField idField   = new JTextField(15);
        JTextField nameField = new JTextField(15);
        String[] roles       = { "Student", "Staff", "Admin" };
        JComboBox<String> roleBox = new JComboBox<>(roles);

        // department dropdown (optional)
        DepartmentService dSvc = frame.getDepartmentService();
        String[] deptChoices = dSvc.getAllDepartments().stream()
                                   .map(dep -> dep.getDeptId() + " – " + dep.getName())
                                   .toArray(String[]::new);
        JComboBox<String> deptBox = new JComboBox<>(deptChoices);
        deptBox.insertItemAt("None", 0);
        deptBox.setSelectedIndex(0);

        int row = 0;
        gbc.gridx=0; gbc.gridy=row; form.add(UIStyle.label("User ID:"), gbc);
        gbc.gridx=1;                form.add(idField, gbc);

        gbc.gridx=0; gbc.gridy=++row; form.add(UIStyle.label("Name:"), gbc);
        gbc.gridx=1;                  form.add(nameField, gbc);

        gbc.gridx=0; gbc.gridy=++row; form.add(UIStyle.label("Role:"), gbc);
        gbc.gridx=1;                  form.add(roleBox, gbc);

        gbc.gridx=0; gbc.gridy=++row; form.add(UIStyle.label("Department:"), gbc);
        gbc.gridx=1;                  form.add(deptBox, gbc);

        /* ------------- buttons ------------ */
        JButton addBtn  = new JButton("Add");
        JButton backBtn = new JButton("Back");
        UIStyle.styleButton(addBtn);
        UIStyle.styleButton(backBtn);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2;
        form.add(btnPanel, gbc);

        add(form, BorderLayout.CENTER);

        /* -------- actions -------- */
        addBtn.addActionListener(e -> {
            try {
                int id      = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String role = roleBox.getSelectedItem().toString();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name can’t be empty");
                    return;
                }
                UserService uSvc = frame.getUserService();
                if (uSvc.getUserById(id) != null) {
                    JOptionPane.showMessageDialog(this, "ID already exists");
                    return;
                }

                // department selection
                int deptId = -1;
                if (deptBox.getSelectedIndex() > 0) {          // not “None”
                    String sel = deptBox.getSelectedItem().toString();
                    deptId = Integer.parseInt(sel.split("–")[0].trim());
                }

                uSvc.addUser(new User(id, name, role, deptId));
                JOptionPane.showMessageDialog(this, "User added successfully");
                idField.setText(""); nameField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid numeric ID");
            }
        });

        backBtn.addActionListener(e -> frame.showMainMenu());
    }
}
