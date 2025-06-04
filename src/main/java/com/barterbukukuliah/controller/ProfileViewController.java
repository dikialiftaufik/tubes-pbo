package com.barterbukukuliah.controller;

import com.barterbukukuliah.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller untuk halaman ProfileView.fxml.
 * Menampilkan data user dengan field disabled, dan tombol Edit Profile.
 */
public class ProfileViewController {

    @FXML private ImageView fotoImageView;
    @FXML private TextField namaField;
    @FXML private TextField telpField;
    @FXML private TextField fakultasField;
    @FXML private TextField prodiField;
    @FXML private TextField angkatanField;
    @FXML private TextArea alamatField;
    @FXML private Button backButton;

    private User currentUser;

    /**
     * Dipanggil setelah FXML di‐load, untuk meng‐set user dan memuat data.
     */
    public void setUser(User user) {
        this.currentUser = user;
        loadUserData();
    }

    /**
     * Muat data user ke field (semua disabled).
     */
    private void loadUserData() {
        namaField.setText(currentUser.getNama());
        telpField.setText(currentUser.getNomorTelepon());
        fakultasField.setText(currentUser.getFakultas());
        prodiField.setText(currentUser.getProgramStudi());
        if (currentUser.getAngkatan() > 0) {
            angkatanField.setText(String.valueOf(currentUser.getAngkatan()));
        }
        alamatField.setText(currentUser.getAlamat());

        // Muat foto profil: cek apakah foto_profil dimulai "file:" atau resource path
        String path = (currentUser.getFotoProfil() != null && !currentUser.getFotoProfil().isBlank())
                      ? currentUser.getFotoProfil()
                      : "/images/profile/default_avatar.png";
        try {
            Image img;
            if (path.startsWith("file:")) {
                img = new Image(path);
            } else {
                img = new Image(getClass().getResourceAsStream(path));
            }
            fotoImageView.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aksi tombol “Edit Profile”: tutup view ini, lalu buka EditProfile.fxml.
     */
    @FXML
    private void handleEditProfile() {
        try {
            // Tutup window ProfileView
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.close();

            // Buka EditProfile.fxml fullscreen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/EditProfile.fxml"));
            Pane root = loader.load();

            ProfileController controller = loader.getController();
            controller.setUser(currentUser);

            Stage editStage = new Stage();
            editStage.setTitle("Edit Profil - Barter Buku Kuliah");
            editStage.setScene(new Scene(root));
            editStage.setMaximized(true);
            editStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackAction() {
        // Mendapatkan stage saat ini dari salah satu komponen UI, misalnya backButton
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close(); // Menutup jendela ProfileView

        // Asumsi: Pengguna kembali ke UserDashboard atau AdminDashboard
        // Jika ProfileView selalu dibuka dari UserDashboardController atau AdminDashboardController
        // dan dashboard tersebut masih ada di background, maka menutup ProfileView
        // secara otomatis akan mengembalikan pengguna ke dashboard.
        // Jika ada logika navigasi yang lebih kompleks (misalnya, ProfileView menggantikan
        // scene dashboard), Anda perlu menambahkan kode untuk memuat ulang FXML dashboard di sini.
        // Untuk kasus umum di mana ProfileView adalah jendela baru, cukup menutupnya.
    }

    // Utility untuk menampilkan alert (opsional, jika belum ada atau ingin standarisasi)
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
