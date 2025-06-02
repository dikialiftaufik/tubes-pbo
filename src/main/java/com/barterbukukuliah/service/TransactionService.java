package com.barterbukukuliah.service;

import java.sql.SQLException;
import java.util.List;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.dao.NotificationDAO;
import com.barterbukukuliah.dao.TransactionDAO;
import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.TransaksiBarter;
import com.barterbukukuliah.model.User;

public class TransactionService {

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();


    /**
     * Proses request barter: hitung match score, simpan transaksi.
     */
    public boolean requestBarter(int idBukuDiminta, int idBukuDitawarkan,
                                 int idUserPengaju, String pesan) throws Exception {

        Book bukuDiminta = bookDAO.findById(idBukuDiminta);
        Book bukuDitawarkan = bookDAO.findById(idBukuDitawarkan);

        User pengaju = userDAO.findById(idUserPengaju);
        User pemberi = userDAO.findById(bukuDitawarkan.getIdPemilik());

        double matchScore = MatchingService.hitungMatchScore(bukuDiminta, bukuDitawarkan, pengaju, pemberi);

        return transactionDAO.createTransaction(idBukuDiminta, idBukuDitawarkan,
                idUserPengaju, pemberi.getIdUser(),
                pesan, matchScore);
    }

    /**
     * Ambil list transaksi barter yang pending untuk user pemberi buku.
     */
    public List<TransaksiBarter> getPendingRequests(int idUserPemberi) throws SQLException {
        return transactionDAO.getPendingTransactionsByUser(idUserPemberi);
    }

    /**
     * Proses respon barter (terima atau tolak).
     * Update status dan kirim notifikasi ke pengaju.
     */
    public boolean respondToBarter(int idTransaksi, String status, String pesanBalasan, int idUserPengaju) throws SQLException {
        boolean updated = transactionDAO.updateStatus(idTransaksi, status, pesanBalasan);

        if (updated) {
            String judulNotif;
            String pesanNotif;

            if ("Diterima".equalsIgnoreCase(status)) {
                judulNotif = "Barter Anda Diterima";
                pesanNotif = "Permintaan barter Anda telah diterima oleh pemilik buku.";
            } else if ("Ditolak".equalsIgnoreCase(status)) {
                judulNotif = "Barter Anda Ditolak";
                pesanNotif = "Permintaan barter Anda ditolak oleh pemilik buku.";
            } else {
                judulNotif = "Status Barter Diperbarui";
                pesanNotif = "Status barter Anda diperbarui menjadi " + status;
            }

            notificationDAO.insertNotification(idUserPengaju, "Request Accepted", judulNotif, pesanNotif, idTransaksi);
        }

        return updated;
    }
}
