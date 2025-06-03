// src/main/java/com/barterbukukuliah/dao/TrustScoreHistoryDAO.java
package com.barterbukukuliah.dao;

import com.barterbukukuliah.model.TrustScoreHistory; // Import model
import com.barterbukukuliah.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Import ResultSet
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList; // Import ArrayList
import java.util.List;    // Import List

public class TrustScoreHistoryDAO {

    public void addHistory(int userId, String actionType, double scoreChange,
                           double oldScore, double newScore, Integer referenceId,
                           String keterangan) throws SQLException {
        String sql = "INSERT INTO trust_score_history " +
                     "(user_id, action_type, score_change, old_score, new_score, reference_id, keterangan) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, actionType);
            stmt.setDouble(3, scoreChange);
            stmt.setDouble(4, oldScore);
            stmt.setDouble(5, newScore);
            if (referenceId != null) {
                stmt.setInt(6, referenceId);
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setString(7, keterangan);
            stmt.executeUpdate();
        }
    }

    public List<TrustScoreHistory> getHistoryForUser(int userId) throws SQLException {
        List<TrustScoreHistory> historyList = new ArrayList<>();
        String sql = "SELECT action_type, score_change, old_score, new_score, keterangan, created_at, reference_id " +
                     "FROM trust_score_history WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TrustScoreHistory history = new TrustScoreHistory();
                    history.setActionType(rs.getString("action_type"));
                    history.setScoreChange(rs.getDouble("score_change"));
                    history.setOldScore(rs.getDouble("old_score"));
                    history.setNewScore(rs.getDouble("new_score"));
                    history.setKeterangan(rs.getString("keterangan"));
                    history.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    history.setReferenceId(rs.getObject("reference_id") != null ? rs.getInt("reference_id") : null);
                    historyList.add(history);
                }
            }
        }
        return historyList;
    }
}