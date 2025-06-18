package views;

import models.Department;
import services.ReportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dashboard extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JLabel totalDepts, totalDisciplines;
    private ReportService service = new ReportService();

    public Dashboard() {
        setTitle("DeptTrack - Reporting Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initTopPanel();
        initTable();
        initBottomPanel();
        loadData();
    }

    private void initTopPanel() {
        JPanel top = new JPanel(new BorderLayout());

        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> filterTable());

        JButton addBtn = new JButton("+");
        addBtn.addActionListener(e -> openAddEditDialog(-1));

        JButton editBtn = new JButton("Edit");
        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                openAddEditDialog(table.convertRowIndexToModel(selectedRow));
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to edit.");
            }
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deleteSelectedRow());

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(addBtn);
        searchPanel.add(editBtn);
        searchPanel.add(deleteBtn);

        top.add(searchPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] cols = {"Department", "Discipline", "Head", "Total Courses"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        JPanel bottom = new JPanel(new GridLayout(2, 2));
        totalDepts = new JLabel("Total Departments: 0");
        totalDisciplines = new JLabel("Total Disciplines: 0");
        JButton exportBtn = new JButton("Export to CSV");
        exportBtn.addActionListener(e -> exportToCSV());
        bottom.add(totalDepts);
        bottom.add(totalDisciplines);
        bottom.add(exportBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        List<Department> depts = service.getDepartments();
        for (Department d : depts) {
            model.addRow(new Object[]{d.getName(), d.getDiscipline(), d.getHead(), d.getTotalCourses()});
        }
        updateStats();
    }

    private void updateStats() {
        Set<String> deptSet = new HashSet<>();
        Set<String> disciplineSet = new HashSet<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            deptSet.add(model.getValueAt(i, 0).toString());
            disciplineSet.add(model.getValueAt(i, 1).toString());
        }
        totalDepts.setText("Total Departments: " + deptSet.size());
        totalDisciplines.setText("Total Disciplines: " + disciplineSet.size());
    }

    private void filterTable() {
        String keyword = searchField.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
    }

    private void exportToCSV() {
        try (FileWriter fw = new FileWriter("report.csv")) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                fw.append(model.getColumnName(i)).append(",");
            }
            fw.append("\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    fw.append(model.getValueAt(i, j).toString()).append(",");
                }
                fw.append("\n");
            }
            JOptionPane.showMessageDialog(this, "Exported to report.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
        }
    }

    private void openAddEditDialog(int rowIndex) {
        JTextField deptField = new JTextField();
        JTextField discField = new JTextField();
        JTextField headField = new JTextField();
        JTextField courseField = new JTextField();

        if (rowIndex >= 0) {
            deptField.setText(model.getValueAt(rowIndex, 0).toString());
            discField.setText(model.getValueAt(rowIndex, 1).toString());
            headField.setText(model.getValueAt(rowIndex, 2).toString());
            courseField.setText(model.getValueAt(rowIndex, 3).toString());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Department Name:"));
        panel.add(deptField);
        panel.add(new JLabel("Discipline:"));
        panel.add(discField);
        panel.add(new JLabel("Head of Department:"));
        panel.add(headField);
        panel.add(new JLabel("Total Courses:"));
        panel.add(courseField);

        int result = JOptionPane.showConfirmDialog(this, panel, rowIndex == -1 ? "Add New Department" : "Edit Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String dept = deptField.getText().trim();
            String disc = discField.getText().trim();
            String head = headField.getText().trim();
            int courses;
            try {
                courses = Integer.parseInt(courseField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid course count.");
                return;
            }
            if (rowIndex == -1) {
                model.addRow(new Object[]{dept, disc, head, courses});
            } else {
                model.setValueAt(dept, rowIndex, 0);
                model.setValueAt(disc, rowIndex, 1);
                model.setValueAt(head, rowIndex, 2);
                model.setValueAt(courses, rowIndex, 3);
            }
            updateStats();
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this row?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(table.convertRowIndexToModel(selectedRow));
                updateStats();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
