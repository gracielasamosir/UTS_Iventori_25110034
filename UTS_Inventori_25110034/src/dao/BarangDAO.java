
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Barang;

public class BarangDAO {

    // 1. SIMPAN BARANG
    public boolean simpanBarang(Barang barang) {

        String sql = "INSERT INTO barang "
                   + "(kode_barang, nama_barang, kategori, stok) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, barang.getKodeBarang());
                ps.setString(2, barang.getNamaBarang());
                ps.setString(3, barang.getKategori());
                ps.setInt(4, barang.getStok());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Gagal menyimpan: " + e.getMessage());
            return false;
        }
    }

    // 2. TAMPILKAN SEMUA BARANG
    public List<Barang> tampilkanBarang() {

        List<Barang> daftar = new ArrayList<>();

        String sql = "SELECT * FROM barang ORDER BY kode_barang";

        try (Connection conn = Koneksi.getConnection()) {

            if (conn == null) {
                return daftar;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Barang b = new Barang();

                    b.setKodeBarang(rs.getString("kode_barang"));
                    b.setNamaBarang(rs.getString("nama_barang"));
                    b.setKategori(rs.getString("kategori"));
                    b.setStok(rs.getInt("stok"));

                    daftar.add(b);
                }
            }

        } catch (SQLException e) {
            System.out.println("Gagal menampilkan: " + e.getMessage());
        }

        return daftar;
    }

    // 3. UBAH BARANG
    public boolean ubahBarang(Barang barang) {

        String sql = "UPDATE barang SET "
                   + "nama_barang=?, kategori=?, stok=? "
                   + "WHERE kode_barang=?";

        try (Connection conn = Koneksi.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, barang.getNamaBarang());
                ps.setString(2, barang.getKategori());
                ps.setInt(3, barang.getStok());
                ps.setString(4, barang.getKodeBarang());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengubah: " + e.getMessage());
            return false;
        }
    }

    // 4. HAPUS BARANG BERDASARKAN KODE
    public boolean hapusBarang(String kodeBarang) {

        String sql = "DELETE FROM barang WHERE kode_barang=?";

        try (Connection conn = Koneksi.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, kodeBarang);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Gagal menghapus: " + e.getMessage());
            return false;
        }
    }
}
