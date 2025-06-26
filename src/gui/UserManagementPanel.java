package gui;

import model.User;
import model.Role;
import model.Department;
import service.UserService;
import service.DepartmentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private User currentUser;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private UserService userService;
    private DepartmentService departmentService;

    public UserManagementPanel(User currentUser) {
        this.currentUser = currentUser;
        this.userService = UserService.getInstance();
        this.departmentService = DepartmentService.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUsers();
    }

    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Name", "Username", "Email", "Role", "Department", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");
        refreshButton = new JButton("Refresh");
        
        // Style buttons
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(0, 123, 255));
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(108, 117, 125));
        refreshButton.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("User Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Table panel
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddUserDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userService.getAllUsers();
        
        for (User user : users) {
            String departmentName = "None";
            if (user.getDepartmentId() > 0) {
                Department dept = departmentService.getDepartmentById(user.getDepartmentId());
                if (dept != null) {
                    departmentName = dept.getName();
                }
            }
            
            Object[] row = {
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getDisplayName(),
                departmentName,
                user.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddUserDialog() {
        UserDialog dialog = new UserDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadUsers();
        }
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        User user = userService.getUserById(userId);
        
        if (user != null) {
            UserDialog dialog = new UserDialog((JFrame) SwingUtilities.getWindowAncestor(this), user);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                loadUsers();
            }
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String userName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete user: " + userName + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (userService.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully.", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}