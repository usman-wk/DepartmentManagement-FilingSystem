package gui;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JLabel;

public class UIStyle {
    public static Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);
    public static Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    public static Color PRIMARY_COLOR = new Color(33, 150, 243);
    public static Color BUTTON_COLOR = new Color(25, 118, 210);
    public static Color BACKGROUND = new Color(245, 245, 245);

    public static void styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }
}
