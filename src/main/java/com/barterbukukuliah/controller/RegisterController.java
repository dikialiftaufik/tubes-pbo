package com.barterbukukuliah.controller;

import java.io.IOException;

import com.barterbukukuliah.model.User;
import com.barterbukukuliah.service.UserService;
import com.barterbukukuliah.util.ValidationUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller untuk halaman registrasi (Register.fxml).
 * Hanya field: NIM, Email, Password, Confirm Password.
 */
public class RegisterController {

    @FXML private TextField nimField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleRegisterAction() {
        String nim = nimField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Validasi input kosong
        if (nim.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form tidak boleh kosong", "Silakan isi semua field yang wajib.");
            return;
        }

        // 2. Validasi NIM (misal minimal 6 digit, angka saja) – sesuaikan aturan jika perlu
        if (!nim.matches("\\d{6,}")) {
            showAlert(Alert.AlertType.ERROR, "NIM tidak valid", "NIM harus berupa minimal 6 digit angka.");
            return;
        }

        // 3. Validasi format email
        if (!ValidationUtil.isEmailFormatValid(email)) {
            showAlert(Alert.AlertType.ERROR, "Format email tidak valid", "Masukkan email dengan format yang benar.");
            return;
        }

        // 4. Validasi domain Telkom University
        if (!ValidationUtil.isTelkomDomain(email)) {
            showAlert(Alert.AlertType.ERROR, "Domain email tidak sesuai",
                      "Email harus berakhiran @student.telkomuniversity.ac.id atau @tass.telkomuniversity.ac.id.");
            return;
        }

        // 5. Validasi kekuatan password (minimal 8 karakter, huruf+angka)
        if (!ValidationUtil.isPasswordStrong(password)) {
            showAlert(Alert.AlertType.ERROR, "Password lemah", "Password minimal 8 karakter dan mengandung huruf serta angka.");
            return;
        }

        // 6. Validasi confirm password
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Konfirmasi password tidak cocok", "Pastikan password dan konfirmasi sama.");
            return;
        }

        // 7. Siapkan objek User (hanya nim, email, password di‐set sekarang)
        User newUser = new User();
        newUser.setNim(nim);
        newUser.setEmail(email);
        newUser.setPasswordHash(password); // plaintext; DAO akan meng‐hash

        try {
            userService.register(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun Anda telah dibuat. Silakan login.");
        
            // Tutup window registrasi
            Stage currentStage = (Stage) nimField.getScene().getWindow();
            currentStage.close();
        
            // Buka Login.fxml fullscreen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);   // <-- tambahan
            stage.show();
        
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", ex.getMessage());
            ex.printStackTrace();
        }        
    }

    @FXML
private void handleBackToLogin() {
    // Tutup window registrasi
    Stage currentStage = (Stage) nimField.getScene().getWindow();
    currentStage.close();

    // Buka kembali Login.fxml dalam fullscreen
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Login - Barter Buku Kuliah");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);   // <-- tambahan
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Login.");
    }
}

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
