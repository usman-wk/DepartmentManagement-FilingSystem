package gui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewUsersPanel extends JPanel {
    public ViewUsersPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        var users = frame.getUserService().getAllUsers();

        String[] cols = {"ID","Name","Role","DeptID"};
        String[][] rows = users.stream()
                .map(u -> new String[]{
                         String.valueOf(u.getUserId()),
                         u.getName(),
                         u.getRole(),
                         u.getDeptId()==-1 ? "None" : String.valueOf(u.getDeptId())})
                .toArray(String[][]::new);

        JTable table = new JTable(rows, cols);
        table.setEnabled(false);

        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.showMainMenu());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}
