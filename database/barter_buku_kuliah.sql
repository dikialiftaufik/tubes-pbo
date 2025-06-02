-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 02, 2025 at 08:34 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `barter_buku_kuliah`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id_buku` int(11) NOT NULL,
  `id_pemilik` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `penulis` varchar(255) NOT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `mata_kuliah` varchar(100) NOT NULL,
  `kondisi` enum('Baru','Bagus','Cukup','Rusak Ringan','Rusak Sedang') NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `harga_asli` decimal(10,2) DEFAULT NULL,
  `status_ketersediaan` enum('Tersedia','Sedang Dipinjam','Tidak Tersedia') DEFAULT 'Tersedia',
  `foto_path_1` varchar(255) DEFAULT NULL,
  `foto_path_2` varchar(255) DEFAULT NULL,
  `foto_path_3` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id_buku`, `id_pemilik`, `judul`, `penulis`, `isbn`, `mata_kuliah`, `kondisi`, `deskripsi`, `harga_asli`, `status_ketersediaan`, `foto_path_1`, `foto_path_2`, `foto_path_3`, `created_at`, `updated_at`) VALUES
(1, 3, 'Core Java: Fundamentals, Volume 1', 'Cay S. Horstmann', '9780135166314', 'Pemrograman Berorientasi Objek', 'Bagus', 'Edisi ke-11 dari Core Java Volume I (Fundamentals). Buku ini umum dipakai untuk matakuliah OOP berbasis Java: menjelaskan konsep dasar Java, class, objek, inheritance, interface, exception handling, dan koleksi (collections). Cover dan halaman dalam masih rapih meski sedikit ada goresan di sudut cover karena pernah digunakan.', 150000.00, 'Tersedia', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/books/book_3_1748752313514_1.jpg', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/books/book_3_1748752313564_2.jpg', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/books/book_3_1748752313570_3.jpg', '2025-06-01 04:31:53', '2025-06-01 07:31:59'),
(10, 5, 'Introduction to Algorithms, 3rd Edition', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein', '9780262033848', 'Algoritma dan Struktur Data', 'Bagus', 'Edisi ketiga buku klasik tentang algoritma komprehensif, mencakup berbagai topik seperti struktur data, algoritma graf, teori kompleksitas, dan lainnya.', 750000.00, 'Tersedia', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/books/book_5_1748774617352_1.jpg', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/books/book_5_1748774617366_2.jpg', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/books/book_5_1748774617370_3.jpg', '2025-06-01 10:43:37', '2025-06-01 17:13:15');

-- --------------------------------------------------------

--
-- Table structure for table `master_fakultas`
--

CREATE TABLE `master_fakultas` (
  `id_fakultas` int(11) NOT NULL,
  `nama_fakultas` varchar(100) NOT NULL,
  `kode_fakultas` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `master_fakultas`
--

INSERT INTO `master_fakultas` (`id_fakultas`, `nama_fakultas`, `kode_fakultas`) VALUES
(1, 'Fakultas Teknik Elektro', 'FTE'),
(2, 'Fakultas Rekayasa Industri', 'FRI'),
(3, 'Fakultas Informatika', 'FIF'),
(4, 'Fakultas Ekonomi dan Bisnis', 'FEB'),
(5, 'Fakultas Komunikasi dan Bisnis', 'FKB'),
(6, 'Fakultas Industri Kreatif', 'FIK'),
(7, 'Fakultas Ilmu Terapan', 'FIT');

-- --------------------------------------------------------

--
-- Table structure for table `master_mata_kuliah`
--

CREATE TABLE `master_mata_kuliah` (
  `id_matkul` int(11) NOT NULL,
  `kode_matkul` varchar(20) NOT NULL,
  `nama_matkul` varchar(200) NOT NULL,
  `id_fakultas` int(11) NOT NULL,
  `semester` int(11) DEFAULT NULL,
  `sks` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id_notifikasi` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `jenis` enum('Barter Request','Request Accepted','Request Rejected','Transaction Confirmed','Transaction Completed','Rating Received','System') NOT NULL,
  `judul` varchar(100) NOT NULL,
  `pesan` text NOT NULL,
  `reference_id` int(11) DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`id_notifikasi`, `user_id`, `jenis`, `judul`, `pesan`, `reference_id`, `is_read`, `created_at`) VALUES
(1, 3, 'Barter Request', 'Permintaan Barter Baru', 'Ada pengguna yang ingin menukar buku milik Anda.', 2, 0, '2025-06-01 14:27:10'),
(2, 3, 'Request Accepted', 'Barter Anda Diterima', 'Permintaan barter Anda telah diterima oleh pemilik buku.', 4, 0, '2025-06-01 18:26:45'),
(3, 3, 'Request Accepted', 'Barter Anda Diterima', 'Permintaan barter Anda telah diterima oleh pemilik buku.', 5, 0, '2025-06-02 01:27:11'),
(4, 3, 'Request Accepted', 'Barter Anda Diterima', 'Permintaan barter Anda telah diterima oleh pemilik buku.', 6, 0, '2025-06-02 06:28:15');

-- --------------------------------------------------------

--
-- Table structure for table `ratings`
--

CREATE TABLE `ratings` (
  `id_rating` int(11) NOT NULL,
  `id_transaksi` int(11) NOT NULL,
  `rater_id` int(11) NOT NULL,
  `ratee_id` int(11) NOT NULL,
  `skor` int(11) NOT NULL CHECK (`skor` between 1 and 5),
  `komentar` varchar(250) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id_transaksi` int(11) NOT NULL,
  `id_buku_diminta` int(11) NOT NULL,
  `id_buku_ditawarkan` int(11) NOT NULL,
  `id_user_pengaju` int(11) NOT NULL,
  `id_user_pemberi` int(11) NOT NULL,
  `status_transaksi` enum('Pending','Ditolak','Diterima','Confirmed','Completed','Dibatalkan','Expired') DEFAULT 'Pending',
  `pesan_pengaju` text DEFAULT NULL,
  `pesan_balasan` text DEFAULT NULL,
  `match_score` decimal(5,2) DEFAULT NULL,
  `timestamp_request` datetime DEFAULT current_timestamp(),
  `timestamp_response` datetime DEFAULT NULL,
  `timestamp_confirmed` datetime DEFAULT NULL,
  `timestamp_completed` datetime DEFAULT NULL,
  `expired_at` datetime DEFAULT (current_timestamp() + interval 7 day)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id_transaksi`, `id_buku_diminta`, `id_buku_ditawarkan`, `id_user_pengaju`, `id_user_pemberi`, `status_transaksi`, `pesan_pengaju`, `pesan_balasan`, `match_score`, `timestamp_request`, `timestamp_response`, `timestamp_confirmed`, `timestamp_completed`, `expired_at`) VALUES
(4, 10, 1, 3, 5, 'Diterima', 'urgent banget', 'bolehh', 60.00, '2025-06-02 00:52:42', '2025-06-02 01:26:45', NULL, NULL, '2025-06-09 00:52:42'),
(6, 10, 1, 3, 5, 'Diterima', 'urgent bgt', 'ok', 60.00, '2025-06-02 13:26:42', '2025-06-02 13:28:15', NULL, NULL, '2025-06-09 13:26:42');

-- --------------------------------------------------------

--
-- Table structure for table `trust_score_history`
--

CREATE TABLE `trust_score_history` (
  `id_history` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `action_type` varchar(50) NOT NULL,
  `score_change` decimal(3,2) NOT NULL,
  `old_score` decimal(3,2) NOT NULL,
  `new_score` decimal(3,2) NOT NULL,
  `reference_id` int(11) DEFAULT NULL,
  `keterangan` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `nim` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nomor_telepon` varchar(20) DEFAULT NULL,
  `fakultas` varchar(100) DEFAULT NULL,
  `program_studi` varchar(100) DEFAULT NULL,
  `angkatan` year(4) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `foto_profil` varchar(255) DEFAULT NULL,
  `trust_score` decimal(3,2) DEFAULT 0.00,
  `status_akun` enum('Aktif','Nonaktif','Suspended') DEFAULT 'Aktif',
  `role` enum('User','Admin') DEFAULT 'User',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `nama`, `nim`, `email`, `password_hash`, `nomor_telepon`, `fakultas`, `program_studi`, `angkatan`, `alamat`, `foto_profil`, `trust_score`, `status_akun`, `role`, `created_at`, `updated_at`) VALUES
(1, 'Admin Tass', '2021000001', 'admin@tass.telkomuniversity.ac.id', '$2a$10$Og2yIzkbpn5U2xQim.ij.uvlOCT/FN5TCo5OAo5mmKlbGgQ7VRKcK', '081234567890', 'Ilmu Terapan', 'D3 Sistem Informasi', '2021', 'Jl. Citarum No. 1, Bandung', '/images/profile/user1.jpg', 0.00, 'Aktif', 'Admin', '2025-05-31 09:46:11', '2025-06-01 16:22:12'),
(3, 'DIKI ALIF TAUFIK', '607012400005', 'dikialiftaufik@student.telkomuniversity.ac.id', '$2a$10$EmlEHodvSuV/U59S3jr3/uGHaaFeIbXLyC0wvfsim2xz9oeAmA6QG', '089659317206', 'Ilmu Terapan', 'D3 Sistem Informasi', '2024', 'Jl. Bihbul Raya I', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/profile/user_3_1748738013953.png', 0.00, 'Aktif', 'User', '2025-05-31 10:14:32', '2025-06-01 01:58:09'),
(5, 'EGA FIANDRA PRATAMA', '607012400032', 'egafiandrapratama@student.telkomuniversity.ac.id', '$2a$10$A6neGZoGATaSNTLHBDvPJ.LrQ1vR13lNZg/p9PRRy5IbBXaQn1Vsy', '081215486311', 'Ilmu Terapan', 'D3 Sistem Informasi', '2024', 'Jl. Buah Batu', 'file:/D:/Telkom%20University/Semester%202/PEMROGRAMAN%20BERORIENTASI%20OBJEK/barterbukukuliah/uploads/profile/user_5_1748774335334.jpg', 0.00, 'Aktif', 'User', '2025-06-01 10:28:19', '2025-06-01 10:38:55');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id_buku`),
  ADD UNIQUE KEY `unique_book_per_owner` (`judul`,`penulis`,`id_pemilik`),
  ADD KEY `idx_mata_kuliah` (`mata_kuliah`),
  ADD KEY `idx_status` (`status_ketersediaan`),
  ADD KEY `idx_pemilik` (`id_pemilik`);

--
-- Indexes for table `master_fakultas`
--
ALTER TABLE `master_fakultas`
  ADD PRIMARY KEY (`id_fakultas`),
  ADD UNIQUE KEY `nama_fakultas` (`nama_fakultas`),
  ADD UNIQUE KEY `kode_fakultas` (`kode_fakultas`);

--
-- Indexes for table `master_mata_kuliah`
--
ALTER TABLE `master_mata_kuliah`
  ADD PRIMARY KEY (`id_matkul`),
  ADD UNIQUE KEY `kode_matkul` (`kode_matkul`),
  ADD KEY `id_fakultas` (`id_fakultas`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id_notifikasi`),
  ADD KEY `idx_user_read` (`user_id`,`is_read`),
  ADD KEY `idx_created` (`created_at`);

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD PRIMARY KEY (`id_rating`),
  ADD UNIQUE KEY `unique_rating_per_transaction` (`id_transaksi`,`rater_id`,`ratee_id`),
  ADD KEY `rater_id` (`rater_id`),
  ADD KEY `ratee_id` (`ratee_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_buku_diminta` (`id_buku_diminta`),
  ADD KEY `id_buku_ditawarkan` (`id_buku_ditawarkan`),
  ADD KEY `idx_status` (`status_transaksi`),
  ADD KEY `idx_pengaju` (`id_user_pengaju`),
  ADD KEY `idx_pemberi` (`id_user_pemberi`),
  ADD KEY `idx_expired` (`expired_at`);

--
-- Indexes for table `trust_score_history`
--
ALTER TABLE `trust_score_history`
  ADD PRIMARY KEY (`id_history`),
  ADD KEY `idx_user_date` (`user_id`,`created_at`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `nim` (`nim`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_trust_score` (`trust_score`),
  ADD KEY `idx_status` (`status_akun`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id_buku` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `master_fakultas`
--
ALTER TABLE `master_fakultas`
  MODIFY `id_fakultas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `master_mata_kuliah`
--
ALTER TABLE `master_mata_kuliah`
  MODIFY `id_matkul` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id_notifikasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `id_rating` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `trust_score_history`
--
ALTER TABLE `trust_score_history`
  MODIFY `id_history` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `books_ibfk_1` FOREIGN KEY (`id_pemilik`) REFERENCES `users` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `master_mata_kuliah`
--
ALTER TABLE `master_mata_kuliah`
  ADD CONSTRAINT `master_mata_kuliah_ibfk_1` FOREIGN KEY (`id_fakultas`) REFERENCES `master_fakultas` (`id_fakultas`) ON UPDATE CASCADE;

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `ratings`
--
ALTER TABLE `ratings`
  ADD CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `transactions` (`id_transaksi`) ON DELETE CASCADE,
  ADD CONSTRAINT `ratings_ibfk_2` FOREIGN KEY (`rater_id`) REFERENCES `users` (`id_user`) ON DELETE CASCADE,
  ADD CONSTRAINT `ratings_ibfk_3` FOREIGN KEY (`ratee_id`) REFERENCES `users` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`id_buku_diminta`) REFERENCES `books` (`id_buku`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`id_buku_ditawarkan`) REFERENCES `books` (`id_buku`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`id_user_pengaju`) REFERENCES `users` (`id_user`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_4` FOREIGN KEY (`id_user_pemberi`) REFERENCES `users` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `trust_score_history`
--
ALTER TABLE `trust_score_history`
  ADD CONSTRAINT `trust_score_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_user`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
