package com.barterbukukuliah.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.util.ValidationUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Controller untuk ResetPassword.fxml.
 * Mengizinkan user memasukkan password baru dan meng‐update ke database.
 */
public class ResetPasswordController {

    @FXML private Label emailLabel;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;

    private String email;  // Akan di‐set dari ForgotPasswordController
    private final UserDAO userDAO = new UserDAO();

    /**
     * Dipanggil dari ForgotPasswordController.setEmail(...)
     */
    public void setEmail(String email) {
        this.email = email;
        emailLabel.setText(email);
    }

    @FXML
    private void handleSaveAction() {
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        // 1. Validasi kosong
        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form tidak boleh kosong", "Silakan isi semua field password.");
            return;
        }

        // 2. Validasi kekuatan password (minimal 8 karakter, huruf+angka)
        if (!ValidationUtil.isPasswordStrong(newPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password lemah", "Password minimal 8 karakter dan mengandung huruf serta angka.");
            return;
        }

        // 3. Validasi konfirmasi
        if (!newPassword.equals(confirmNewPassword)) {
            showAlert(Alert.AlertType.ERROR, "Konfirmasi password tidak cocok", "Password dan konfirmasi harus sama.");
            return;
        }

        // 4. Update di database
        try {
            userDAO.updatePasswordByEmail(email, newPassword);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Password berhasil di‐update. Silakan login kembali.");

            // Tutup window Reset Password
            Stage currentStage = (Stage) emailLabel.getScene().getWindow();
            currentStage.close();

            // Buka kembali Login.fxml fullscreen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal meng‐update password.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        // Tutup window Reset Password
        Stage currentStage = (Stage) emailLabel.getScene().getWindow();
        currentStage.close();

        // Buka Login.fxml fullscreen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
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
