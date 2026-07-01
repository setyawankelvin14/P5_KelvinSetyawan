package form.util;

import form.Dashboard;
import form.FormBarang;
import form.FormLogin;
import form.FormLaporan;
import form.FormTransaksi;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class FormNavigator {

    private FormNavigator() {
    }

    public static void openDashboard(JFrame current) {
        if (current instanceof Dashboard) {
            return;
        }
        new Dashboard().setVisible(true);
        current.dispose();
    }

    public static void openBarang(JFrame current) {
        if (current instanceof FormBarang) {
            return;
        }
        new FormBarang().setVisible(true);
        current.dispose();
    }

    public static void openTransaksi(JFrame current) {
        if (current instanceof FormTransaksi) {
            return;
        }
        new FormTransaksi().setVisible(true);
        current.dispose();
    }

    public static void openLaporan(JFrame current) {
        if (current instanceof FormLaporan) {
            return;
        }
        new FormLaporan().setVisible(true);
        current.dispose();
    }

    public static void logout(JFrame current) {
        int pilih = JOptionPane.showConfirmDialog(
                current,
                "Yakin ingin logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (pilih == JOptionPane.YES_OPTION) {
            new FormLogin().setVisible(true);
            current.dispose();
        }
    }
}
