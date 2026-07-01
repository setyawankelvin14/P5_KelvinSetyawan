package form;

import form.component.SidebarPanel;
import form.util.SwingStyleHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

public class FormTransaksi extends JFrame {

    private static final Logger logger = Logger.getLogger(FormTransaksi.class.getName());

    private JComboBox<String> cbKodeBarang;
    private JTextField txtNamaBarang;
    private JComboBox<String> cbJenis;
    private JTextField txtJumlah;
    private JTextField txtTanggal;
    private JTable tblHistory;
    private DefaultTableModel tableModel;

    public FormTransaksi() {
        initUI();
        applyModernTheme();
        loadBarangKode();
        loadHistory();
    }

    private void initUI() {
        setTitle("StockMate - Transaksi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 900));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SwingStyleHelper.BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(new Color(245, 247, 250));
        body.add(createSidebar(), BorderLayout.WEST);
        body.add(createContentPanel(), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 76));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel logo = new JLabel("STOCKMATE");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(SwingStyleHelper.SECONDARY);
        logo.setBorder(new EmptyBorder(0, 24, 0, 0));
        header.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel("Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(15, 23, 42));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);

        JLabel user = new JLabel("User: Admin");
        user.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        user.setForeground(new Color(71, 85, 105));
        user.setBorder(new EmptyBorder(0, 0, 0, 24));
        header.add(user, BorderLayout.EAST);
        return header;
    }

    private JPanel createSidebar() {
        return new SidebarPanel(this, SidebarPanel.ActiveMenu.TRANSAKSI);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(SwingStyleHelper.BACKGROUND);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(SwingStyleHelper.CARD);
        formCard.setBorder(SwingStyleHelper.cardBorder());

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formFields.add(createLabel("Kode Barang"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cbKodeBarang = new JComboBox<>();
        cbKodeBarang.addActionListener(evt -> autoFillNamaBarang());
        formFields.add(cbKodeBarang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Nama Barang"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNamaBarang = new JTextField();
        txtNamaBarang.setEditable(false);
        formFields.add(txtNamaBarang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Jenis Transaksi"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cbJenis = new JComboBox<>(new String[]{"Barang Masuk", "Barang Keluar"});
        formFields.add(cbJenis, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Jumlah"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtJumlah = new JTextField();
        formFields.add(txtJumlah, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Tanggal"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTanggal = new JTextField(LocalDate.now().toString());
        formFields.add(txtTanggal, gbc);

        JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        JButton btnSimpan = createActionButton("Simpan", SwingStyleHelper.SECONDARY, Color.WHITE);
        btnSimpan.addActionListener(evt -> simpanTransaksi());
        JButton btnReset = createActionButton("Reset", new Color(241, 245, 249), new Color(51, 65, 85));
        btnReset.addActionListener(evt -> resetForm());
        buttons.add(btnSimpan);
        buttons.add(btnReset);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formFields, BorderLayout.CENTER);
        topPanel.add(buttons, BorderLayout.SOUTH);
        formCard.add(topPanel, BorderLayout.CENTER);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(SwingStyleHelper.CARD);
        tableCard.setBorder(SwingStyleHelper.sectionBorder());

        JLabel tableTitle = new JLabel("History Transaksi");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(15, 23, 42));
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        tblHistory = new JTable();
        SwingStyleHelper.styleTable(tblHistory);
        JScrollPane scroll = new JScrollPane(tblHistory);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(formCard, BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        return content;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(71, 85, 105));
        return label;
    }

    private JButton createActionButton(String text, Color bg, Color fg) {
        return SwingStyleHelper.createRoundedButton(text, bg, fg);
    }

    private void applyModernTheme() {
        cbKodeBarang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        SwingStyleHelper.styleTextField(txtNamaBarang);
        cbJenis.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        SwingStyleHelper.styleTextField(txtJumlah);
        SwingStyleHelper.styleTextField(txtTanggal);
    }

    private void loadBarangKode() {
        cbKodeBarang.removeAllItems();
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("SELECT kode_barang FROM barang ORDER BY kode_barang")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    cbKodeBarang.addItem(rs.getString("kode_barang"));
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal memuat kode barang", e);
        }

        if (cbKodeBarang.getItemCount() > 0) {
            cbKodeBarang.setSelectedIndex(0);
            autoFillNamaBarang();
        }
    }

    private void autoFillNamaBarang() {
        Object selected = cbKodeBarang.getSelectedItem();
        if (selected == null) {
            txtNamaBarang.setText("");
            return;
        }
        String kode = selected.toString();
        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("SELECT nama_barang FROM barang WHERE kode_barang=?")) {
            pst.setString(1, kode);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    txtNamaBarang.setText(rs.getString("nama_barang"));
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal mengambil nama barang", e);
        }
    }

    private void simpanTransaksi() {
        if (!validasiInput()) {
            return;
        }

        String kodeBarang = cbKodeBarang.getSelectedItem().toString();
        String jenis = cbJenis.getSelectedItem().toString();
        int jumlah = Integer.parseInt(txtJumlah.getText().trim());
        String tanggal = txtTanggal.getText().trim();

        try (Connection conn = Koneksi.getConnection()) {
            int stokSaatIni = 0;
            try (PreparedStatement pst = conn.prepareStatement("SELECT stok FROM barang WHERE kode_barang=?")) {
                pst.setString(1, kodeBarang);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        stokSaatIni = rs.getInt("stok");
                    }
                }
            }

            if ("Barang Keluar".equals(jenis) && stokSaatIni < jumlah) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi untuk barang keluar.");
                return;
            }

            int stokBaru = "Barang Masuk".equals(jenis) ? stokSaatIni + jumlah : stokSaatIni - jumlah;

            try (PreparedStatement updateStok = conn.prepareStatement("UPDATE barang SET stok=? WHERE kode_barang=?")) {
                updateStok.setInt(1, stokBaru);
                updateStok.setString(2, kodeBarang);
                updateStok.executeUpdate();
            }

            try (PreparedStatement insertTransaksi = conn.prepareStatement("INSERT INTO transaksi (kode_barang, nama_barang, jenis, jumlah, tanggal) VALUES (?, ?, ?, ?, ?)")) {
                insertTransaksi.setString(1, kodeBarang);
                insertTransaksi.setString(2, txtNamaBarang.getText().trim());
                insertTransaksi.setString(3, jenis);
                insertTransaksi.setInt(4, jumlah);
                insertTransaksi.setString(5, tanggal);
                insertTransaksi.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan.");
            resetForm();
            loadHistory();
            loadBarangKode();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menyimpan transaksi", e);
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi.");
        }
    }

    private boolean validasiInput() {
        if (cbKodeBarang.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih kode barang terlebih dahulu.");
            return false;
        }
        if (txtJumlah.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah wajib diisi.");
            return false;
        }
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka bulat.");
            return false;
        }
        if (txtTanggal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal wajib diisi.");
            return false;
        }
        return true;
    }

    private void resetForm() {
        if (cbKodeBarang.getItemCount() > 0) {
            cbKodeBarang.setSelectedIndex(0);
            autoFillNamaBarang();
        }
        cbJenis.setSelectedIndex(0);
        txtJumlah.setText("");
        txtTanggal.setText(LocalDate.now().toString());
    }

    private void loadHistory() {
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Kode Barang", "Nama Barang", "Jenis", "Jumlah", "Tanggal"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("SELECT kode_barang, nama_barang, jenis, jumlah, tanggal FROM transaksi ORDER BY tanggal DESC, id DESC")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("jenis"),
                        rs.getInt("jumlah"),
                        rs.getString("tanggal")
                    });
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal memuat history transaksi", e);
        }

        tblHistory.setModel(tableModel);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormTransaksi().setVisible(true));
    }
}
