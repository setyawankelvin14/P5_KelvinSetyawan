package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;

public final class Koneksi {

    private static Connection conn;

    private Koneksi() {
    }

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/database_stockmate";
            String user = "root";
            String pass = "";

            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Koneksi Berhasil");
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }

        return conn;
    }
}
