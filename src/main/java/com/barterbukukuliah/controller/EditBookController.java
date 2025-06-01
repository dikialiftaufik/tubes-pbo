package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

/**
 * Controller untuk EditBook.fxml.
 * Mengisi field dengan data lama, validasi, upload pengganti gambar, lalu panggil updateBook().
 */
public class EditBookController {

    @FXML private TextField judulField;
    @FXML private TextField penulisField;
    @FXML private TextField isbnField;
    @FXML private TextField mataKuliahField;
    @FXML private ComboBox<String> kondisiCombo;
    @FXML private TextArea deskripsiArea;
    @FXML private TextField hargaField;
    @FXML private ImageView imageView1;
    @FXML private ImageView imageView2;
    @FXML private ImageView imageView3;
    @FXML private Button btnChoose1;
    @FXML private Button btnChoose2;
    @FXML private Button btnChoose3;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private File chosenFile1;
    private File chosenFile2;
    private File chosenFile3;

    private User currentUser;
    private Book currentBook;
    private final BookDAO bookDAO = new BookDAO();

    /**
     * Dipanggil setelah FXML di‐load, untuk meng‐set user + buku yang diedit.
     */
    public void setData(User user, Book book) {
        this.currentUser = user;
        this.currentBook = book;
        loadBookData();
    }

    /**
     * Muat data buku ke dalam form, termasuk gambar (jika ada).
     */
    private void loadBookData() {
        judulField.setText(currentBook.getJudul());
        penulisField.setText(currentBook.getPenulis());
        isbnField.setText(currentBook.getIsbn());
        mataKuliahField.setText(currentBook.getMataKuliah());
        kondisiCombo.setValue(currentBook.getKondisi());
        deskripsiArea.setText(currentBook.getDeskripsi());
        hargaField.setText(String.valueOf((long) currentBook.getHargaAsli()));

        // Muat gambar lama, jika path tidak null/blank
        try {
            if (currentBook.getFotoPath1() != null && !currentBook.getFotoPath1().isBlank()) {
                imageView1.setImage(new Image(currentBook.getFotoPath1()));
            }
            if (currentBook.getFotoPath2() != null && !currentBook.getFotoPath2().isBlank()) {
                imageView2.setImage(new Image(currentBook.getFotoPath2()));
            }
            if (currentBook.getFotoPath3() != null && !currentBook.getFotoPath3().isBlank()) {
                imageView3.setImage(new Image(currentBook.getFotoPath3()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChooseImage1() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar 1");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        File file = chooser.showOpenDialog(judulField.getScene().getWindow());
        if (file != null) {
            if (!validateImageFile(file)) return;
            chosenFile1 = file;
            imageView1.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleChooseImage2() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar 2");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        File file = chooser.showOpenDialog(judulField.getScene().getWindow());
        if (file != null) {
            if (!validateImageFile(file)) return;
            chosenFile2 = file;
            imageView2.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleChooseImage3() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar 3");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        File file = chooser.showOpenDialog(judulField.getScene().getWindow());
        if (file != null) {
            if (!validateImageFile(file)) return;
            chosenFile3 = file;
            imageView3.setImage(new Image(file.toURI().toString()));
        }
    }

    private boolean validateImageFile(File file) {
        String name = file.getName().toLowerCase();
        if (!(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))) {
            showAlert(Alert.AlertType.ERROR, "Format Salah", "File harus JPG/JPEG/PNG.");
            return false;
        }
        if (file.length() > 2 * 1024 * 1024) {
            showAlert(Alert.AlertType.ERROR, "File Terlalu Besar", "Ukuran gambar maksimal 2MB.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleSaveAction() {
        String judul = judulField.getText().trim();
        String penulis = penulisField.getText().trim();
        String isbn = isbnField.getText().trim();
        String mataKuliah = mataKuliahField.getText().trim();
        String kondisi = (kondisiCombo.getValue() == null) ? "" : kondisiCombo.getValue();
        String deskripsi = deskripsiArea.getText().trim();
        String hargaText = hargaField.getText().trim();

        // 1. Validasi Judul
        if (judul.isEmpty() || judul.length() > 255) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Judul tidak boleh kosong dan maksimal 255 karakter.");
            return;
        }
        // 2. Validasi Penulis
        if (penulis.isEmpty() || penulis.length() > 255) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Penulis tidak boleh kosong dan maksimal 255 karakter.");
            return;
        }
        // 3. Validasi ISBN
        if (!isbn.isEmpty() && !isbn.matches("\\d{10}|\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "ISBN harus 10 atau 13 digit angka.");
            return;
        }
        // 4. Validasi Mata Kuliah
        if (mataKuliah.isEmpty() || mataKuliah.length() > 100) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Mata Kuliah tidak boleh kosong dan maksimal 100 karakter.");
            return;
        }
        // 5. Validasi Kondisi
        if (kondisi.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Silakan pilih kondisi buku.");
            return;
        }
        // 6. Validasi Deskripsi
        if (deskripsi.length() > 500) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Deskripsi maksimal 500 karakter.");
            return;
        }
        // 7. Validasi Harga
        long harga;
        try {
            harga = Long.parseLong(hargaText);
            if (harga < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Harga harus berupa angka positif.");
            return;
        }

        // 8. Validasi jumlah dan ukuran gambar (maks total 6MB)
        long totalBytes = 0;
        File[] chosenFiles = new File[]{chosenFile1, chosenFile2, chosenFile3};
        for (File f : chosenFiles) {
            if (f != null) totalBytes += f.length();
        }
        if (totalBytes > 6L * 1024 * 1024) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Total ukuran gambar maksimal 6MB.");
            return;
        }

        // 9. Salin file baru, jika ada, ke uploads/books/
        String p1 = currentBook.getFotoPath1();
        String p2 = currentBook.getFotoPath2();
        String p3 = currentBook.getFotoPath3();

        try {
            File dirUploads = new File("uploads/books/");
            if (!dirUploads.exists()) dirUploads.mkdirs();

            if (chosenFile1 != null) {
                String ext = chosenFile1.getName().substring(chosenFile1.getName().lastIndexOf('.'));
                String filename = "book_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + "_1" + ext;
                File dest = new File(dirUploads, filename);
                Files.copy(chosenFile1.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                p1 = dest.toURI().toString();
            }
            if (chosenFile2 != null) {
                String ext = chosenFile2.getName().substring(chosenFile2.getName().lastIndexOf('.'));
                String filename = "book_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + "_2" + ext;
                File dest = new File(dirUploads, filename);
                Files.copy(chosenFile2.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                p2 = dest.toURI().toString();
            }
            if (chosenFile3 != null) {
                String ext = chosenFile3.getName().substring(chosenFile3.getName().lastIndexOf('.'));
                String filename = "book_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + "_3" + ext;
                File dest = new File(dirUploads, filename);
                Files.copy(chosenFile3.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                p3 = dest.toURI().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan file gambar.");
            return;
        }

        // 10. Perbarui objek Book dan panggil updateBook()
        currentBook.setJudul(judul);
        currentBook.setPenulis(penulis);
        currentBook.setIsbn(isbn.isEmpty() ? null : isbn);
        currentBook.setMataKuliah(mataKuliah);
        currentBook.setKondisi(kondisi);
        currentBook.setDeskripsi(deskripsi.isEmpty() ? null : deskripsi);
        currentBook.setHargaAsli(harga);
        currentBook.setFotoPath1(p1);
        currentBook.setFotoPath2(p2);
        currentBook.setFotoPath3(p3);

        try {
            bookDAO.updateBook(currentBook);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil diperbarui.");
            // Tutup window EditBook
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            // Refresh daftar buku di Dashboard
            UserDashboardController parent = (UserDashboardController) saveButton.getScene().getUserData();
            if (parent != null) parent.loadBukuSaya();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui data buku.");
        }
    }

    @FXML
    private void handleCancelAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}