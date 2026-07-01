/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import form.component.SidebarPanel;
import form.util.FormNavigator;
import form.util.SwingStyleHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import koneksi.Koneksi;

/**
 * Dashboard modern untuk StockMate.
 */
public class Dashboard extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(Dashboard.class.getName());
    private static String loggedInUser = "Admin";

    private JTable tblBarang;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JLabel lblTitle;
    private JLabel lblUser;
    private JLabel lblTotalBarang;
    private JLabel lblTotalStok;
    private JLabel lblBarangMasuk;
    private JLabel lblBarangKeluar;
    private JLabel lblStokHampirHabis;

    public Dashboard() {
        initUI();
        loadDashboardStats();
        loadBarangData();
    }

    public static void setLoggedInUser(String username) {
        loggedInUser = (username == null || username.trim().isEmpty()) ? "Admin" : username.trim();
    }

    private void initUI() {
        setTitle("StockMate Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 900));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SwingStyleHelper.BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(SwingStyleHelper.BACKGROUND);
        body.add(createSidebar(), BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(SwingStyleHelper.BACKGROUND);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.add(createSummaryPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        body.add(content, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SwingStyleHelper.BORDER));
        header.setPreferredSize(new Dimension(0, 76));
        header.setMinimumSize(new Dimension(0, 76));

        JLabel logo = new JLabel("STOCKMATE");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(SwingStyleHelper.SECONDARY);
        logo.setBorder(new EmptyBorder(0, 24, 0, 0));
        logo.setIcon(createDashboardBrandIcon());
        logo.setIconTextGap(12);
        header.add(logo, BorderLayout.WEST);

        lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(SwingStyleHelper.TITLE_FONT);
        lblTitle.setForeground(new Color(15, 23, 42));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(lblTitle, BorderLayout.CENTER);

        lblUser = new JLabel("User: " + loggedInUser);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(new Color(71, 85, 105));
        lblUser.setBorder(new EmptyBorder(0, 0, 0, 24));
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        return new SidebarPanel(this, SidebarPanel.ActiveMenu.DASHBOARD);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 16, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        panel.add(createCardPanel("Total Barang", lblTotalBarang = new JLabel(), new Color(37, 99, 235), createStatIcon(StatIconType.BOX)));
        panel.add(createCardPanel("Total Stok", lblTotalStok = new JLabel(), new Color(14, 116, 144), createStatIcon(StatIconType.SHELF)));
        panel.add(createCardPanel("Barang Masuk Hari Ini", lblBarangMasuk = new JLabel(), new Color(22, 163, 74), createStatIcon(StatIconType.ARROW_IN)));
        panel.add(createCardPanel("Barang Keluar Hari Ini", lblBarangKeluar = new JLabel(), new Color(249, 115, 22), createStatIcon(StatIconType.ARROW_OUT)));
        panel.add(createCardPanel("Stok Hampir Habis", lblStokHampirHabis = new JLabel(), new Color(220, 38, 38), createStatIcon(StatIconType.WARNING)));

        return panel;
    }

    private JPanel createCardPanel(String title, JLabel valueLabel, Color accent, ImageIcon icon) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(SwingStyleHelper.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SwingStyleHelper.BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 116, 139));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        topRow.add(iconLabel, BorderLayout.WEST);
        topRow.add(titleLabel, BorderLayout.CENTER);
        card.add(topRow, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accent);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SwingStyleHelper.CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SwingStyleHelper.BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel sectionTitle = new JLabel("Daftar Barang");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(15, 23, 42));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        panel.add(sectionTitle, BorderLayout.NORTH);

        tblBarang = new JTable();
        SwingStyleHelper.styleTable(tblBarang);

        javax.swing.JTextField searchField = new javax.swing.JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        searchField.setPreferredSize(new Dimension(0, 34));
        searchField.setToolTipText("Cari kode atau nama barang");

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateFilter() {
                String text = searchField.getText().trim();
                if (rowSorter == null) {
                    return;
                }
                rowSorter.setRowFilter(text.isEmpty()
                        ? null
                        : RowFilter.regexFilter("(?i)" + Pattern.quote(text), 0, 1));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblBarang);
        scrollPane.setColumnHeaderView(searchField);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private int hitungTotalBarang() {
        String sql = "SELECT COUNT(*) AS total_barang FROM barang";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_barang");
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menghitung total barang", e);
        }
        return 0;
    }

    private int hitungTotalStok() {
        String sql = "SELECT COALESCE(SUM(stok), 0) AS total_stok FROM barang";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_stok");
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menghitung total stok", e);
        }
        return 0;
    }

    private int hitungBarangMasukHariIni() {
        String sql = "SELECT COUNT(*) AS masuk_hari_ini FROM transaksi WHERE DATE(tanggal) = CURDATE() AND LOWER(jenis) = 'barang masuk'";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("masuk_hari_ini");
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menghitung barang masuk hari ini", e);
        }
        return 0;
    }

    private int hitungBarangKeluarHariIni() {
        String sql = "SELECT COUNT(*) AS keluar_hari_ini FROM transaksi WHERE DATE(tanggal) = CURDATE() AND LOWER(jenis) = 'barang keluar'";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("keluar_hari_ini");
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menghitung barang keluar hari ini", e);
        }
        return 0;
    }

    private int hitungStokHampirHabis() {
        String sql = "SELECT COUNT(*) AS hampir_habis FROM barang WHERE stok > 0 AND stok <= 5";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hampir_habis");
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menghitung stok hampir habis", e);
        }
        return 0;
    }

    private void loadDashboardStats() {
        int totalBarang = hitungTotalBarang();
        int totalStok = hitungTotalStok();
        int barangMasukHariIni = hitungBarangMasukHariIni();
        int barangKeluarHariIni = hitungBarangKeluarHariIni();
        int stokHampirHabis = hitungStokHampirHabis();

        lblTotalBarang.setText(String.valueOf(totalBarang));
        lblTotalStok.setText(String.valueOf(totalStok));
        lblBarangMasuk.setText(String.valueOf(barangMasukHariIni));
        lblBarangKeluar.setText(String.valueOf(barangKeluarHariIni));
        lblStokHampirHabis.setText(String.valueOf(stokHampirHabis));
    }

    private void loadBarangData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Kode Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Stok");
        model.addColumn("Harga");

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("SELECT kode_barang, nama_barang, stok, harga FROM barang ORDER BY nama_barang")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("stok"),
                        rs.getDouble("harga")
                    });
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal memuat data barang", e);
        }

        tblBarang.setModel(model);
        rowSorter = new TableRowSorter<>(model);
        tblBarang.setRowSorter(rowSorter);
    }

    private enum StatIconType {
        BOX, SHELF, ARROW_IN, ARROW_OUT, WARNING
    }

    private ImageIcon createStatIcon(StatIconType type) {
        int size = 30;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(59, 130, 246));

        switch (type) {
            case BOX -> {
                g.fill(new RoundRectangle2D.Float(6, 6, 18, 18, 6, 6));
                g.setColor(Color.WHITE);
                g.draw(new Line2D.Float(6, 15, 24, 15));
                g.draw(new Line2D.Float(15, 6, 15, 24));
            }
            case SHELF -> {
                g.fill(new RoundRectangle2D.Float(4, 6, 22, 18, 6, 6));
                g.setColor(Color.WHITE);
                g.fill(new RoundRectangle2D.Float(8, 10, 14, 3, 3, 3));
                g.fill(new RoundRectangle2D.Float(8, 15, 14, 3, 3, 3));
            }
            case ARROW_IN -> {
                g.fill(new RoundRectangle2D.Float(8, 6, 14, 18, 6, 6));
                g.setColor(Color.WHITE);
                g.fill(new Polygon(new int[]{8, 20, 12}, new int[]{12, 12, 18}, 3));
            }
            case ARROW_OUT -> {
                g.fill(new RoundRectangle2D.Float(8, 6, 14, 18, 6, 6));
                g.setColor(Color.WHITE);
                g.fill(new Polygon(new int[]{8, 20, 14}, new int[]{12, 12, 6}, 3));
            }
            case WARNING -> {
                g.setColor(new Color(249, 115, 22));
                g.fill(new RoundRectangle2D.Float(6, 6, 18, 18, 6, 6));
                g.setColor(Color.WHITE);
                g.fill(new Polygon(new int[]{15, 18, 12}, new int[]{8, 20, 20}, 3));
                g.fillRect(14, 12, 2, 5);
            }
        }

        g.dispose();
        return new ImageIcon(image);
    }

    private ImageIcon createDashboardBrandIcon() {
        int size = 28;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(37, 99, 235));
        g.fill(new RoundRectangle2D.Float(4, 8, 20, 12, 8, 8));
        g.setColor(new Color(255, 255, 255));
        g.fill(new RoundRectangle2D.Float(8, 4, 12, 20, 6, 6));
        g.dispose();
        return new ImageIcon(image);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
