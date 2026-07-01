package form.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public final class AppHeaderPanel {

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Color ACCENT_COLOR = new Color(37, 99, 235);
    private static final Color TITLE_COLOR = new Color(15, 23, 42);

    private AppHeaderPanel() {
    }

    public static JPanel create(String pageTitle) {
        JPanel header = new JPanel(new AbsoluteLayout());
        header.setBackground(Color.WHITE);
        header.setMinimumSize(new Dimension(1600, 76));
        header.setPreferredSize(new Dimension(1600, 76));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel lblStockmate = new JLabel("STOCKMATE");
        lblStockmate.setBackground(Color.WHITE);
        lblStockmate.setForeground(ACCENT_COLOR);
        lblStockmate.setFont(TITLE_FONT);
        header.add(lblStockmate, new AbsoluteConstraints(40, 18, -1, -1));

        JLabel lblPageTitle = new JLabel(pageTitle);
        lblPageTitle.setForeground(TITLE_COLOR);
        lblPageTitle.setFont(TITLE_FONT);
        header.add(lblPageTitle, new AbsoluteConstraints(540, 18, -1, -1));

        return header;
    }
}
