
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {

    private static final String URL =
        "jdbc:mysql://localhost:3306/db_inventori?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                URL, USER, PASSWORD
            );

            return conn;

        } catch (ClassNotFoundException | SQLException e) {

            JOptionPane.showMessageDialog(
                null,
                "Koneksi database gagal!\n" + e.getMessage(),
                "Kesalahan Koneksi",
                JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }
}
