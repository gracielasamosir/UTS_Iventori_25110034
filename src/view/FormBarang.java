
package view;

import dao.BarangDAO;
import model.Barang;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormBarang extends JFrame {

    // Komponen input
    private JTextField txtKode;
    private JTextField txtNama;
    private JTextField txtStok;

    private JComboBox<String> cmbKategori;

    // Tombol
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBersihkan;

    // Tabel
    private JTable tblBarang;
    private DefaultTableModel modelTabel;

    // DAO
    private BarangDAO dao = new BarangDAO();

    // Constructor
    public FormBarang() {

        setTitle("Sistem Informasi Inventori Barang");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buatTampilan();
        tampilkanData();

    }

    // MEMBUAT TAMPILAN FORM
    private void buatTampilan() {

        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));

        JLabel judul = new JLabel(
            "SISTEM INFORMASI INVENTORI BARANG",
            SwingConstants.CENTER
        );

        judul.setFont(new java.awt.Font(
            "Arial", java.awt.Font.BOLD, 20
        ));

        panelUtama.add(judul, BorderLayout.NORTH);

        // Panel input
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));

        txtKode = new JTextField();
        txtNama = new JTextField();
        txtStok = new JTextField();

        cmbKategori = new JComboBox<>(
            new String[] {
                "Komputer",
                "Jaringan",
                "Alat Ukur"
            }
        );

        panelInput.add(new JLabel("Kode Barang"));
        panelInput.add(txtKode);

        panelInput.add(new JLabel("Nama Barang"));
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Kategori"));
        panelInput.add(cmbKategori);

        panelInput.add(new JLabel("Stok"));
        panelInput.add(txtStok);

        // Panel tombol
        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnBersihkan = new JButton("Bersihkan");

        JPanel panelTombol = new JPanel(new FlowLayout());

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBersihkan);

        // Panel atas
        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));

        panelAtas.add(panelInput, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        panelUtama.add(panelAtas, BorderLayout.CENTER);

        // Membuat JTable
        modelTabel = new DefaultTableModel(
            new Object[] {
                "Kode Barang",
                "Nama Barang",
                "Kategori",
                "Stok"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBarang = new JTable(modelTabel);

        JScrollPane scrollPane = new JScrollPane(tblBarang);

        panelUtama.add(scrollPane, BorderLayout.SOUTH);

        // Atur ukuran bagian tabel
        scrollPane.setPreferredSize(
            new java.awt.Dimension(700, 220)
        );

        add(panelUtama);

        // EVENT TOMBOL
        btnSimpan.addActionListener(e -> simpanData());

        btnUbah.addActionListener(e -> ubahData());

        btnHapus.addActionListener(e -> hapusData());

        btnBersihkan.addActionListener(e -> bersihkanForm());

        // EVENT KLIK TABEL
        tblBarang.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int baris = tblBarang.getSelectedRow();

                if (baris != -1) {

                    txtKode.setText(
                        modelTabel.getValueAt(baris, 0).toString()
                    );

                    txtNama.setText(
                        modelTabel.getValueAt(baris, 1).toString()
                    );

                    cmbKategori.setSelectedItem(
                        modelTabel.getValueAt(baris, 2).toString()
                    );

                    txtStok.setText(
                        modelTabel.getValueAt(baris, 3).toString()
                    );

                    // Kode utama tidak diubah saat update
                    txtKode.setEditable(false);
                }
            }
        });
    }

    // VALIDASI INPUT
    private boolean validasiInput() {

        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String stok = txtStok.getText().trim();

        if (kode.isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Kode barang tidak boleh kosong!"
            );

            txtKode.requestFocus();
            return false;
        }

        if (kode.length() > 10) {

            JOptionPane.showMessageDialog(
                this, "Kode maksimal 10 karakter!"
            );

            return false;
        }

        if (nama.isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Nama barang tidak boleh kosong!"
            );

            txtNama.requestFocus();
            return false;
        }

        if (nama.length() > 50) {

            JOptionPane.showMessageDialog(
                this, "Nama maksimal 50 karakter!"
            );

            return false;
        }

        if (!stok.matches("[0-9]+")) {

            JOptionPane.showMessageDialog(
                this,
                "Stok harus berupa angka bulat dan tidak boleh negatif!"
            );

            txtStok.requestFocus();
            return false;
        }

        try {

            Integer.parseInt(stok);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Nilai stok terlalu besar!"
            );

            return false;
        }

        return true;
    }

    // 1. SIMPAN DATA
    private void simpanData() {

        if (!validasiInput()) {
            return;
        }

        Barang barang = new Barang(
            txtKode.getText().trim(),
            txtNama.getText().trim(),
            cmbKategori.getSelectedItem().toString(),
            Integer.parseInt(txtStok.getText().trim())
        );

        boolean berhasil = dao.simpanBarang(barang);

        if (berhasil) {

            JOptionPane.showMessageDialog(
                this, "Data barang berhasil disimpan!"
            );

            tampilkanData();
            bersihkanForm();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Data gagal disimpan!\n"
                + "Periksa kemungkinan kode sudah digunakan."
            );
        }
    }

    // 2. MENAMPILKAN DATA
    private void tampilkanData() {

        modelTabel.setRowCount(0);

        List<Barang> daftar = dao.tampilkanBarang();

        for (Barang b : daftar) {

            modelTabel.addRow(new Object[] {
                b.getKodeBarang(),
                b.getNamaBarang(),
                b.getKategori(),
                b.getStok()
            });
        }
    }

    // 3. UBAH DATA
    private void ubahData() {

        if (tblBarang.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(
                this, "Pilih data pada tabel terlebih dahulu!"
            );

            return;
        }

        if (!validasiInput()) {
            return;
        }

        Barang barang = new Barang(
            txtKode.getText().trim(),
            txtNama.getText().trim(),
            cmbKategori.getSelectedItem().toString(),
            Integer.parseInt(txtStok.getText().trim())
        );

        boolean berhasil = dao.ubahBarang(barang);

        if (berhasil) {

            JOptionPane.showMessageDialog(
                this, "Data barang berhasil diubah!"
            );

            tampilkanData();
            bersihkanForm();

        } else {

            JOptionPane.showMessageDialog(
                this, "Data gagal diubah!"
            );
        }
    }

    // 4. HAPUS DATA
    private void hapusData() {

        if (tblBarang.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(
                this, "Pilih data yang ingin dihapus!"
            );

            return;
        }

        String kode = txtKode.getText().trim();

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus barang "
            + kode + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {

            boolean berhasil = dao.hapusBarang(kode);

            if (berhasil) {

                JOptionPane.showMessageDialog(
                    this, "Data berhasil dihapus!"
                );

                tampilkanData();
                bersihkanForm();

            } else {

                JOptionPane.showMessageDialog(
                    this, "Data gagal dihapus!"
                );
            }
        }
    }

    // 5. BERSIHKAN FORM
    private void bersihkanForm() {

        txtKode.setText("");
        txtNama.setText("");
        txtStok.setText("");

        cmbKategori.setSelectedIndex(0);

        txtKode.setEditable(true);

        tblBarang.clearSelection();

        txtKode.requestFocus();
    }

    // MAIN PROGRAM
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new FormBarang().setVisible(true);

        });
    }
}
