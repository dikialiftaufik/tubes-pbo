package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk UserDashboard.fxml.
 * Menampilkan daftar buku milik user dan slider untuk melihat tiga gambar tiap buku.
 */
public class UserDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button menuBukuSaya;
    @FXML private Button menuCariBuku;
    @FXML private Button menuTransaksi;
    @FXML private Button menuRating;
    @FXML private Button menuNotifikasi;
    @FXML private Button menuProfil;
    @FXML private Button menuPengaturan;
    @FXML private TableView<Book> bukuTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colJudul;
    @FXML private TableColumn<Book, String> colPenulis;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colMatkul;
    @FXML private TableColumn<Book, String> colKondisi;
    @FXML private TableColumn<Book, Double> colHarga;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colLihatGambar;
    @FXML private Button refreshButton;

    private final BookDAO bookDAO = new BookDAO();
    private User currentUser;
    private ObservableList<Book> bukuList = FXCollections.observableArrayList();

    /**
     * Dipanggil setelah user login, untuk meng‐set objek User.
     */
    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Selamat datang, " + user.getNama() + "!");
        initializeTableColumns();
        loadBukuSaya();
    }

    /**
     * Inisialisasi kolom‐kolom TableView, termasuk cell factory untuk "Lihat Gambar" dengan slider.
     */
    private void initializeTableColumns() {
        // Kolom bawaan
        colId.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPenulis.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colMatkul.setCellValueFactory(new PropertyValueFactory<>("mataKuliah"));
        colKondisi.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("hargaAsli"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKetersediaan"));

        // Kolom "Lihat Gambar" dengan slider
        colLihatGambar.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Lihat");

            {
                btn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px;");
                btn.setOnAction(evt -> {
                    Book book = getTableView().getItems().get(getIndex());
                    List<String> paths = new ArrayList<>();
                    if (book.getFotoPath1() != null && !book.getFotoPath1().isBlank()) {
                        paths.add(book.getFotoPath1());
                    }
                    if (book.getFotoPath2() != null && !book.getFotoPath2().isBlank()) {
                        paths.add(book.getFotoPath2());
                    }
                    if (book.getFotoPath3() != null && !book.getFotoPath3().isBlank()) {
                        paths.add(book.getFotoPath3());
                    }
                    if (paths.isEmpty()) {
                        showAlert(Alert.AlertType.INFORMATION, "Info", "Tidak ada gambar untuk buku ini.");
                        return;
                    }
                    showImageSlider(book.getJudul(), paths);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    /**
     * Membuka window slider untuk melihat gambar‐gambar buku.
     *
     * @param title  Judul buku, digunakan sebagai title window.
     * @param paths  Daftar path gambar (relatif dari resources).
     */
    private void showImageSlider(String title, List<String> paths) {
        // Muat semua Image ke list
        List<Image> images = new ArrayList<>();
        for (String p : paths) {
            try {
                Image img = new Image(getClass().getResourceAsStream(p));
                images.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (images.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat gambar.");
            return;
        }

        // Buat ImageView dan index tracker
        ImageView imageView = new ImageView(images.get(0));
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        // Tombol Prev / Next
        Button prevButton = new Button("<");
        Button nextButton = new Button(">");

        prevButton.setDisable(true);
        if (images.size() == 1) {
            nextButton.setDisable(true);
        }

        // Index sebagai array agar bisa di‐modify di lambda
        final int[] index = {0};

        prevButton.setOnAction(e -> {
            if (index[0] > 0) {
                index[0]--;
                imageView.setImage(images.get(index[0]));
                nextButton.setDisable(false);
            }
            if (index[0] == 0) {
                prevButton.setDisable(true);
            }
        });

        nextButton.setOnAction(e -> {
            if (index[0] < images.size() - 1) {
                index[0]++;
                imageView.setImage(images.get(index[0]));
                prevButton.setDisable(false);
            }
            if (index[0] == images.size() - 1) {
                nextButton.setDisable(true);
            }
        });

        HBox controls = new HBox(10, prevButton, nextButton);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, imageView, controls);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        Stage imageStage = new Stage();
        imageStage.setTitle("Gambar Buku: " + title);
        imageStage.setScene(new Scene(root));
        imageStage.show();
    }

    /**
     * Muat data Buku Saya dari database dan tampilkan di TableView.
     */
    private void loadBukuSaya() {
        try {
            List<Book> list = bookDAO.findByPemilik(currentUser.getIdUser());
            bukuList.setAll(list);
            bukuTable.setItems(bukuList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data Buku Saya.");
        }
    }

    /**
     * Action untuk tombol Refresh Data.
     */
    @FXML
    private void refreshBukuSaya() {
        loadBukuSaya();
    }

    /**
     * Action untuk tombol Logout: tutup dashboard, buka kembali login.
     */
    @FXML
    private void handleLogoutAction() {
        // Tutup window Dashboard
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        // Buka kembali Login.fxml fullscreen
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login - Barter Buku Kuliah");
            loginStage.setScene(new javafx.scene.Scene(root));
            loginStage.setMaximized(true);
            loginStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Login.");
        }
    }

    /* Placeholder untuk menu‐menu lain, hanya menampilkan alert. */
    @FXML private void showBukuSaya() { loadBukuSaya(); }
    @FXML private void showCariBuku() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Cari Buku' sedang dalam pengembangan."); }
    @FXML private void showTransaksi() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Transaksi' sedang dalam pengembangan."); }
    @FXML private void showRating() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Rating' sedang dalam pengembangan."); }
    @FXML private void showNotifikasi() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Notifikasi' sedang dalam pengembangan."); }
    @FXML private void showProfil() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Profil' sedang dalam pengembangan."); }
    @FXML private void showPengaturan() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Pengaturan' sedang dalam pengembangan."); }

    /**
     * Utility method untuk menampilkan alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}