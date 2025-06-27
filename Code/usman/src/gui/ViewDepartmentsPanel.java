package gui;

import model.Department;
import service.DepartmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewDepartmentsPanel extends JPanel {

    private final DefaultTableModel model;

    public ViewDepartmentsPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.BACKGROUND);

        DepartmentService ds = frame.getDepartmentService();
        List<Department>   deps = ds.getAllDepartments();

        String[] cols = {"ID", "Name", "HOD-ID"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r,int c){ return c!=0; } // ID not editable
        };
        deps.forEach(d -> model.addRow(new Object[]{ d.getDeptId(), d.getName(),
                                                     d.getHodId()==-1?"":d.getHodId() }));

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");
        UIStyle.styleButton(saveBtn);
        UIStyle.styleButton(backBtn);

        saveBtn.addActionListener(e -> {
            for (int r=0;r<model.getRowCount();r++){
                int id   = (int)    model.getValueAt(r,0);
                String name = ((String) model.getValueAt(r,1)).trim();
                String hodStr = ((String) model.getValueAt(r,2)).trim();
                int hod = hodStr.isBlank()? -1 : Integer.parseInt(hodStr);
                ds.updateDepartment(id, name, hod);
            }
            ds.save();                        // write back to file
            JOptionPane.showMessageDialog(this,"Changes saved!");
        });

        backBtn.addActionListener(e -> frame.showMainMenu());

        JPanel south = new JPanel();
        south.add(saveBtn);
        south.add(backBtn);

        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}
