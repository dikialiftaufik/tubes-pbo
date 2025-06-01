package com.barterbukukuliah.controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller untuk EditProfile.fxml.
 * Mengelola load data user, validasi, upload gambar, dan simpan perubahan.
 */
public class ProfileController {

    @FXML private TextField namaField;
    @FXML private TextField telpField;
    @FXML private TextField fakultasField;
    @FXML private TextField prodiField;
    @FXML private TextField angkatanField;
    @FXML private TextArea alamatField;
    @FXML private ImageView fotoImageView;
    @FXML private Button saveButton;

    private User currentUser;
    private final UserDAO userDAO = new UserDAO();

    // Menyimpan referensi file gambar yang dipilih (jika ada)
    private File chosenImageFile;

    /**
     * Dipanggil setelah FXML di‐load, untuk meng‐set user dan memuat data awal.
     */
    public void setUser(User user) {
        this.currentUser = user;
        loadUserData();
    }

    /**
     * Muat data user ke dalam field UI.
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

        // Muat foto profil (fallback ke default jika null/blank)
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
     * Aksi tombol “Pilih Gambar”: buka FileChooser untuk memilih file gambar.
     */
    @FXML
    private void handleChooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Foto Profil");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
        Stage stage = (Stage) namaField.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            // Validasi ukuran file ≤ 2MB
            if (file.length() > 2 * 1024 * 1024) {
                showAlert(Alert.AlertType.ERROR, "File Terlalu Besar", "Ukuran gambar maksimal 2MB.");
                return;
            }
            // Simpan sementara untuk disalin saat simpan action
            chosenImageFile = file;
            try {
                // Tampilkan preview
                Image img = new Image(new FileInputStream(file));
                fotoImageView.setImage(img);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat gambar.");
            }
        }
    }

    /**
     * Aksi tombol “Simpan Perubahan”: validasi input & simpan ke database.
     */
    @FXML
    private void handleSaveAction() {
        String nama = namaField.getText().trim();
        String telp = telpField.getText().trim();
        String fakultas = fakultasField.getText().trim();
        String prodi = prodiField.getText().trim();
        String angkatanStr = angkatanField.getText().trim();
        String alamat = alamatField.getText().trim();

        // 1. Validasi: Nama tidak boleh kosong
        if (nama.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Nama tidak boleh kosong.");
            return;
        }
        // 2. Validasi telepon (boleh kosong, tapi jika diisi, harus numeric 10–15 digit)
        if (!telp.isEmpty() && !telp.matches("\\d{10,15}")) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Nomor telepon harus 10–15 digit angka.");
            return;
        }
        // 3. Validasi fakultas & prodi (boleh kosong, tetapi jika diisi tidak lebih dari 100 char)
        if (fakultas.length() > 100) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Fakultas maksimal 100 karakter.");
            return;
        }
        if (prodi.length() > 100) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Program Studi maksimal 100 karakter.");
            return;
        }
        // 4. Validasi angkatan (boleh kosong, tapi jika diisi harus berupa tahun 4 digit antara 2000–2100)
        int angkatan = 0;
        if (!angkatanStr.isEmpty()) {
            if (!angkatanStr.matches("\\d{4}")) {
                showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Angkatan harus 4 digit tahun (misal: 2022).");
                return;
            }
            angkatan = Integer.parseInt(angkatanStr);
            if (angkatan < 2000 || angkatan > 2100) {
                showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Angkatan harus antara 2000–2100.");
                return;
            }
        }
        // 5. Validasi alamat (boleh kosong, maksimal 500 karakter)
        if (alamat.length() > 500) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Alamat maksimal 500 karakter.");
            return;
        }

        // 6. Jika ada file gambar yang dipilih (chosenImageFile), salin ke folder uploads/profile/
        String newFotoPath = currentUser.getFotoProfil(); // default = path lama atau fallback
        if (chosenImageFile != null) {
            try {
                // Pastikan direktori target ada: <working_dir>/uploads/profile/
                File uploadsDir = new File("uploads/profile/");
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }
                // Nama file baru: user_<id_user>_<timestamp>_<originalName>
                String original = chosenImageFile.getName();
                String ext = original.substring(original.lastIndexOf('.'));
                String filename = "user_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + ext;
                File dest = new File(uploadsDir, filename);
                // Copy file
                Files.copy(chosenImageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // Simpan path sebagai URI (prefix file:)
                newFotoPath = dest.toURI().toString();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan file gambar.");
                return;
            }
        }

        // 7. Buat objek User baru untuk update
        User updatedUser = new User();
        updatedUser.setIdUser(currentUser.getIdUser());
        updatedUser.setNama(nama);
        updatedUser.setNomorTelepon(telp.isEmpty() ? null : telp);
        updatedUser.setFakultas(fakultas.isEmpty() ? null : fakultas);
        updatedUser.setProgramStudi(prodi.isEmpty() ? null : prodi);
        updatedUser.setAngkatan(angkatan);
        updatedUser.setAlamat(alamat.isEmpty() ? null : alamat);
        updatedUser.setFotoProfil(newFotoPath);

        // 8. Simpan ke database
        try {
            userDAO.updateProfile(updatedUser);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Profil berhasil diperbarui.");

            // Update currentUser (supaya jika window detail dibuka lagi, data ter‐refresh)
            currentUser.setNama(nama);
            currentUser.setNomorTelepon(updatedUser.getNomorTelepon());
            currentUser.setFakultas(updatedUser.getFakultas());
            currentUser.setProgramStudi(updatedUser.getProgramStudi());
            currentUser.setAngkatan(updatedUser.getAngkatan());
            currentUser.setAlamat(updatedUser.getAlamat());
            currentUser.setFotoProfil(updatedUser.getFotoProfil());

            // Tutup window Edit Profile
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui data profil.");
        }
    }

    /**
     * Aksi tombol “Batal”: tutup form Edit Profile dan kembali ke Dashboard User.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) namaField.getScene().getWindow();
        stage.close();
    }

    /**
     * Utility untuk menampilkan alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
