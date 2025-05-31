package com.barterbukukuliah.controller;

import com.barterbukukuliah.model.User;
import com.barterbukukuliah.service.AuthService;
import com.barterbukukuliah.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller untuk halaman Login (Login.fxml).
 * Menangani proses login, navigasi ke registrasi, dan reset password.
 */
public class AuthController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheck;

    private final AuthService authService = new AuthService();

    /**
     * Dipanggil saat user men‐klik tombol LOGIN.
     * Melakukan validasi dan autentikasi, lalu membuka dashboard sesuai role.
     */
    @FXML
    private void handleLoginAction() {
        System.out.println("DEBUG: handleLoginAction() terpanggil. Email input: " + emailField.getText());

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // 1. Validasi input tidak kosong
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form tidak boleh kosong", "Silakan isi email dan password.");
            return;
        }

        // 2. Validasi format email
        if (!ValidationUtil.isEmailFormatValid(email)) {
            showAlert(Alert.AlertType.ERROR, "Format email tidak valid", "Masukkan email dengan format yang benar.");
            return;
        }

        // 3. Validasi domain Telkom University
        if (!ValidationUtil.isTelkomDomain(email)) {
            showAlert(Alert.AlertType.ERROR, "Domain email tidak sesuai",
                      "Email harus berakhiran @student.telkomuniversity.ac.id atau @tass.telkomuniversity.ac.id.");
            return;
        }

        // 4. Proses login via service (cek email, cek akun, cek password)
        try {
            User loggedInUser = authService.login(email, password);

            // Jika login berhasil, buka dashboard sesuai role
            String role = loggedInUser.getRole();
            if ("Admin".equalsIgnoreCase(role)) {
                openAdminDashboard(loggedInUser);
            } else {
                openUserDashboard(loggedInUser);
            }

            // Tutup window login
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Membuka form registrasi (Register.fxml) dalam kondisi fullscreen.
     */
    @FXML
    private void handleRegister() {
        try {
            // Tutup window login
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

            // Load Register.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Register.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Register - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Registrasi.");
        }
    }

    /**
     * Membuka form reset password (ForgotPassword.fxml) dalam kondisi fullscreen.
     */
    @FXML
    private void handleForgotPassword() {
        try {
            // Tutup window login
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

            // Load ForgotPassword.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/ForgotPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Reset Password - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Reset Password.");
        }
    }

    /**
     * Buka AdminDashboard.fxml dan kirim data User yang sudah login.
     */
    private void openAdminDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
    
            AdminDashboardController controller = loader.getController();
            controller.setUser(user);
    
            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);   // <-- pastikan ini ada
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka Admin Dashboard.");
        }
    }
    

    /**
     * Buka UserDashboard.fxml dan kirim data User yang sudah login.
     */
    private void openUserDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/UserDashboard.fxml"));
            Parent root = loader.load();

            com.barterbukukuliah.controller.UserDashboardController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("User Dashboard - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka User Dashboard.");
        }
    }

    /**
     * Menampilkan alert dengan tipe, judul, dan pesan yang diberikan.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
