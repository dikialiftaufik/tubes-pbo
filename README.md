<p align="center">
  <h1 align="center">📚 Barter Buku Kuliah</h1>
  <p align="center">
    Platform pertukaran buku kuliah antar mahasiswa berbasis desktop, dibangun dengan <strong>JavaFX</strong> dan <strong>MySQL</strong>.
  </p>
  <p align="center">
    <img src="https://img.shields.io/badge/Java-11+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 11+"/>
    <img src="https://img.shields.io/badge/JavaFX-23-007396?style=for-the-badge&logo=java&logoColor=white" alt="JavaFX 23"/>
    <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
    <img src="https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
    <img src="https://img.shields.io/badge/License-Academic-blue?style=for-the-badge" alt="License"/>
  </p>
</p>

---

## 📖 Deskripsi Proyek

**Barter Buku Kuliah** adalah aplikasi desktop yang memfasilitasi pertukaran (barter) buku kuliah antar mahasiswa di lingkungan Telkom University. Aplikasi ini mengatasi permasalahan tingginya biaya buku kuliah baru dengan menyediakan platform di mana mahasiswa dapat saling menukar buku yang sudah tidak digunakan lagi.

Proyek ini dikembangkan sebagai **Tugas Besar** mata kuliah **Pemrograman Berorientasi Objek (PBO)** di Telkom University, mengimplementasikan konsep-konsep OOP seperti _Encapsulation_, _Inheritance_, _Polymorphism_, serta _design pattern_ MVC (Model-View-Controller).

### 🎯 Latar Belakang

- Buku kuliah baru memiliki harga yang relatif mahal bagi mahasiswa.
- Banyak buku kuliah yang hanya digunakan selama satu semester, kemudian menganggur.
- Belum ada platform khusus untuk pertukaran buku antar mahasiswa di lingkungan kampus.

---

## ✨ Fitur Utama

### 👤 Autentikasi & Manajemen Akun

- **Login & Register** — Registrasi akun mahasiswa menggunakan email kampus dan NIM.
- **Password Hashing** — Keamanan password menggunakan **BCrypt** (Spring Security Crypto).
- **Forgot & Reset Password** — Fitur lupa password dan reset password.
- **Manajemen Profil** — Upload foto profil, edit data diri (fakultas, program studi, angkatan, alamat, nomor telepon).

### 📚 Manajemen Buku

- **CRUD Buku** — Tambah, lihat, edit, dan hapus buku milik sendiri.
- **Upload Multi-Foto** — Mendukung hingga 3 foto per buku dengan _image slider_ pada detail.
- **Informasi Lengkap** — Judul, penulis, ISBN, mata kuliah, kondisi buku (Baru/Bagus/Cukup/Rusak Ringan/Rusak Sedang), harga asli, dan deskripsi.
- **Status Ketersediaan** — Otomatis berubah menjadi _"Sedang Dipinjam"_ saat barter diterima.

### 🔍 Pencarian & Filter Buku

- **Search** berdasarkan kata kunci (judul/penulis).
- **Filter** berdasarkan mata kuliah dan kondisi buku.
- **Sorting** — Terbaru, Terlama, Harga Terendah, Harga Tertinggi, dan Kondisi.
- Buku milik sendiri tidak ditampilkan di hasil pencarian.

### 🔄 Sistem Barter

- **Ajukan Barter** — Pilih buku yang ingin ditukar, pilih buku milik sendiri sebagai tawaran, dan kirim pesan ke pemilik.
- **Matching Score Algorithm** — Algoritma pencocokan otomatis dengan 5 kriteria:
  1. **Mutual Interest** — Kedua belah pihak tertarik.
  2. **Subject Match** — Kesamaan mata kuliah.
  3. **Condition Match** — Selisih kondisi buku maksimal 1 level.
  4. **Academic Level Match** — Selisih angkatan maksimal 2 tahun.
  5. **Value Match** — Harga asli dalam rentang ±30%.
- **Manajemen Transaksi** — Terima/tolak permintaan barter dengan pesan balasan.
- **Status Transaksi** — Lifecycle lengkap: `Pending → Diterima/Ditolak → Confirmed → Completed / Dibatalkan / Expired`.
- **Auto-Expiry** — Transaksi otomatis kadaluarsa setelah 7 hari tanpa respons.

### ⭐ Rating & Trust Score

- **Sistem Rating (1-5)** — Berikan rating dan komentar setelah barter selesai.
- **Trust Score** — Skor kepercayaan pengguna yang meningkat otomatis (+0.5) setiap kali menerima permintaan barter (maks 9.99).
- **Trust Score History** — Riwayat perubahan skor kepercayaan tercatat di database.

### 🔔 Notifikasi

- **Notifikasi Real-time** — Notifikasi otomatis untuk setiap aktivitas barter (request baru, diterima, ditolak, konfirmasi, selesai, rating diterima).
- **Status Baca/Belum Dibaca** — Pelacakan notifikasi yang sudah/belum dibaca.

### 🛡️ Admin Dashboard

- **Manajemen Pengguna** — Lihat daftar seluruh pengguna beserta detail profil.
- **Manajemen Buku** — Kelola semua buku yang terdaftar di platform.
- **Monitoring Transaksi** — Pantau seluruh transaksi barter.

---

## 🏗️ Arsitektur & Tech Stack

### Arsitektur: MVC (Model-View-Controller)

```
src/main/java/com/barterbukukuliah/
├── Main.java                    # Entry point aplikasi
├── controller/                  # Controller (logika UI)
│   ├── AuthController.java
│   ├── RegisterController.java
│   ├── ForgotPasswordController.java
│   ├── ResetPasswordController.java
│   ├── UserDashboardController.java
│   ├── AdminDashboardController.java
│   ├── AddBookController.java
│   ├── EditBookController.java
│   ├── SearchBookController.java
│   ├── TransaksiMasukController.java
│   ├── ProfileController.java
│   ├── ProfileViewController.java
│   ├── ManageBooksController.java
│   └── RatingViewController.java
├── model/                       # Model (representasi data)
│   ├── User.java
│   ├── Book.java
│   ├── TransaksiBarter.java
│   ├── ItemBarter.java
│   └── TrustScoreHistory.java
├── dao/                         # Data Access Object (akses database)
│   ├── UserDAO.java
│   ├── BookDAO.java
│   ├── TransactionDAO.java
│   ├── NotificationDAO.java
│   └── TrustScoreHistoryDAO.java
├── service/                     # Service Layer (business logic)
│   ├── AuthService.java
│   ├── TransactionService.java
│   ├── MatchingService.java
│   └── UserService.java
├── session/
│   └── Session.java             # Manajemen sesi pengguna
└── util/                        # Utility
    ├── DatabaseConnection.java  # Koneksi database
    ├── HashUtil.java            # BCrypt hashing
    └── ValidationUtil.java      # Validasi input

src/main/resources/com/barterbukukuliah/
├── fxml/                        # View (14 file FXML)
│   ├── Login.fxml
│   ├── Register.fxml
│   ├── ForgotPassword.fxml
│   ├── ResetPassword.fxml
│   ├── UserDashboard.fxml
│   ├── AdminDashboard.fxml
│   ├── AddBook.fxml
│   ├── EditBook.fxml
│   ├── SearchBook.fxml
│   ├── TransaksiMasukView.fxml
│   ├── EditProfile.fxml
│   ├── ProfileView.fxml
│   ├── ManageBooks.fxml
│   └── RatingView.fxml
├── images/                      # Gambar statis (buku contoh)
└── database.properties          # Konfigurasi koneksi database
```

### Tech Stack

| Komponen             | Teknologi                       | Versi          |
| -------------------- | ------------------------------- | -------------- |
| **Bahasa**           | Java                            | 11+            |
| **UI Framework**     | JavaFX (Controls + FXML)        | 23             |
| **Build Tool**       | Apache Maven                    | 3.6+           |
| **Database**         | MySQL / MariaDB                 | 8.0 / 10.4+    |
| **Password Hashing** | Spring Security Crypto (BCrypt) | 6.0.2          |
| **Logging**          | SLF4J + Logback                 | 2.0.7 / 1.4.11 |
| **JDBC Driver**      | MySQL Connector/J               | 8.0.33         |

---

## 📊 Entity Relationship Diagram

```
┌──────────────┐     ┌──────────────────┐     ┌──────────────┐
│    users     │     │   transactions   │     │    books     │
├──────────────┤     ├──────────────────┤     ├──────────────┤
│ id_user (PK) │◄────│ id_user_pengaju  │     │ id_buku (PK) │
│ nama         │◄────│ id_user_pemberi  │────►│ id_pemilik   │──►users
│ nim (UQ)     │     │ id_buku_diminta  │────►│ judul        │
│ email (UQ)   │     │ id_buku_ditawar  │────►│ penulis      │
│ password_hash│     │ status_transaksi │     │ isbn (UQ)    │
│ trust_score  │     │ match_score      │     │ mata_kuliah  │
│ role         │     │ expired_at       │     │ kondisi      │
│ status_akun  │     └──────────────────┘     │ harga_asli   │
└──────────────┘                              │ foto_path_*  │
       │              ┌──────────────┐        │ status       │
       │              │   ratings    │        └──────────────┘
       │              ├──────────────┤
       ├─────────────►│ rater_id     │
       ├─────────────►│ ratee_id     │
       │              │ skor (1-5)   │
       │              │ komentar     │
       │              └──────────────┘
       │
       │              ┌────────────────────┐
       └─────────────►│   notifications    │
                      ├────────────────────┤
                      │ jenis              │
                      │ judul              │
                      │ is_read            │
                      └────────────────────┘
```

---

## ⚙️ Prasyarat (Prerequisites)

Sebelum menjalankan aplikasi, pastikan Anda telah menginstal:

- [**Java JDK 11+**](https://adoptium.net/) (disarankan JDK 17 atau lebih baru)
- [**Apache Maven 3.6+**](https://maven.apache.org/download.cgi)
- [**MySQL 8.0+**](https://dev.mysql.com/downloads/) atau [**XAMPP**](https://www.apachefriends.org/) (yang menyertakan MariaDB)
- **Git** (opsional, untuk clone repository)

### Verifikasi Instalasi

```bash
java -version       # Pastikan output menunjukkan versi 11+
mvn -version        # Pastikan Maven terinstal
mysql --version     # Pastikan MySQL/MariaDB terinstal
```

---

## 🚀 Cara Instalasi & Menjalankan

### 1. Clone Repository

```bash
git clone https://github.com/dikialiftaufik/tubes-pbo.git
cd tubes-pbo
```

### 2. Setup Database

Jalankan MySQL/MariaDB (jika menggunakan XAMPP, aktifkan Apache dan MySQL dari XAMPP Control Panel), kemudian import file SQL:

**Opsi A: Via Command Line**

```bash
mysql -u root -p < database/barter_buku_kuliah.sql
```

**Opsi B: Via phpMyAdmin**

1. Buka `http://localhost/phpmyadmin`
2. Buat database baru dengan nama `barter_buku_kuliah`
3. Pilih tab **Import** → pilih file `database/barter_buku_kuliah.sql` → klik **Go**

### 3. Konfigurasi Koneksi Database

Edit file konfigurasi database sesuai environment Anda:

```
📁 src/main/resources/com/barterbukukuliah/database.properties
```

```properties
db.url=jdbc:mysql://localhost:3306/barter_buku_kuliah?useSSL=false&serverTimezone=Asia/Jakarta
db.username=root
db.password=
db.driver=com.mysql.cj.jdbc.Driver
```

> **⚠️ Catatan:** Sesuaikan `db.username` dan `db.password` dengan konfigurasi MySQL lokal Anda.

### 4. Build & Run

```bash
# Install dependencies dan compile
mvn clean install

# Jalankan aplikasi
mvn clean javafx:run
```

### 5. Akun Default

| Role  | Email                               | Password                    |
| ----- | ----------------------------------- | --------------------------- |
| Admin | `admin@tass.telkomuniversity.ac.id` | _(sesuai hash di database)_ |

Atau register akun baru melalui halaman **Register**.

---

## 🧩 Konsep OOP yang Diterapkan

| Konsep                 | Implementasi                                                                                                               |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| **Encapsulation**      | Seluruh atribut model (`User`, `Book`, `TransaksiBarter`) bersifat `private` dengan getter/setter.                         |
| **Abstraction**        | Layer DAO menyembunyikan detail query SQL dari service layer. Service layer menyembunyikan business logic dari controller. |
| **Inheritance**        | Pewarisan class JavaFX `Application` pada `Main.java`.                                                                     |
| **Polymorphism**       | Penggunaan `TableCell<>` generics, method overriding `updateItem()`, dan `@Override start()`.                              |
| **MVC Pattern**        | Pemisahan Model (data), View (FXML), dan Controller (logika) secara tegas.                                                 |
| **DAO Pattern**        | Pemisahan akses database ke kelas DAO terpisah per entitas.                                                                |
| **Service Layer**      | Business logic diisolasi pada `AuthService`, `TransactionService`, `MatchingService`.                                      |
| **Session Management** | Pengelolaan sesi login pengguna melalui kelas `Session`.                                                                   |

---

## 📂 Struktur Database

Aplikasi menggunakan **7 tabel** pada database `barter_buku_kuliah`:

| Tabel                 | Deskripsi                                  |
| --------------------- | ------------------------------------------ |
| `users`               | Data pengguna (mahasiswa & admin)          |
| `books`               | Data buku yang didaftarkan untuk barter    |
| `transactions`        | Riwayat transaksi barter antar pengguna    |
| `ratings`             | Rating dan komentar setelah barter selesai |
| `notifications`       | Notifikasi aktivitas barter                |
| `trust_score_history` | Riwayat perubahan skor kepercayaan         |
| `master_fakultas`     | Data master fakultas di Telkom University  |

---

## 🔐 Keamanan

- **Password Hashing**: Menggunakan algoritma **BCrypt** via Spring Security Crypto, bukan plain text.
- **Prepared Statement**: Seluruh query database menggunakan `PreparedStatement` untuk mencegah **SQL Injection**.
- **Input Validation**: Validasi input pengguna menggunakan `ValidationUtil` sebelum diproses.
- **Role-Based Access**: Pemisahan dashboard dan hak akses antara **User** dan **Admin**.

---

## 🛠️ Pengembangan Selanjutnya (Roadmap)

- [ ] Fitur notifikasi real-time di dalam aplikasi
- [ ] Fitur chat langsung antar pengguna
- [ ] Laporan statistik untuk admin
- [ ] Export data barter ke PDF/Excel
- [ ] Integrasi dengan email kampus untuk verifikasi akun
- [ ] Dark mode UI

---

## 📄 Lisensi

Proyek ini dikembangkan untuk keperluan akademis sebagai Tugas Besar mata kuliah Pemrograman Berorientasi Objek (PBO) di Telkom University.

---

<p align="center">
  Dibuat dengan ❤️ oleh Diki Alif Taufik
</p>
