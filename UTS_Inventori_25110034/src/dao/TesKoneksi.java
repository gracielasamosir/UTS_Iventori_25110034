package dao;

import java.sql.Connection;
import javax.swing.JOptionPane;

public class TesKoneksi {

    public static void main(String[] args) {

        Connection conn = Koneksi.getConnection();

        if (conn != null) {

            JOptionPane.showMessageDialog(
                null,
                "Koneksi ke database berhasil!"
            );

            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}