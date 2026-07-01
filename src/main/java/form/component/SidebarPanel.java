package form.component;

import form.util.FormNavigator;
import form.util.SwingStyleHelper;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class SidebarPanel extends JPanel {

    public enum ActiveMenu {
        DASHBOARD,
        BARANG,
        TRANSAKSI,
        LAPORAN
    }

    private static final Color SIDEBAR_COLOR = SwingStyleHelper.PRIMARY;
    private static final Color TEXT_COLOR = new Color(240, 245, 255);
    private static final Color SECONDARY_TEXT = new Color(191, 219, 254);
    private static final Color BUTTON_BG = new Color(15, 23, 42);
    private static final Color BUTTON_HOVER_BG = new Color(31, 41, 55);
    private static final Color LOGOUT_COLOR = new Color(239, 68, 68);
    private static final Dimension BUTTON_SIZE = new Dimension(170, 44);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private Color getMenuColor(ActiveMenu menu) {
        if (menu == null) {
            return LOGOUT_COLOR;
        }
        switch (menu) {
            case DASHBOARD:
                return new Color(37, 99, 235);
            case BARANG:
                return new Color(29, 78, 216);
            case TRANSAKSI:
                return new Color(16, 185, 129);
            case LAPORAN:
                return new Color(139, 92, 246);
            default:
                return BUTTON_BG;
        }
    }

    private ImageIcon createSidebarIcon(ActiveMenu menu, boolean active) {
        int size = 18;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color fill = active ? getMenuColor(menu) : new Color(148, 163, 184);
        g.setColor(fill);

        if (menu == ActiveMenu.DASHBOARD) {
            int box = 5;
            g.fill(new RoundRectangle2D.Float(1, 1, box, box, 4, 4));
            g.fill(new RoundRectangle2D.Float(1, 12, box, box, 4, 4));
            g.fill(new RoundRectangle2D.Float(12, 1, box, box, 4, 4));
            g.fill(new RoundRectangle2D.Float(12, 12, box, box, 4, 4));
        } else if (menu == ActiveMenu.BARANG) {
            g.fill(new RoundRectangle2D.Float(2, 4, 14, 10, 4, 4));
            g.setColor(new Color(255, 255, 255, 180));
            g.fill(new RoundRectangle2D.Float(4, 6, 10, 6, 3, 3));
        } else if (menu == ActiveMenu.TRANSAKSI) {
            g.setStroke(new java.awt.BasicStroke(2));
            g.draw(new Line2D.Float(4, 14, 14, 4));
            g.draw(new Line2D.Float(4, 7, 4, 16));
            g.draw(new Line2D.Float(4, 7, 11, 7));
        } else if (menu == ActiveMenu.LAPORAN) {
            Path2D.Float doc = new Path2D.Float();
            doc.moveTo(4, 2);
            doc.lineTo(12, 2);
            doc.lineTo(14, 4);
            doc.lineTo(14, 16);
            doc.lineTo(4, 16);
            doc.closePath();
            g.fill(doc);
            g.setColor(new Color(255, 255, 255, 180));
            g.fill(new RoundRectangle2D.Float(6, 6, 6, 2, 2, 2));
            g.fill(new RoundRectangle2D.Float(6, 10, 6, 2, 2, 2));
        } else {
            g.fill(new RoundRectangle2D.Float(4, 3, 10, 12, 4, 4));
            g.setColor(new Color(255, 255, 255, 220));
            g.fill(new Ellipse2D.Float(7, 6, 4, 4));
            g.draw(new Line2D.Float(9, 10, 9, 14));
        }

        g.dispose();
        return new ImageIcon(image);
    }

    private ImageIcon createBrandIcon() {
        int size = 28;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(59, 130, 246));
        g.fill(new RoundRectangle2D.Float(4, 8, 20, 12, 8, 8));
        g.setColor(new Color(255, 255, 255, 220));
        g.fill(new RoundRectangle2D.Float(8, 4, 12, 20, 6, 6));
        g.dispose();
        return new ImageIcon(image);
    }

    public SidebarPanel(JFrame parent, ActiveMenu activeMenu) {
        setBackground(SIDEBAR_COLOR);
        setPreferredSize(new Dimension(240, 579));
        setLayout(new AbsoluteLayout());

        JLabel brand = new JLabel("StockMate");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(Color.WHITE);
        brand.setIcon(createBrandIcon());
        brand.setIconTextGap(12);
        add(brand, new AbsoluteConstraints(28, 24, 180, 40));

        JLabel subTitle = new JLabel("Inventory & POS");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(SECONDARY_TEXT);
        add(subTitle, new AbsoluteConstraints(28, 60, 180, 24));

        addButton("Dashboard", 120, ActiveMenu.DASHBOARD, activeMenu,
                () -> FormNavigator.openDashboard(parent));
        addButton("Barang", 180, ActiveMenu.BARANG, activeMenu,
                () -> FormNavigator.openBarang(parent));
        addButton("Transaksi", 240, ActiveMenu.TRANSAKSI, activeMenu,
                () -> FormNavigator.openTransaksi(parent));
        addButton("Laporan", 300, ActiveMenu.LAPORAN, activeMenu,
                () -> FormNavigator.openLaporan(parent));
        addButton("Logout", 360, null, activeMenu,
                () -> FormNavigator.logout(parent));
    }

    private void addButton(String text, int y, ActiveMenu menu, ActiveMenu activeMenu, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 24), 1, true),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);

        boolean isActive = menu != null && menu == activeMenu;
        boolean isLogout = menu == null;
        Color baseColor = isActive ? getMenuColor(menu) : (isLogout ? LOGOUT_COLOR.darker() : BUTTON_BG);
        Color hoverColor = isActive ? getMenuColor(menu).brighter() : (isLogout ? LOGOUT_COLOR : BUTTON_HOVER_BG);

        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setIcon(createSidebarIcon(menu, isActive));
        button.setIconTextGap(12);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    button.setBackground(baseColor);
                }
            }
        });

        if (action != null) {
            button.addActionListener(event -> action.run());
        }

        add(button, new AbsoluteConstraints(32, y, 160, 44));
    }
}
