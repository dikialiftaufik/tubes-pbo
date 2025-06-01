package com.barterbukukuliah.dao;

import com.barterbukukuliah.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data Access Object untuk tabel transactions.
 */
public class TransactionDAO {

    /**
     * Buat transaksi baru dengan status 'Pending'.
     *
     * @param idBukuDiminta   ID buku yang sedang dicari (target)
     * @param idBukuDitawarkan ID buku yang ditawarkan user
     * @param idUserPengaju   ID currentUser (pengaju barter)
     * @param idUserPemberi   ID pemilik buku target
     * @param pesanPengaju    Pesan optional dari pengaju
     * @throws SQLException jika gagal ke DB
     */
    public void createTransaction(int idBukuDiminta,
                                  int idBukuDitawarkan,
                                  int idUserPengaju,
                                  int idUserPemberi,
                                  String pesanPengaju) throws SQLException {

        String sql = "INSERT INTO transactions " +
                     "(id_buku_diminta, id_buku_ditawarkan, id_user_pengaju, id_user_pemberi, status_transaksi, pesan_pengaju) " +
                     "VALUES (?, ?, ?, ?, 'Pending', ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBukuDiminta);
            stmt.setInt(2, idBukuDitawarkan);
            stmt.setInt(3, idUserPengaju);
            stmt.setInt(4, idUserPemberi);
            stmt.setString(5, pesanPengaju);
            stmt.executeUpdate();
        }
    }
}
