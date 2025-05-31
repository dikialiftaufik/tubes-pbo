package com.barterbukukuliah.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Kelas util untuk mengelola koneksi ke database MySQL.
 * Menggunakan Singleton pattern untuk instance Connection.
 */
public class DatabaseConnection {
    private static Connection connection;

    // Nama file properties yang berada di src/main/resources/com/barterbukukuliah/
    private static final String PROPERTIES_FILE = "/com/barterbukukuliah/database.properties";

    private DatabaseConnection() {
        // private constructor untuk mencegah instantiation
    }

    /**
     * Mengembalikan instance Connection ke database.
     * Jika belum terinisialisasi, maka buat koneksi baru.
     *
     * @return Connection aktif
     * @throws SQLException jika gagal memuat properties, driver, atau konek ke database
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();

            // 1) Muat file database.properties
            try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
                if (input == null) {
                    throw new SQLException(
                        "Tidak dapat menemukan file database.properties di " + PROPERTIES_FILE
                    );
                }
                // props.load() melempar IOException jika gagal membaca stream
                props.load(input);
            } catch (IOException e) {
                throw new SQLException(
                    "Gagal memuat file database.properties: " + e.getMessage(), e
                );
            }

            // 2) Ambil nilai properti
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String pass = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            // 3) Muat driver JDBC dan buat koneksi
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connected: " + url);
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                    "Driver JDBC tidak ditemukan: " + e.getMessage(), e
                );
            }
        }
        return connection;
    }
}
