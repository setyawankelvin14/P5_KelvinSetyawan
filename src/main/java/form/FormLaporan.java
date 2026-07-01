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
import javax.swing.JFileChooser;
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
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FormLaporan extends JFrame {

    private static final Logger logger = Logger.getLogger(FormLaporan.class.getName());

    private JTextField txtTanggalAwal;
    private JTextField txtTanggalAkhir;
    private JTable tblLaporan;
    private DefaultTableModel tableModel;

    public FormLaporan() {
        initUI();
        applyModernTheme();
        loadData();
    }

    private void initUI() {
        setTitle("StockMate - Laporan");
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

        JLabel title = new JLabel("Laporan");
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
        return new SidebarPanel(this, SidebarPanel.ActiveMenu.LAPORAN);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(SwingStyleHelper.BACKGROUND);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel filterCard = new JPanel(new BorderLayout());
        filterCard.setBackground(SwingStyleHelper.CARD);
        filterCard.setBorder(SwingStyleHelper.cardBorder());

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(createLabel("Tanggal Awal"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTanggalAwal = new JTextField(LocalDate.now().toString());
        filterPanel.add(txtTanggalAwal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        filterPanel.add(createLabel("Tanggal Akhir"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTanggalAkhir = new JTextField(LocalDate.now().toString());
        filterPanel.add(txtTanggalAkhir, gbc);

        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton btnTampilkan = createActionButton("Tampilkan", SwingStyleHelper.SECONDARY, Color.WHITE);
        btnTampilkan.addActionListener(evt -> tampilkanLaporan());
        JButton btnPdf = createActionButton("Cetak PDF", new Color(14, 116, 144), Color.WHITE);
        btnPdf.addActionListener(evt -> btnPdfActionPerformed(evt));
        JButton btnExcel = createActionButton("Export Excel", new Color(22, 163, 74), Color.WHITE);
        btnExcel.addActionListener(evt -> btnExcelActionPerformed(evt));
        buttonPanel.add(btnTampilkan);
        buttonPanel.add(btnPdf);
        buttonPanel.add(btnExcel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        filterCard.add(topPanel, BorderLayout.CENTER);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(SwingStyleHelper.CARD);
        tableCard.setBorder(SwingStyleHelper.sectionBorder());

        JLabel tableTitle = new JLabel("Data Transaksi");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(15, 23, 42));
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        tblLaporan = new JTable();
        SwingStyleHelper.styleTable(tblLaporan);
        JScrollPane scroll = new JScrollPane(tblLaporan);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(filterCard, BorderLayout.NORTH);
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
        SwingStyleHelper.styleTextField(txtTanggalAwal);
        SwingStyleHelper.styleTextField(txtTanggalAkhir);
    }

    private void loadData() {
        tampilkanLaporan();
    }

    private void tampilkanLaporan() {
        String awal = txtTanggalAwal.getText().trim();
        String akhir = txtTanggalAkhir.getText().trim();

        if (awal.isEmpty() || akhir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal awal dan akhir wajib diisi.");
            return;
        }

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Kode Barang", "Nama Barang", "Jenis", "Jumlah", "Tanggal"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT id, kode_barang, nama_barang, jenis, jumlah, tanggal FROM transaksi WHERE tanggal BETWEEN ? AND ? ORDER BY tanggal DESC, id DESC";

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, awal);
            pst.setString(2, akhir);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("jenis"),
                        rs.getInt("jumlah"),
                        rs.getString("tanggal")
                    });
                }
            }
            tblLaporan.setModel(tableModel);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal memuat laporan", e);
            JOptionPane.showMessageDialog(this, "Gagal memuat data laporan.");
        }
    }

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {
        exportPdf();
    }

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {
        exportExcel();
    }

    private void exportPdf() {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak.");
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String defaultName = "Laporan_StockMate_" + timestamp + ".pdf";

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan File PDF");
        chooser.setSelectedFile(new File(defaultName));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Paragraph title = new Paragraph("LAPORAN TRANSAKSI STOCKMATE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph printedAt = new Paragraph("Tanggal Cetak: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), FontFactory.getFont(FontFactory.HELVETICA, 11));
            printedAt.setSpacingBefore(8);
            printedAt.setAlignment(Element.ALIGN_CENTER);
            document.add(printedAt);

            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(tableModel.getColumnCount());
            pdfTable.setWidthPercentage(100);
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(tableModel.getColumnName(col), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(headerCell);
            }
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object value = tableModel.getValueAt(row, col);
                    PdfPCell cell = new PdfPCell(new Paragraph(value == null ? "" : value.toString(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                    pdfTable.addCell(cell);
                }
            }
            document.add(pdfTable);
            JOptionPane.showMessageDialog(this, "PDF berhasil disimpan ke:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Gagal membuat file PDF", e);
            JOptionPane.showMessageDialog(this, "Gagal membuat file PDF.");
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void exportExcel() {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan File Excel");
        chooser.setSelectedFile(new File("Laporan_StockMate.xlsx"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(); FileOutputStream outputStream = new FileOutputStream(file)) {
            XSSFSheet sheet = workbook.createSheet("Laporan");
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(tableModel.getColumnName(col));
            }
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Row excelRow = sheet.createRow(row + 1);
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Cell cell = excelRow.createCell(col);
                    Object value = tableModel.getValueAt(row, col);
                    cell.setCellValue(value == null ? "" : value.toString());
                }
            }
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
            }
            workbook.write(outputStream);
            JOptionPane.showMessageDialog(this, "File Excel berhasil diekspor ke:\n" + file.getAbsolutePath());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Gagal mengekspor file Excel", e);
            JOptionPane.showMessageDialog(this, "Gagal mengekspor file Excel.");
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormLaporan().setVisible(true));
    }
}
