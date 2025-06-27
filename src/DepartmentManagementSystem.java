import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DepartmentManagementSystem extends JFrame {
    private List<Department> departments;
    private List<String> auditLogs;
    private DefaultTableModel tableModel;
    private JTable departmentTable;
    private JTextField deptNameField, deptCodeField, assignDeptCodeField, hodNameField, searchField;
    private JTextArea deptDescArea;
    private JTextArea auditArea;
    
    private static final String DEPARTMENTS_FILE = "departments.dat";
    private static final String AUDIT_FILE = "audit.dat";
    
    public DepartmentManagementSystem() {
        departments = new ArrayList<>();
        auditLogs = new ArrayList<>();
        
        loadData();
        initializeGUI();
        loadTableData();
        loadAuditLogs();
    }
    
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    private void initializeGUI() {
        setTitle("Department Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main components
        createHeader();
        createMainPanel();
        createFooter();
        
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 64, 128));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("Department Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add Department Tab
        tabbedPane.addTab("Add Department", createAddDepartmentPanel());
        
        // Assign HoD Tab
        tabbedPane.addTab("Assign HoD", createAssignHoDPanel());
        
        // View & Search Tab
        tabbedPane.addTab("View & Search", createViewSearchPanel());
        
        // Audit Trail Tab
        tabbedPane.addTab("Audit Trail", createAuditTrailPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    @SuppressWarnings("unused")
    private JPanel createAddDepartmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Add New Department");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 64, 128));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Department Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        deptNameField = new JTextField(20);
        deptNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(deptNameField, gbc);
        
        // Department Code
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Department Code:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        deptCodeField = new JTextField(20);
        deptCodeField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(deptCodeField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        deptDescArea = new JTextArea(4, 20);
        deptDescArea.setFont(new Font("Arial", Font.PLAIN, 14));
        deptDescArea.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane scrollPane = new JScrollPane(deptDescArea);
        panel.add(scrollPane, gbc);
        
        // Add Button
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = createStyledButton("Add Department");
        addButton.addActionListener(e -> addDepartment());
        panel.add(addButton, gbc);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel createAssignHoDPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Assign Head of Department");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 64, 128));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Department Code
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Department Code:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        assignDeptCodeField = new JTextField(20);
        assignDeptCodeField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(assignDeptCodeField, gbc);
        
        // HoD Name
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("HoD Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        hodNameField = new JTextField(20);
        hodNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(hodNameField, gbc);
        
        // Assign Button
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton assignButton = createStyledButton("Assign HoD");
        assignButton.addActionListener(e -> assignHoD());
        panel.add(assignButton, gbc);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel createViewSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Real-time search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @SuppressWarnings("override")
            public void changedUpdate(DocumentEvent e) { searchDepartments(); }
            @SuppressWarnings("override")
            public void removeUpdate(DocumentEvent e) { searchDepartments(); }
            @SuppressWarnings("override")
            public void insertUpdate(DocumentEvent e) { searchDepartments(); }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Export buttons
        JButton exportCSVButton = createStyledButton("Export CSV");
        JButton exportPDFButton = createStyledButton("Export PDF");
        exportCSVButton.addActionListener(e -> exportToCSV());
        exportPDFButton.addActionListener(e -> exportToPDF());
        
        searchPanel.add(exportCSVButton);
        searchPanel.add(exportPDFButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Name", "Code", "Description", "HoD", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only actions column is editable
            }
        };
        
        departmentTable = new JTable(tableModel);
        departmentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        departmentTable.setRowHeight(25);
        
        // Style table header
        JTableHeader header = departmentTable.getTableHeader();
        header.setBackground(new Color(0, 64, 128));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add action buttons to table
        departmentTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        departmentTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane tableScrollPane = new JScrollPane(departmentTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAuditTrailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Audit Trail");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 64, 128));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        auditArea = new JTextArea();
        auditArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        auditArea.setEditable(false);
        auditArea.setBackground(new Color(248, 248, 248));
        
        JScrollPane auditScrollPane = new JScrollPane(auditArea);
        panel.add(auditScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0, 64, 128));
        footerPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel footerLabel = new JLabel("© 2025 Department Management System. All rights reserved.");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(footerLabel);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 64, 128));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @SuppressWarnings("override")
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 89, 179));
            }
            @SuppressWarnings("override")
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 64, 128));
            }
        });
        
        return button;
    }
    
    private void addDepartment() {
        String name = deptNameField.getText().trim();
        String code = deptCodeField.getText().trim();
        String description = deptDescArea.getText().trim();
        
        if (name.isEmpty() || code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Code are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for duplicates
        for (Department dept : departments) {
            if (dept.getName().equalsIgnoreCase(name) || dept.getCode().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(this, "Duplicate department name or code!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        Department newDept = new Department(name, code, description, "");
        departments.add(newDept);
        
        loadTableData();
        saveData();
        logChange("Added Department", name);
        
        // Clear fields
        deptNameField.setText("");
        deptCodeField.setText("");
        deptDescArea.setText("");
        
        JOptionPane.showMessageDialog(this, "Department added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void assignHoD() {
        String deptCode = assignDeptCodeField.getText().trim();
        String hodName = hodNameField.getText().trim();
        
        if (deptCode.isEmpty() || hodName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        for (Department dept : departments) {
            if (dept.getCode().equalsIgnoreCase(deptCode)) {
                dept.setHod(hodName);
                loadTableData();
                saveData();
                logChange("Assigned HoD", hodName + " to " + deptCode);
                
                assignDeptCodeField.setText("");
                hodNameField.setText("");
                
                JOptionPane.showMessageDialog(this, "HoD assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        
        JOptionPane.showMessageDialog(this, "Department not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void searchDepartments() {
        String keyword = searchField.getText().trim().toLowerCase();
        
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        for (Department dept : departments) {
            boolean matches = dept.getName().toLowerCase().contains(keyword) ||
                            dept.getCode().toLowerCase().contains(keyword) ||
                            (dept.getHod() != null && dept.getHod().toLowerCase().contains(keyword));
            
            if (matches) {
                Object[] row = {dept.getName(), dept.getCode(), dept.getDescription(), 
                              dept.getHod().isEmpty() ? "-" : dept.getHod(), "Actions"};
                tableModel.addRow(row);
            }
        }
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        
        for (Department dept : departments) {
            Object[] row = {dept.getName(), dept.getCode(), dept.getDescription(), 
                          dept.getHod().isEmpty() ? "-" : dept.getHod(), "Actions"};
            tableModel.addRow(row);
        }
    }
    
    private void editDepartment(int index) {
        if (index >= 0 && index < departments.size()) {
            Department dept = departments.get(index);
            
            String newName = JOptionPane.showInputDialog(this, "Enter new department name:", dept.getName());
            if (newName != null && !newName.trim().isEmpty()) {
                String newDesc = JOptionPane.showInputDialog(this, "Enter new description:", dept.getDescription());
                if (newDesc != null) {
                    dept.setName(newName.trim());
                    dept.setDescription(newDesc.trim());
                    loadTableData();
                    saveData();
                    logChange("Edited Department", newName);
                }
            }
        }
    }
    
    private void deleteDepartment(int index) {
        if (index >= 0 && index < departments.size()) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this department?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                Department dept = departments.remove(index);
                loadTableData();
                saveData();
                logChange("Deleted Department", dept.getName());
            }
        }
    }
    
    private void archiveDepartment(int index) {
        if (index >= 0 && index < departments.size()) {
            Department dept = departments.remove(index);
            loadTableData();
            saveData();
            logChange("Archived Department", dept.getName());
            JOptionPane.showMessageDialog(this, "Department archived successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("departments.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("Name,Code,Description,HoD");
                for (Department dept : departments) {
                    writer.printf("\"%s\",\"%s\",\"%s\",\"%s\"%n", 
                        dept.getName(), dept.getCode(), dept.getDescription(), dept.getHod());
                }
                JOptionPane.showMessageDialog(this, "CSV exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("departments.txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("DEPARTMENT MANAGEMENT SYSTEM");
                writer.println("============================");
                writer.println();
                
                for (int i = 0; i < departments.size(); i++) {
                    Department dept = departments.get(i);
                    writer.printf("%d. %s | %s | %s | HoD: %s%n", 
                        i + 1, dept.getName(), dept.getCode(), dept.getDescription(), 
                        dept.getHod().isEmpty() ? "-" : dept.getHod());
                }
                JOptionPane.showMessageDialog(this, "Report exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logChange(String action, String detail) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = timestamp + ": " + action + " - " + detail;
        auditLogs.add(logEntry);
        saveAuditLogs();
        loadAuditLogs();
    }
    
    private void loadAuditLogs() {
        StringBuilder sb = new StringBuilder();
        for (String log : auditLogs) {
            sb.append(log).append("\n");
        }
        if (auditArea != null) {
            auditArea.setText(sb.toString());
            auditArea.setCaretPosition(auditArea.getDocument().getLength());
        }
    }
    
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DEPARTMENTS_FILE))) {
            oos.writeObject(departments);
        } catch (IOException e) {
            System.err.println("Error saving departments: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DEPARTMENTS_FILE))) {
            departments = (List<Department>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            departments = new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(AUDIT_FILE))) {
            auditLogs = (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            auditLogs = new ArrayList<>();
        }
    }
    
    private void saveAuditLogs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AUDIT_FILE))) {
            oos.writeObject(auditLogs);
        } catch (IOException e) {
            System.err.println("Error saving audit logs: " + e.getMessage());
        }
    }
    
    // Button Renderer and Editor classes for table actions
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @SuppressWarnings("override")
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Edit | Delete | Archive");
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        
        @SuppressWarnings("Convert2Lambda")
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }
        
        @SuppressWarnings("override")
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            label = (value == null) ? "" : value.toString();
            button.setText("Edit | Delete | Archive");
            isPushed = true;
            return button;
        }
        
        @SuppressWarnings("override")
        public Object getCellEditorValue() {
            if (isPushed) {
                showActionDialog();
            }
            isPushed = false;
            return label;
        }
        
        @SuppressWarnings("override")
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
        
        @SuppressWarnings("override")
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
        
        private void showActionDialog() {
            String[] options = {"Edit", "Delete", "Archive", "Cancel"};
            int choice = JOptionPane.showOptionDialog(DepartmentManagementSystem.this,
                "Select an action:", "Department Actions",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
            
            switch (choice) {
                case 0 -> editDepartment(currentRow);
                case 1 -> deleteDepartment(currentRow);
                case 2 -> archiveDepartment(currentRow);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DepartmentManagementSystem().setVisible(true);
        });
    }
}