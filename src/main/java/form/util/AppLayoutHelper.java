package form.util;

import form.component.AppHeaderPanel;
import form.component.SidebarPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public final class AppLayoutHelper {

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);

    private AppLayoutHelper() {
    }

    public static void applyMainLayout(JFrame frame, String pageTitle, SidebarPanel.ActiveMenu activeMenu) {
        Container contentPane = frame.getContentPane();
        JPanel formPanel = new JPanel(new AbsoluteLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        Component[] components = contentPane.getComponents();
        for (Component component : components) {
            contentPane.remove(component);
            Rectangle bounds = component.getBounds();
            formPanel.add(component, new AbsoluteConstraints(
                    bounds.x,
                    bounds.y,
                    bounds.width,
                    bounds.height
            ));
        }

        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(BACKGROUND_COLOR);
        contentPane.add(AppHeaderPanel.create(pageTitle), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BACKGROUND_COLOR);
        body.add(new SidebarPanel(frame, activeMenu), BorderLayout.WEST);
        body.add(formPanel, BorderLayout.CENTER);
        contentPane.add(body, BorderLayout.CENTER);
    }
}
