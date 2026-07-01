package form.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

public final class SwingStyleHelper {

    public static final Color PRIMARY = new Color(30, 58, 138);
    public static final Color SECONDARY = new Color(37, 99, 235);
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color TEXT = new Color(51, 65, 85);
    public static final Color MUTED = new Color(100, 116, 139);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

    private SwingStyleHelper() {
    }

    public static JButton createRoundedButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(new RoundedBorder(16));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    public static void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setBackground(Color.WHITE);
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(15, 23, 42));
        table.setGridColor(new Color(226, 232, 240));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(new Color(71, 85, 105));
    }

    public static void stylePanel(JComponent component) {
        component.setBackground(CARD);
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    public static Border sectionBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    public static class RoundedBorder implements Border {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(6, 12, 6, 12);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(0, 0, 0, 0));
        }

        public int getRadius() {
            return radius;
        }
    }
}
