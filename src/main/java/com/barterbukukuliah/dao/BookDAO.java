package com.barterbukukuliah.dao;

import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Pattern untuk entitas Book.
 * Berisi implementasi CRUD lengkap (insert, update, delete, findById, findByPemilik, dsb.).
 */
public class BookDAO {

    /**
     * Insert satu buku baru ke database.
     */
    public void insert(Book book) throws SQLException {
        String sql = "INSERT INTO books (id_pemilik, judul, penulis, isbn, mata_kuliah, kondisi, deskripsi, harga_asli, status_ketersediaan, foto_path_1, foto_path_2, foto_path_3) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, book.getIdPemilik());
            stmt.setString(2, book.getJudul());
            stmt.setString(3, book.getPenulis());
            stmt.setString(4, book.getIsbn());
            stmt.setString(5, book.getMataKuliah());
            stmt.setString(6, book.getKondisi());
            stmt.setString(7, book.getDeskripsi());
            stmt.setDouble(8, book.getHargaAsli());
            stmt.setString(9, book.getStatusKetersediaan());
            stmt.setString(10, book.getFotoPath1());
            stmt.setString(11, book.getFotoPath2());
            stmt.setString(12, book.getFotoPath3());
            stmt.executeUpdate();

            // Dapatkan id_buku yang di‐generate
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setIdBuku(generatedKeys.getInt(1));
                }
            }
        }
    }

    /**
     * Update data buku (seluruh field kecuali id_buku dan id_pemilik).
     */
    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET judul=?, penulis=?, isbn=?, mata_kuliah=?, kondisi=?, deskripsi=?, harga_asli=?, status_ketersediaan=?, foto_path_1=?, foto_path_2=?, foto_path_3=?, updated_at=CURRENT_TIMESTAMP WHERE id_buku=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getJudul());
            stmt.setString(2, book.getPenulis());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getMataKuliah());
            stmt.setString(5, book.getKondisi());
            stmt.setString(6, book.getDeskripsi());
            stmt.setDouble(7, book.getHargaAsli());
            stmt.setString(8, book.getStatusKetersediaan());
            stmt.setString(9, book.getFotoPath1());
            stmt.setString(10, book.getFotoPath2());
            stmt.setString(11, book.getFotoPath3());
            stmt.setInt(12, book.getIdBuku());
            stmt.executeUpdate();
        }
    }

    /**
     * Hapus buku berdasarkan id_buku.
     */
    public void delete(int idBuku) throws SQLException {
        String sql = "DELETE FROM books WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            stmt.executeUpdate();
        }
    }

    /**
     * Ambil satu buku berdasarkan id_buku.
     */
    public Book findById(int idBuku) throws SQLException {
        String sql = "SELECT * FROM books WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Ambil semua buku milik user tertentu (id_pemilik = idUser).
     */
    public List<Book> findByPemilik(int idUser) throws SQLException {
        String sql = "SELECT * FROM books WHERE id_pemilik = ? ORDER BY created_at DESC";
        List<Book> bukuList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bukuList.add(mapResultSetToBook(rs));
                }
            }
        }
        return bukuList;
    }

    /**
     * Map ResultSet baris saat ini menjadi objek Book.
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setIdBuku(rs.getInt("id_buku"));
        book.setIdPemilik(rs.getInt("id_pemilik"));
        book.setJudul(rs.getString("judul"));
        book.setPenulis(rs.getString("penulis"));
        book.setIsbn(rs.getString("isbn"));
        book.setMataKuliah(rs.getString("mata_kuliah"));
        book.setKondisi(rs.getString("kondisi"));
        book.setDeskripsi(rs.getString("deskripsi"));
        book.setHargaAsli(rs.getDouble("harga_asli"));
        book.setStatusKetersediaan(rs.getString("status_ketersediaan"));
        book.setFotoPath1(rs.getString("foto_path_1"));
        book.setFotoPath2(rs.getString("foto_path_2"));
        book.setFotoPath3(rs.getString("foto_path_3"));
        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            book.setCreatedAt(createdTs.toLocalDateTime());
        }
        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            book.setUpdatedAt(updatedTs.toLocalDateTime());
        }
        return book;
    }
}
