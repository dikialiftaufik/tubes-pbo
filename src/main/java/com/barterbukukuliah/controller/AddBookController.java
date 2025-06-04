package com.barterbukukuliah.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddBookController {

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
    private final BookDAO bookDAO = new BookDAO();

    /**
     * Method initialize akan dipanggil otomatis oleh FXMLLoader setelah komponen @FXML diinject.
     * Kita isi ComboBox 'kondisiCombo' di sini.
     */
    @FXML
    private void initialize() {
        kondisiCombo.setItems(FXCollections.observableArrayList(
            "Baru", "Bagus", "Cukup", "Rusak Ringan", "Rusak Sedang"
        ));
    }

    /**
     * Dipanggil sebelum form ditampilkan untuk meng‐set user yang sedang login.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleChooseImage1() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar 1");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
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
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
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
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
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

        // Validasi input (sama seperti sebelumnya)...
        if (judul.isEmpty() || judul.length() > 255) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Judul tidak boleh kosong dan maksimal 255 karakter.");
            return;
        }
        if (penulis.isEmpty() || penulis.length() > 255) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Penulis tidak boleh kosong dan maksimal 255 karakter.");
            return;
        }
        if (!isbn.isEmpty() && !isbn.matches("\\d{10}|\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "ISBN harus 10 atau 13 digit angka.");
            return;
        }
        try {
            if (bookDAO.isIsbnExists(isbn)) {
                showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "ISBN '" + isbn + "' sudah terdaftar untuk buku lain.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memvalidasi ISBN: " + e.getMessage());
            return;
        }
        if (mataKuliah.isEmpty() || mataKuliah.length() > 100) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Mata Kuliah tidak boleh kosong dan maksimal 100 karakter.");
            return;
        }
        if (kondisi.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Silakan pilih kondisi buku.");
            return;
        }
        if (deskripsi.length() > 500) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Deskripsi maksimal 500 karakter.");
            return;
        }

        long harga;
        try {
            harga = Long.parseLong(hargaText);
            if (harga < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Harga harus berupa angka positif.");
            return;
        }

        long totalBytes = 0;
        List<File> chosenFiles = new ArrayList<>();
        if (chosenFile1 != null) chosenFiles.add(chosenFile1);
        if (chosenFile2 != null) chosenFiles.add(chosenFile2);
        if (chosenFile3 != null) chosenFiles.add(chosenFile3);
        for (File f : chosenFiles) {
            totalBytes += f.length();
        }
        if (totalBytes > 6L * 1024 * 1024) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Total ukuran gambar maksimal 6MB.");
            return;
        }

        // Salin gambar ke folder uploads/books/
        String p1 = null, p2 = null, p3 = null;
        try {
            File dirUploads = new File("uploads/books/");
            if (!dirUploads.exists()) dirUploads.mkdirs();

            if (chosenFile1 != null) {
                String ext1 = chosenFile1.getName().substring(chosenFile1.getName().lastIndexOf('.'));
                String filename1 = "book_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + "_1" + ext1;
                File dest1 = new File(dirUploads, filename1);
                Files.copy(chosenFile1.toPath(), dest1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                p1 = dest1.toURI().toString();
            }
            if (chosenFile2 != null) {
                String ext2 = chosenFile2.getName().substring(chosenFile2.getName().lastIndexOf('.'));
                String filename2 = "book_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + "_2" + ext2;
                File dest2 = new File(dirUploads, filename2);
                Files.copy(chosenFile2.toPath(), dest2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                p2 = dest2.toURI().toString();
            }
            if (chosenFile3 != null) {
                String ext3 = chosenFile3.getName().substring(chosenFile3.getName().lastIndexOf('.'));
                String filename3 = "book_" + currentUser.getIdUser() + "_" + System.currentTimeMillis() + "_3" + ext3;
                File dest3 = new File(dirUploads, filename3);
                Files.copy(chosenFile3.toPath(), dest3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                p3 = dest3.toURI().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan file gambar.");
            return;
        }

        // Simpan data buku ke DB
        Book book = new Book();
        book.setIdPemilik(currentUser.getIdUser());
        book.setJudul(judul);
        book.setPenulis(penulis);
        book.setIsbn(isbn.isEmpty() ? null : isbn);
        book.setMataKuliah(mataKuliah);
        book.setKondisi(kondisi);
        book.setDeskripsi(deskripsi.isEmpty() ? null : deskripsi);
        book.setHargaAsli(harga);
        book.setStatusKetersediaan("Tersedia");
        book.setFotoPath1(p1);
        book.setFotoPath2(p2);
        book.setFotoPath3(p3);

        try {
            bookDAO.addBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil ditambahkan.");
            // Tutup form
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            // Refresh di dashboard
            UserDashboardController parent = (UserDashboardController) saveButton.getScene().getUserData();
            if (parent != null) parent.loadBukuSaya();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data buku.");
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
