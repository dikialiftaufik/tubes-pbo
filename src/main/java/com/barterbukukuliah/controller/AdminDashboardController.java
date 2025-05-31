package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.UserDAO;
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

import java.util.List;

/**
 * Controller untuk AdminDashboard.fxml.
 * Menampilkan seluruh user dan memungkinkan admin melihat detail setiap akun,
 * termasuk foto profil dengan fallback ke default_avatar.png bila tidak ada.
 */
public class AdminDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button menuManageUsers;
    @FXML private Button menuManageBooks;
    @FXML private Button menuManageTransactions;
    @FXML private Button menuReports;
    @FXML private Button menuSettings;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUserNama;
    @FXML private TableColumn<User, String> colUserNim;
    @FXML private TableColumn<User, String> colUserEmail;
    @FXML private TableColumn<User, String> colUserRole;
    @FXML private TableColumn<User, String> colUserStatus;
    @FXML private TableColumn<User, Double> colUserTrust;
    @FXML private TableColumn<User, Void> colLihatDetail;
    @FXML private Button refreshButton;

    private final UserDAO userDAO = new UserDAO();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    /**
     * Dipanggil dari AuthController setelah login sukses.
     * Meng‐set nama admin, lalu memuat daftar user.
     */
    public void setUser(User user) {
        // Tampilkan nama admin di welcomeLabel
        welcomeLabel.setText("Selamat datang, " + user.getNama() + " (Admin)");
        initializeTableColumns();
        loadAllUsers();
    }

    /**
     * Inisialisasi kolom TableView untuk menampilkan properti User,
     * dan buat cell factory untuk kolom "Detail".
     */
    private void initializeTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colUserNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colUserNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserStatus.setCellValueFactory(new PropertyValueFactory<>("statusAkun"));
        colUserTrust.setCellValueFactory(new PropertyValueFactory<>("trustScore"));

        // Kolom "Detail": setiap baris memuat tombol "Lihat"
        colLihatDetail.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Lihat");

            {
                btn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px;");
                btn.setOnAction(evt -> {
                    User user = getTableView().getItems().get(getIndex());
                    showUserDetail(user);
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
     * Muat semua user dari database dan tampilkan di userTable.
     */
    private void loadAllUsers() {
        try {
            List<User> list = userDAO.findAllUsers();
            userList.setAll(list);
            userTable.setItems(userList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat daftar pengguna.");
        }
    }

    /**
     * Aksi tombol Refresh Data: reload daftar user.
     */
    @FXML
    private void refreshUsers() {
        loadAllUsers();
    }

    /**
     * Aksi tombol Logout: tutup dashboard, kembali ke Login.fxml fullscreen.
     */
    @FXML
    private void handleLogoutAction() {
        // Tutup window Admin Dashboard
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

    /*
     * Placeholder untuk menu‐menu lain: hanya menampilkan alert "sedang dalam pengembangan".
     */
    @FXML private void showManageUsers() { loadAllUsers(); }
    @FXML private void showManageBooks() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Manage Books' sedang dalam pengembangan."); }
    @FXML private void showManageTransactions() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Manage Transactions' sedang dalam pengembangan."); }
    @FXML private void showReports() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Reports' sedang dalam pengembangan."); }
    @FXML private void showSettings() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Settings' sedang dalam pengembangan."); }

    /**
     * Menampilkan window baru yang berisi detail informasi lengkap user,
     * termasuk foto profil dengan fallback ke "/images/profile/default_avatar.png".
     *
     * @param user Objek User yang akan ditampilkan detailnya.
     */
    private void showUserDetail(User user) {
        // Layout: VBox agar muncul vertikal
        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(15));

        // Label untuk setiap informasi
        Label lblNama = new Label("Nama         : " + user.getNama());
        Label lblNim = new Label("NIM          : " + user.getNim());
        Label lblEmail = new Label("Email        : " + user.getEmail());
        Label lblNoTelp = new Label("No. Telepon  : " + (user.getNomorTelepon() == null || user.getNomorTelepon().isBlank() ? "-" : user.getNomorTelepon()));
        Label lblFakultas = new Label("Fakultas     : " + (user.getFakultas() == null || user.getFakultas().isBlank() ? "-" : user.getFakultas()));
        Label lblProdi = new Label("Program Studi: " + (user.getProgramStudi() == null || user.getProgramStudi().isBlank() ? "-" : user.getProgramStudi()));
        Label lblAngkatan = new Label("Angkatan     : " + (user.getAngkatan() == 0 ? "-" : user.getAngkatan()));
        Label lblAlamat = new Label("Alamat       : " + (user.getAlamat() == null || user.getAlamat().isBlank() ? "-" : user.getAlamat()));

        // ImageView untuk foto_profil (fallback ke default_avatar.png)
        ImageView imageView = new ImageView();
        try {
            String path = (user.getFotoProfil() != null && !user.getFotoProfil().isBlank())
                          ? user.getFotoProfil()
                          : "/images/profile/default_avatar.png";

            Image img = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(img);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
            // Jika gagal load (misal file tidak ada), kita biarkan kosong atau bisa set teks/gambar fallback
        }

        // Tambahkan semua kontrol ke dalam VBox
        root.getChildren().addAll(
                imageView,
                lblNama,
                lblNim,
                lblEmail,
                lblNoTelp,
                lblFakultas,
                lblProdi,
                lblAngkatan,
                lblAlamat
        );

        // Buat stage baru untuk menampilkan detail
        Stage detailStage = new Stage();
        detailStage.setTitle("Detail Pengguna: " + user.getNama());
        detailStage.setScene(new Scene(root));
        detailStage.setWidth(400);
        detailStage.setHeight(550);
        detailStage.show();
    }

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
