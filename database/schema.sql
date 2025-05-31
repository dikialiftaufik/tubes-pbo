-- ======================================================
-- Database: barter_buku_kuliah
-- ======================================================

-- 1. Buat database (jika belum ada)
CREATE DATABASE IF NOT EXISTS barter_buku_kuliah
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
USE barter_buku_kuliah;


-- 2. Tabel users
CREATE TABLE IF NOT EXISTS users (
    id_user INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    nim VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nomor_telepon VARCHAR(20),
    fakultas VARCHAR(100),
    program_studi VARCHAR(100),
    angkatan YEAR,
    alamat TEXT,
    foto_profil VARCHAR(255),
    trust_score DECIMAL(3,2) DEFAULT 0.00,
    status_akun ENUM('Aktif', 'Nonaktif', 'Suspended') DEFAULT 'Aktif',
    role ENUM('User','Admin') DEFAULT 'User',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_trust_score (trust_score),
    INDEX idx_status (status_akun)
) ENGINE=InnoDB;


-- 3. Tabel master_fakultas
CREATE TABLE IF NOT EXISTS master_fakultas (
    id_fakultas INT PRIMARY KEY AUTO_INCREMENT,
    nama_fakultas VARCHAR(100) NOT NULL UNIQUE,
    kode_fakultas VARCHAR(10) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 4. Tabel master_mata_kuliah
CREATE TABLE IF NOT EXISTS master_mata_kuliah (
    id_matkul INT PRIMARY KEY AUTO_INCREMENT,
    kode_matkul VARCHAR(20) NOT NULL UNIQUE,
    nama_matkul VARCHAR(200) NOT NULL,
    id_fakultas INT NOT NULL,
    semester INT,
    sks INT,
    FOREIGN KEY (id_fakultas) REFERENCES master_fakultas(id_fakultas)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 5. Insert data master fakultas Telkom University
INSERT IGNORE INTO master_fakultas (nama_fakultas, kode_fakultas) VALUES
('Fakultas Teknik Elektro', 'FTE'),
('Fakultas Rekayasa Industri', 'FRI'),
('Fakultas Informatika', 'FIF'),
('Fakultas Ekonomi dan Bisnis', 'FEB'),
('Fakultas Komunikasi dan Bisnis', 'FKB'),
('Fakultas Industri Kreatif', 'FIK'),
('Fakultas Ilmu Terapan', 'FIT');


-- 6. Tabel books
CREATE TABLE IF NOT EXISTS books (
    id_buku INT PRIMARY KEY AUTO_INCREMENT,
    id_pemilik INT NOT NULL,
    judul VARCHAR(255) NOT NULL,
    penulis VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    mata_kuliah VARCHAR(100) NOT NULL,
    kondisi ENUM('Baru', 'Bagus', 'Cukup', 'Rusak Ringan', 'Rusak Sedang') NOT NULL,
    deskripsi TEXT,
    harga_asli DECIMAL(10,2),
    status_ketersediaan ENUM('Tersedia', 'Dalam Proses Barter', 'Tidak Tersedia') DEFAULT 'Tersedia',
    foto_path_1 VARCHAR(255),
    foto_path_2 VARCHAR(255),
    foto_path_3 VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pemilik) REFERENCES users(id_user) ON DELETE CASCADE,
    INDEX idx_mata_kuliah (mata_kuliah),
    INDEX idx_status (status_ketersediaan),
    INDEX idx_pemilik (id_pemilik),
    UNIQUE KEY unique_book_per_owner (judul, penulis, id_pemilik)
) ENGINE=InnoDB;


-- 7. Tabel transactions
CREATE TABLE IF NOT EXISTS transactions (
    id_transaksi INT PRIMARY KEY AUTO_INCREMENT,
    id_buku_diminta INT NOT NULL,
    id_buku_ditawarkan INT NOT NULL,
    id_user_pengaju INT NOT NULL,
    id_user_pemberi INT NOT NULL,
    status_transaksi ENUM(
        'Pending','Ditolak','Diterima',
        'Confirmed','Completed','Dibatalkan','Expired'
    ) DEFAULT 'Pending',
    pesan_pengaju TEXT,
    pesan_balasan TEXT,
    match_score DECIMAL(5,2),
    timestamp_request DATETIME DEFAULT CURRENT_TIMESTAMP,
    timestamp_response DATETIME NULL,
    timestamp_confirmed DATETIME NULL,
    timestamp_completed DATETIME NULL,
    expired_at DATETIME DEFAULT (DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY)),
    FOREIGN KEY (id_buku_diminta) REFERENCES books(id_buku) ON DELETE CASCADE,
    FOREIGN KEY (id_buku_ditawarkan) REFERENCES books(id_buku) ON DELETE CASCADE,
    FOREIGN KEY (id_user_pengaju) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_user_pemberi) REFERENCES users(id_user) ON DELETE CASCADE,
    INDEX idx_status (status_transaksi),
    INDEX idx_pengaju (id_user_pengaju),
    INDEX idx_pemberi (id_user_pemberi),
    INDEX idx_expired (expired_at)
) ENGINE=InnoDB;


-- 8. Tabel ratings
CREATE TABLE IF NOT EXISTS ratings (
    id_rating INT PRIMARY KEY AUTO_INCREMENT,
    id_transaksi INT NOT NULL,
    rater_id INT NOT NULL,
    ratee_id INT NOT NULL,
    skor INT NOT NULL CHECK (skor BETWEEN 1 AND 5),
    komentar VARCHAR(250),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_transaksi) REFERENCES transactions(id_transaksi) ON DELETE CASCADE,
    FOREIGN KEY (rater_id) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (ratee_id) REFERENCES users(id_user) ON DELETE CASCADE,
    UNIQUE KEY unique_rating_per_transaction (id_transaksi, rater_id, ratee_id)
) ENGINE=InnoDB;


-- 9. Tabel notifications
CREATE TABLE IF NOT EXISTS notifications (
    id_notifikasi INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    jenis ENUM(
        'Barter Request','Request Accepted','Request Rejected',
        'Transaction Confirmed','Transaction Completed',
        'Rating Received','System'
    ) NOT NULL,
    judul VARCHAR(100) NOT NULL,
    pesan TEXT NOT NULL,
    reference_id INT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id_user) ON DELETE CASCADE,
    INDEX idx_user_read (user_id, is_read),
    INDEX idx_created (created_at)
) ENGINE=InnoDB;


-- 10. Tabel trust_score_history
CREATE TABLE IF NOT EXISTS trust_score_history (
    id_history INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    score_change DECIMAL(3,2) NOT NULL,
    old_score DECIMAL(3,2) NOT NULL,
    new_score DECIMAL(3,2) NOT NULL,
    reference_id INT,
    keterangan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id_user) ON DELETE CASCADE,
    INDEX idx_user_date (user_id, created_at)
) ENGINE=InnoDB;
