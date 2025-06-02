package com.barterbukukuliah.dao;

import com.barterbukukuliah.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationDAO {

    /**
     * Insert notifikasi baru untuk user.
     */
    public boolean insertNotification(int userId, String jenis, String judul, String pesan, Integer referenceId) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, jenis, judul, pesan, reference_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, jenis);
            stmt.setString(3, judul);
            stmt.setString(4, pesan);
            if (referenceId == null) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, referenceId);
            }

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
