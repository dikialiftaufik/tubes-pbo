package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller untuk ForgotPassword.fxml.
 * Meminta user memasukkan email yang terdaftar, lalu membuka ResetPassword.fxml jika valid.
 */
public class ForgotPasswordController {

    @FXML private TextField emailField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleSubmitAction() {
        String email = emailField.getText().trim();

        // 1. Validasi input kosong
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form tidak boleh kosong", "Silakan isi email Anda.");
            return;
        }

        // 2. Validasi format email
        if (!com.barterbukukuliah.util.ValidationUtil.isEmailFormatValid(email)) {
            showAlert(Alert.AlertType.ERROR, "Format email tidak valid", "Masukkan email dengan format yang benar.");
            return;
        }

        // 3. Validasi domain Telkom University
        if (!com.barterbukukuliah.util.ValidationUtil.isTelkomDomain(email)) {
            showAlert(Alert.AlertType.ERROR, "Domain email tidak sesuai",
                      "Email harus berakhiran @student.telkomuniversity.ac.id atau @tass.telkomuniversity.ac.id.");
            return;
        }

        // 4. Cek apakah email terdaftar
        try {
            if (userDAO.findByEmail(email) == null) {
                showAlert(Alert.AlertType.ERROR, "Email tidak terdaftar", "Email ini belum terdaftar di sistem.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal mengakses database.");
            return;
        }

        // 5. Simulasi kirim email reset (di sini cukup print ke console)
        System.out.println("DEBUG: Permintaan reset password untuk email: " + email);

        // 6. Buka form ResetPassword.fxml dan kirim email lewat controller
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/ResetPassword.fxml"));
            Parent root = loader.load();

            // Set email di ResetPasswordController
            ResetPasswordController controller = loader.getController();
            controller.setEmail(email);

            Stage stage = new Stage();
            stage.setTitle("Atur Ulang Password - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Tutup window Forgot Password
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Reset Password.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        // Tutup window Forgot Password
        Stage currentStage = (Stage) emailField.getScene().getWindow();
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
