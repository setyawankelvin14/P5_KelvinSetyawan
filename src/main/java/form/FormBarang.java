/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import koneksi.Koneksi;

/**
 * Form data barang yang modern, tetap mempertahankan CRUD dan koneksi database.
 */
public class FormBarang extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(FormBarang.class.getName());

    private JTextField txtKode;
    private JTextField txtNama;
    private JTextField txtStok;
    private JTextField txtHarga;
    private JTextField txtSearch;
    private JTable tblBarang;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JButton btnSimpan;
    private JButton btnEdit;
    private JButton btnHapus;
    private JButton btnReset;

    public FormBarang() {
        initUI();
        applyModernTheme();
        loadBarangData();
    }

    private void initUI() {
        setTitle("StockMate - Data Barang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 900));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));
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

        JLabel title = new JLabel("Data Barang");
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
        return new SidebarPanel(this, SidebarPanel.ActiveMenu.BARANG);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout());
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
        txtKode = new JTextField();
        formFields.add(txtKode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Nama Barang"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNama = new JTextField();
        formFields.add(txtNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Stok"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtStok = new JTextField();
        formFields.add(txtStok, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formFields.add(createLabel("Harga"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtHarga = new JTextField();
        formFields.add(txtHarga, gbc);

        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        btnSimpan = createActionButton("Simpan", SwingStyleHelper.SECONDARY, Color.WHITE);
        btnSimpan.addActionListener(evt -> simpanBarang());
        btnEdit = createActionButton("Edit", new Color(14, 116, 144), Color.WHITE);
        btnEdit.addActionListener(evt -> editBarang());
        btnHapus = createActionButton("Hapus", new Color(220, 38, 38), Color.WHITE);
        btnHapus.addActionListener(evt -> hapusBarang());
        btnReset = createActionButton("Reset", new Color(241, 245, 249), new Color(51, 65, 85));
        btnReset.addActionListener(evt -> clearForm());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JPanel searchPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search"));
        txtSearch = new JTextField(20);
        txtSearch.addActionListener(evt -> cariBarang());
        searchPanel.add(txtSearch);
        JButton btnSearch = createActionButton("Search", SwingStyleHelper.SECONDARY, Color.WHITE);
        btnSearch.addActionListener(evt -> cariBarang());
        searchPanel.add(btnSearch);
        JButton btnRefresh = createActionButton("Refresh", new Color(15, 118, 110), Color.WHITE);
        btnRefresh.addActionListener(evt -> loadBarangData());
        searchPanel.add(btnRefresh);
        topBar.add(searchPanel, BorderLayout.WEST);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formFields, BorderLayout.CENTER);
        formWrapper.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(topBar, BorderLayout.NORTH);
        formCard.add(formWrapper, BorderLayout.CENTER);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(SwingStyleHelper.CARD);
        tableCard.setBorder(SwingStyleHelper.sectionBorder());

        JLabel tableTitle = new JLabel("Daftar Barang");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(15, 23, 42));
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        tblBarang = new JTable();
        SwingStyleHelper.styleTable(tblBarang);
        tblBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                isiFormDariTabel();
            }
        });
        JScrollPane scroll = new JScrollPane(tblBarang);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scroll, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setOpaque(false);
        mainPanel.add(formCard, BorderLayout.NORTH);
        mainPanel.add(tableCard, BorderLayout.CENTER);
        content.add(mainPanel, BorderLayout.CENTER);

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
        getContentPane().setBackground(new Color(245, 247, 250));
        SwingStyleHelper.styleTextField(txtKode);
        SwingStyleHelper.styleTextField(txtNama);
        SwingStyleHelper.styleTextField(txtStok);
        SwingStyleHelper.styleTextField(txtHarga);
        SwingStyleHelper.styleTextField(txtSearch);
    }

    private void loadBarangData() {
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Kode Barang", "Nama Barang", "Stok", "Harga"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("SELECT kode_barang, nama_barang, stok, harga FROM barang ORDER BY nama_barang")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("stok"),
                        rs.getDouble("harga")
                    });
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal memuat data barang", e);
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang dari database.");
        }

        tblBarang.setModel(tableModel);
        tableSorter = new TableRowSorter<>(tableModel);
        tblBarang.setRowSorter(tableSorter);
        cariBarang();
    }

    private void cariBarang() {
        if (tableSorter == null) {
            return;
        }
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            tableSorter.setRowFilter(null);
        } else {
            tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword)));
        }
    }

    private void clearForm() {
        txtKode.setText("");
        txtNama.setText("");
        txtStok.setText("");
        txtHarga.setText("");
        txtKode.requestFocus();
    }

    private boolean validasiInput() {
        if (txtKode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode barang wajib diisi.");
            txtKode.requestFocus();
            return false;
        }
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang wajib diisi.");
            txtNama.requestFocus();
            return false;
        }
        try {
            int stok = Integer.parseInt(txtStok.getText().trim());
            if (stok < 0) {
                JOptionPane.showMessageDialog(this, "Stok tidak boleh minus.");
                txtStok.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stok harus berupa angka bulat.");
            txtStok.requestFocus();
            return false;
        }
        try {
            double harga = Double.parseDouble(txtHarga.getText().trim());
            if (harga <= 0) {
                JOptionPane.showMessageDialog(this, "Harga harus lebih dari 0.");
                txtHarga.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka.");
            txtHarga.requestFocus();
            return false;
        }
        return true;
    }

    private void simpanBarang() {
        if (!validasiInput()) {
            return;
        }

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("INSERT INTO barang (kode_barang, nama_barang, stok, harga) VALUES (?, ?, ?, ?)")) {
            pst.setString(1, txtKode.getText().trim());
            pst.setString(2, txtNama.getText().trim());
            pst.setInt(3, Integer.parseInt(txtStok.getText().trim()));
            pst.setDouble(4, Double.parseDouble(txtHarga.getText().trim()));
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
            loadBarangData();
            clearForm();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menyimpan data barang", e);
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data barang.");
        }
    }

    private void editBarang() {
        if (!validasiInput()) {
            return;
        }

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("UPDATE barang SET nama_barang=?, stok=?, harga=? WHERE kode_barang=?")) {
            pst.setString(1, txtNama.getText().trim());
            pst.setInt(2, Integer.parseInt(txtStok.getText().trim()));
            pst.setDouble(3, Double.parseDouble(txtHarga.getText().trim()));
            pst.setString(4, txtKode.getText().trim());

            int hasil = pst.executeUpdate();
            if (hasil > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.");
                loadBarangData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal mengubah data barang", e);
            JOptionPane.showMessageDialog(this, "Gagal mengubah data barang.");
        }
    }

    private void hapusBarang() {
        if (txtKode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu sebelum hapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus barang ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement("DELETE FROM barang WHERE kode_barang=?")) {
            pst.setString(1, txtKode.getText().trim());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            loadBarangData();
            clearForm();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal menghapus data barang", e);
            JOptionPane.showMessageDialog(this, "Gagal menghapus data barang.");
        }
    }

    private void isiFormDariTabel() {
        int selectedRow = tblBarang.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        selectedRow = tblBarang.convertRowIndexToModel(selectedRow);
        txtKode.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNama.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtStok.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtHarga.setText(tableModel.getValueAt(selectedRow, 3).toString());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormBarang().setVisible(true));
    }
}

