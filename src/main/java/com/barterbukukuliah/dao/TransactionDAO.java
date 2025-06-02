package com.barterbukukuliah.dao;

import com.barterbukukuliah.model.TransaksiBarter;
import com.barterbukukuliah.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Simpan transaksi barter baru dengan matchScore.
     */
    public boolean createTransaction(int idBukuDiminta, int idBukuDitawarkan,
                                     int idUserPengaju, int idUserPemberi,
                                     String pesanPengaju, double matchScore) throws SQLException {
        String sql = "INSERT INTO transactions " +
                "(id_buku_diminta, id_buku_ditawarkan, id_user_pengaju, id_user_pemberi, " +
                "status_transaksi, pesan_pengaju, match_score) " +
                "VALUES (?, ?, ?, ?, 'Pending', ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBukuDiminta);
            stmt.setInt(2, idBukuDitawarkan);
            stmt.setInt(3, idUserPengaju);
            stmt.setInt(4, idUserPemberi);
            stmt.setString(5, pesanPengaju);
            stmt.setDouble(6, matchScore);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Update status transaksi barter dan pesan balasan, sekaligus timestamp response.
     * @param idTransaksi ID transaksi
     * @param newStatus Status baru ('Diterima' atau 'Ditolak')
     * @param pesanBalasan Pesan opsional dari pemilik
     * @return true jika update sukses
     * @throws SQLException
     */
    public boolean updateStatus(int idTransaksi, String newStatus, String pesanBalasan) throws SQLException {
        String sql = "UPDATE transactions SET status_transaksi = ?, pesan_balasan = ?, timestamp_response = NOW() WHERE id_transaksi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, pesanBalasan);
            stmt.setInt(3, idTransaksi);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Tambahan method untuk dapatkan daftar transaksi Pending untuk user pemberi
    public List<TransaksiBarter> getPendingTransactionsByUser(int idUserPemberi) throws SQLException {
    List<TransaksiBarter> list = new ArrayList<>();

    String sql = "SELECT t.*, " +
                 "bd.judul AS judul_buku_diminta, " +
                 "bt.judul AS judul_buku_ditawarkan, " +
                 "u.nama AS nama_pengaju " +
                 "FROM transactions t " +
                 "JOIN books bd ON t.id_buku_diminta = bd.id_buku " +
                 "JOIN books bt ON t.id_buku_ditawarkan = bt.id_buku " +
                 "JOIN users u ON t.id_user_pengaju = u.id_user " +
                 "WHERE t.id_user_pemberi = ? AND t.status_transaksi = 'Pending' " +
                 "ORDER BY t.timestamp_request DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idUserPemberi);
        var rs = stmt.executeQuery();

        while (rs.next()) {
            TransaksiBarter t = new TransaksiBarter();
            t.setIdTransaksi(rs.getInt("id_transaksi"));
            t.setIdUserPengaju(rs.getInt("id_user_pengaju"));
            t.setIdUserPemberi(rs.getInt("id_user_pemberi"));
            t.setIdBukuDiminta(rs.getInt("id_buku_diminta"));
            t.setIdBukuDitawarkan(rs.getInt("id_buku_ditawarkan"));
            t.setStatus(rs.getString("status_transaksi"));
            t.setPesanPengaju(rs.getString("pesan_pengaju"));
            t.setPesanBalasan(rs.getString("pesan_balasan"));
            t.setMatchScore(rs.getDouble("match_score"));
            t.setTimestampRequest(rs.getTimestamp("timestamp_request"));

            t.setJudulBukuDiminta(rs.getString("judul_buku_diminta"));
            t.setJudulBukuDitawarkan(rs.getString("judul_buku_ditawarkan"));
            t.setNamaPengaju(rs.getString("nama_pengaju"));

            list.add(t);
        }
    }

    return list;
}

}
