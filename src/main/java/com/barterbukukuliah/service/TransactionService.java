// dikialiftaufik/tubes-pbo/tubes-pbo-114b958adca147bc9ecad8a39ec42eb28db2797e/src/main/java/com/barterbukukuliah/service/TransactionService.java
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
import com.barterbukukuliah.dao.TrustScoreHistoryDAO; // Pastikan import ini ada


public class TransactionService {

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final TrustScoreHistoryDAO trustScoreHistoryDAO = new TrustScoreHistoryDAO();

    private static final double TRUST_SCORE_INCREMENT_ON_ACCEPT = 0.5;
    private static final double MAX_TRUST_SCORE = 9.99;


    public boolean requestBarter(int idBukuDiminta, int idBukuDitawarkan,
                                 int idUserPengaju, String pesan) throws Exception {
        Book bukuDiminta = bookDAO.findById(idBukuDiminta);
        Book bukuDitawarkan = bookDAO.findById(idBukuDitawarkan);

        // Validasi null untuk buku
        if (bukuDiminta == null || bukuDitawarkan == null) {
            throw new Exception("Satu atau kedua buku tidak ditemukan.");
        }

        User pengaju = userDAO.findById(idUserPengaju);
        // Pemilik buku yang diminta adalah 'Pemberi'
        User pemberi = userDAO.findById(bukuDiminta.getIdPemilik());

        // Validasi null untuk user
        if (pengaju == null || pemberi == null) {
            throw new Exception("Pengaju atau pemilik buku tidak ditemukan.");
        }
        
        double matchScore = MatchingService.hitungMatchScore(bukuDiminta, bukuDitawarkan, pengaju, pemberi);

        return transactionDAO.createTransaction(idBukuDiminta, idBukuDitawarkan,
                idUserPengaju, pemberi.getIdUser(),
                pesan, matchScore);
    }

    public List<TransaksiBarter> getPendingRequests(int idUserPemberi) throws SQLException {
        return transactionDAO.getPendingTransactionsByUser(idUserPemberi);
    }

    public boolean respondToBarter(int idTransaksi, String status, String pesanBalasan,
                                   int idUserPengaju, int idBukuMilikPemberi, int idPemberi) throws SQLException {
        boolean updated = transactionDAO.updateStatus(idTransaksi, status, pesanBalasan);

        if (updated) {
            String judulNotif;
            String pesanNotif;

            if ("Diterima".equalsIgnoreCase(status)) {
                judulNotif = "Barter Anda Diterima";
                pesanNotif = "Permintaan barter Anda telah diterima oleh pemilik buku.";

                bookDAO.updateBookStatus(idBukuMilikPemberi, "Sedang Dipinjam");

                User pemberiUser = userDAO.findById(idPemberi); 
                if (pemberiUser != null) {
                    double oldScore = pemberiUser.getTrustScore();
                    double scoreChange = TRUST_SCORE_INCREMENT_ON_ACCEPT;
                    double newScore = Math.min(oldScore + scoreChange, MAX_TRUST_SCORE); 

                    if (newScore > oldScore) { 
                        userDAO.updateTrustScore(idPemberi, newScore); // Ini yang menyebabkan error sebelumnya
                        trustScoreHistoryDAO.addHistory(idPemberi, "BARTER_ACCEPTED_AS_OWNER",
                                scoreChange, oldScore, newScore, idTransaksi,
                                "Menerima permintaan barter untuk buku ID: " + idBukuMilikPemberi);
                    }
                }
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