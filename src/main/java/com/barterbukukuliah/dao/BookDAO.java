package com.barterbukukuliah.dao;

import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;
import com.barterbukukuliah.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    /**
     * Mengambil semua buku yang dimiliki user dengan id tertentu.
     */
    public List<Book> findByPemilik(int idPemilik) throws SQLException {
        String sql = "SELECT * FROM books WHERE id_pemilik = ?";
        List<Book> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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
                    // Gunakan getTimestamp untuk kolom TIMESTAMP
                    book.setCreatedAt(rs.getTimestamp("created_at"));
                    book.setUpdatedAt(rs.getTimestamp("updated_at"));
                    result.add(book);
                }
            }
        }
        return result;
    }

    /**
     * Menambahkan objek Book baru ke database.
     */
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (id_pemilik, judul, penulis, isbn, mata_kuliah, kondisi, deskripsi, harga_asli, status_ketersediaan, foto_path_1, foto_path_2, foto_path_3) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, book.getIdPemilik());
            stmt.setString(2, book.getJudul());
            stmt.setString(3, book.getPenulis());
            if (book.getIsbn() != null) stmt.setString(4, book.getIsbn());
            else stmt.setNull(4, Types.VARCHAR);
            stmt.setString(5, book.getMataKuliah());
            stmt.setString(6, book.getKondisi());
            if (book.getDeskripsi() != null) stmt.setString(7, book.getDeskripsi());
            else stmt.setNull(7, Types.VARCHAR);
            stmt.setDouble(8, book.getHargaAsli());
            stmt.setString(9, book.getStatusKetersediaan());
            if (book.getFotoPath1() != null) stmt.setString(10, book.getFotoPath1());
            else stmt.setNull(10, Types.VARCHAR);
            if (book.getFotoPath2() != null) stmt.setString(11, book.getFotoPath2());
            else stmt.setNull(11, Types.VARCHAR);
            if (book.getFotoPath3() != null) stmt.setString(12, book.getFotoPath3());
            else stmt.setNull(12, Types.VARCHAR);
            stmt.executeUpdate();
        }
    }

    /**
     * Menghapus buku berdasarkan id.
     */
    public void deleteBook(int idBuku) throws SQLException {
        String sql = "DELETE FROM books WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            stmt.executeUpdate();
        }
    }

    public Book findById(int idBuku) throws SQLException {
        String sql = "SELECT * FROM books WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
                    book.setCreatedAt(rs.getTimestamp("created_at"));
                    book.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return book;
                } else {
                    return null;
                }
            }
        }
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET judul = ?, penulis = ?, isbn = ?, mata_kuliah = ?, kondisi = ?, " +
                     "deskripsi = ?, harga_asli = ?, status_ketersediaan = ?, foto_path_1 = ?, foto_path_2 = ?, foto_path_3 = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getJudul());
            stmt.setString(2, book.getPenulis());

            if (book.getIsbn() != null) stmt.setString(3, book.getIsbn());
            else stmt.setNull(3, Types.VARCHAR);

            stmt.setString(4, book.getMataKuliah());
            stmt.setString(5, book.getKondisi());

            if (book.getDeskripsi() != null) stmt.setString(6, book.getDeskripsi());
            else stmt.setNull(6, Types.VARCHAR);

            stmt.setDouble(7, book.getHargaAsli());
            stmt.setString(8, book.getStatusKetersediaan());

            if (book.getFotoPath1() != null) stmt.setString(9, book.getFotoPath1());
            else stmt.setNull(9, Types.VARCHAR);

            if (book.getFotoPath2() != null) stmt.setString(10, book.getFotoPath2());
            else stmt.setNull(10, Types.VARCHAR);

            if (book.getFotoPath3() != null) stmt.setString(11, book.getFotoPath3());
            else stmt.setNull(11, Types.VARCHAR);

            stmt.setInt(12, book.getIdBuku());
            stmt.executeUpdate();
        }
    }

    // Hapus buku secara permanen oleh admin
    public void forceDeleteBook(int idBuku) throws SQLException {
        String sql = "DELETE FROM books WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            stmt.executeUpdate();
        }
    }

    // Update status_ketersediaan buku
    public void updateBookStatus(int idBuku, String statusBaru) throws SQLException {
        String sql = "UPDATE books SET status_ketersediaan = ? WHERE id_buku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusBaru);
            stmt.setInt(2, idBuku);
            stmt.executeUpdate();
        }
    }

    public List<Book> searchBooks(int excludeUserId,
                                  String keyword,
                                  String mataKuliah,
                                  String kondisi,
                                  String sortBy) throws SQLException {

        List<Book> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b.*, u.nama AS pemilik_nama ")
           .append("FROM books b ")
           .append("JOIN users u ON b.id_pemilik = u.id_user ")
           .append("WHERE b.id_pemilik <> ? ")
           .append("AND b.status_ketersediaan = 'Tersedia' ");

        // 1. Keyword (judul, penulis, ISBN)
        if (keyword != null && !keyword.isBlank()) {
            sql.append("AND (LOWER(b.judul) LIKE ? OR LOWER(b.penulis) LIKE ? OR LOWER(b.isbn) LIKE ?) ");
        }
        // 2. Filter mataKuliah
        if (mataKuliah != null && !mataKuliah.isBlank()) {
            sql.append("AND LOWER(b.mata_kuliah) LIKE ? ");
        }
        // 3. Filter kondisi
        if (kondisi != null && !kondisi.isBlank()) {
            sql.append("AND b.kondisi = ? ");
        }
        // 4. Sort By
        switch (sortBy) {
            case "Newest":
                sql.append("ORDER BY b.created_at DESC");
                break;
            case "Oldest":
                sql.append("ORDER BY b.created_at ASC");
                break;
            case "PriceAsc":
                sql.append("ORDER BY b.harga_asli ASC");
                break;
            case "PriceDesc":
                sql.append("ORDER BY b.harga_asli DESC");
                break;
            case "Condition":
                sql.append("ORDER BY FIELD(b.kondisi, 'Baru','Bagus','Cukup','Rusak Ringan','Rusak Sedang')");
                break;
            default:
                sql.append("ORDER BY b.created_at DESC");
                break;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            stmt.setInt(idx++, excludeUserId);

            if (keyword != null && !keyword.isBlank()) {
                String kw = "%" + keyword.toLowerCase() + "%";
                stmt.setString(idx++, kw);
                stmt.setString(idx++, kw);
                stmt.setString(idx++, kw);
            }
            if (mataKuliah != null && !mataKuliah.isBlank()) {
                stmt.setString(idx++, "%" + mataKuliah.toLowerCase() + "%");
            }
            if (kondisi != null && !kondisi.isBlank()) {
                stmt.setString(idx++, kondisi);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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
                    book.setCreatedAt(rs.getTimestamp("created_at"));
                    book.setUpdatedAt(rs.getTimestamp("updated_at"));

                    // Ambil nama pemilik dari kolom pemilik_nama
                    book.setPemilikName(rs.getString("pemilik_nama"));

                    result.add(book);
                }
            }
        }

        return result;
    }

    public List<Book> getAllBooksForAdmin() throws SQLException {
        List<Book> bookList = new ArrayList<>();

        String sql = "SELECT b.*, u.nama AS nama_pemilik, u.email AS email_pemilik "
           + "FROM books b "
           + "JOIN users u ON b.id_pemilik = u.id_user "
           + "ORDER BY b.updated_at DESC";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql);
     ResultSet rs = stmt.executeQuery()) {

    while (rs.next()) {
        Book book = new Book();
        book.setIdBuku(rs.getInt("id_buku"));
        book.setJudul(rs.getString("judul"));
        book.setPenulis(rs.getString("penulis"));
        book.setIsbn(rs.getString("isbn"));
        book.setMataKuliah(rs.getString("mata_kuliah"));
        book.setKondisi(rs.getString("kondisi"));
        book.setDeskripsi(rs.getString("deskripsi"));
        
        // Konversi BigDecimal ke double jika setter setHargaAsli menerima double
        BigDecimal harga = rs.getBigDecimal("harga_asli");
        if (harga != null) {
            book.setHargaAsli(harga.doubleValue());
        } else {
            book.setHargaAsli(0);
        }
        
        book.setStatusKetersediaan(rs.getString("status_ketersediaan"));
        book.setFotoPath1(rs.getString("foto_path_1"));
        book.setFotoPath2(rs.getString("foto_path_2"));
        book.setFotoPath3(rs.getString("foto_path_3"));
        book.setUpdatedAt(rs.getTimestamp("updated_at"));

        User pemilik = new User();
        pemilik.setNama(rs.getString("nama_pemilik"));
        pemilik.setEmail(rs.getString("email_pemilik"));
        book.setPemilik(pemilik);

        bookList.add(book);
    }
}


        return bookList;
    }
}