package com.barterbukukuliah.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.barterbukukuliah.model.User;
import com.barterbukukuliah.util.DatabaseConnection;
import com.barterbukukuliah.util.HashUtil;

/**
 * DAO Pattern untuk entitas User.
 */
public class UserDAO {

    /**
     * Cari user berdasarkan email (unique).
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setIdUser(rs.getInt("id_user"));
                    user.setNama(rs.getString("nama"));
                    user.setNim(rs.getString("nim"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setNomorTelepon(rs.getString("nomor_telepon"));
                    user.setFakultas(rs.getString("fakultas"));
                    user.setProgramStudi(rs.getString("program_studi"));
                    // Perhatikan: rs.getObject("angkatan") bisa null
                    if (rs.getObject("angkatan") != null) {
                        user.setAngkatan(rs.getInt("angkatan"));
                    }
                    user.setAlamat(rs.getString("alamat"));
                    user.setFotoProfil(rs.getString("foto_profil"));
                    user.setTrustScore(rs.getDouble("trust_score"));
                    user.setStatusAkun(rs.getString("status_akun"));
                    user.setRole(rs.getString("role"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return user;
                } else {
                    return null;
                }
            }
        }
    }
    

    /**
     * Cari user berdasarkan NIM (unique).
     */
    public User findByNim(String nim) throws SQLException {
        String sql = "SELECT * FROM users WHERE nim = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nim);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setIdUser(rs.getInt("id_user"));
                    user.setNama(rs.getString("nama"));
                    user.setNim(rs.getString("nim"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setNomorTelepon(rs.getString("nomor_telepon"));
                    user.setFakultas(rs.getString("fakultas"));
                    user.setProgramStudi(rs.getString("program_studi"));
                    if (rs.getObject("angkatan") != null) {
                        user.setAngkatan(rs.getInt("angkatan"));
                    }
                    user.setAlamat(rs.getString("alamat"));
                    user.setFotoProfil(rs.getString("foto_profil"));
                    user.setTrustScore(rs.getDouble("trust_score"));
                    user.setStatusAkun(rs.getString("status_akun"));
                    user.setRole(rs.getString("role"));
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    if (rs.getTimestamp("updated_at") != null) {
                        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }
                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    /**
 * Ambil semua user yang terdaftar di database.
 */
public List<User> findAllUsers() throws SQLException {
    String sql = "SELECT * FROM users ORDER BY created_at DESC";
    List<User> userList = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            User user = new User();
            user.setIdUser(rs.getInt("id_user"));
            user.setNama(rs.getString("nama"));
            user.setNim(rs.getString("nim"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setNomorTelepon(rs.getString("nomor_telepon"));
            user.setFakultas(rs.getString("fakultas"));
            user.setProgramStudi(rs.getString("program_studi"));
            if (rs.getObject("angkatan") != null) {
                user.setAngkatan(rs.getInt("angkatan"));
            }
            user.setAlamat(rs.getString("alamat"));
            user.setFotoProfil(rs.getString("foto_profil"));
            user.setTrustScore(rs.getDouble("trust_score"));
            user.setStatusAkun(rs.getString("status_akun"));
            user.setRole(rs.getString("role"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            userList.add(user);
        }
    }
    return userList;
}


    /**
     * Insert user baru ke database. 
     * Dianggap user.getPasswordHash() berisi plaintext password;
     * method ini akan meng‐hash sebelum menyimpan.
     */
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (nama, nim, email, password_hash, nomor_telepon, fakultas, program_studi, angkatan, alamat, foto_profil, trust_score, status_akun, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Ambil plaintext password dari user.getPasswordHash() dan hash sekarang
        String plainPassword = user.getPasswordHash();
        String hashedPassword = HashUtil.hashPassword(plainPassword);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getNim());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, hashedPassword);
            stmt.setString(5, user.getNomorTelepon());
            stmt.setString(6, user.getFakultas());
            stmt.setString(7, user.getProgramStudi());

            if (user.getAngkatan() != 0) {
                stmt.setInt(8, user.getAngkatan());
            } else {
                stmt.setNull(8, java.sql.Types.INTEGER);
            }

            stmt.setString(9, user.getAlamat());
            stmt.setString(10, user.getFotoProfil());
            stmt.setDouble(11, user.getTrustScore());
            stmt.setString(12, user.getStatusAkun());
            stmt.setString(13, user.getRole());
            stmt.executeUpdate();
        }
    }

        /**
     * Update password untuk user dengan email tertentu.
     * Password plaintext akan di‐hash sebelum disimpan.
     *
     * @param email              email user yang ingin di‐reset password
     * @param newPlainPassword   plaintext password baru
     * @throws SQLException jika terjadi error pada query
     */
    public void updatePasswordByEmail(String email, String newPlainPassword) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";
        // Hash password baru
        String hashedPassword = HashUtil.hashPassword(newPlainPassword);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public void updateTrustScore(int userId, double newTrustScore) throws SQLException {
        String sql = "UPDATE users SET trust_score = ?, updated_at = CURRENT_TIMESTAMP WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newTrustScore);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
    

    /**
     * Update profil user: nama, nomor_telepon, fakultas, program_studi, angkatan, alamat, foto_profil.
     */
    public void updateProfile(User user) throws SQLException {
        String sql = "UPDATE users SET nama = ?, nomor_telepon = ?, fakultas = ?, program_studi = ?, angkatan = ?, alamat = ?, foto_profil = ?, updated_at = CURRENT_TIMESTAMP WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getNomorTelepon());
            stmt.setString(3, user.getFakultas());
            stmt.setString(4, user.getProgramStudi());
            if (user.getAngkatan() > 0) {
                stmt.setInt(5, user.getAngkatan());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, user.getAlamat());
            stmt.setString(7, user.getFotoProfil());
            stmt.setInt(8, user.getIdUser());
            stmt.executeUpdate();
        }
    }

    // Bagian dari UserDAO.java (pastikan metode findById seperti ini atau serupa)
public User findById(int idUser) throws SQLException {
    String sql = "SELECT * FROM users WHERE id_user = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idUser);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setNama(rs.getString("nama"));
                user.setNim(rs.getString("nim"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setNomorTelepon(rs.getString("nomor_telepon"));
                user.setFakultas(rs.getString("fakultas"));
                user.setProgramStudi(rs.getString("program_studi"));
                if (rs.getObject("angkatan") != null) {
                    user.setAngkatan(rs.getInt("angkatan"));
                }
                user.setAlamat(rs.getString("alamat"));
                user.setFotoProfil(rs.getString("foto_profil"));
                user.setTrustScore(rs.getDouble("trust_score")); // Penting
                user.setStatusAkun(rs.getString("status_akun"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                return user;
            }
        }
    }
    return null;
}

/**
     * Helper method untuk memetakan ResultSet ke objek User.
     * Ini menghindari duplikasi kode di findByEmail, findByNim, findById, findAllUsers.
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUser(rs.getInt("id_user"));
        user.setNama(rs.getString("nama"));
        user.setNim(rs.getString("nim"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setNomorTelepon(rs.getString("nomor_telepon"));
        user.setFakultas(rs.getString("fakultas"));
        user.setProgramStudi(rs.getString("program_studi"));
        // Perhatikan: rs.getObject("angkatan") bisa null
        if (rs.getObject("angkatan") != null) {
            user.setAngkatan(rs.getInt("angkatan"));
        } else {
            user.setAngkatan(0); // Atau biarkan default model jika berbeda
        }
        user.setAlamat(rs.getString("alamat"));
        user.setFotoProfil(rs.getString("foto_profil"));
        user.setTrustScore(rs.getDouble("trust_score")); // Penting untuk trust score
        user.setStatusAkun(rs.getString("status_akun"));
        user.setRole(rs.getString("role"));
        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return user;
    }

}
