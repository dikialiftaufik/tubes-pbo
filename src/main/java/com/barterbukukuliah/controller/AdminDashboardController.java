package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller untuk AdminDashboard.fxml (SplitPane + sidebar yang bisa dilebarkan).
 */
public class AdminDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button menuManageUsers;
    @FXML private Button menuManageBooks;
    @FXML private Button menuManageTransactions;
    @FXML private Button menuReports;
    @FXML private Button menuProfile;     // Tombol Profil di sidebar
    @FXML private Button menuLogout;      // Tombol Logout di sidebar

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
    private User currentUser;  // Admin yang sedang login

    /**
     * Dipanggil dari AuthController setelah login sukses.
     * Mengatur nama admin, lalu memuat daftar user.
     */
    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Selamat datang, " + user.getNama() + " (Admin)");
        initializeTableColumns();
        loadAllUsers();
    }

    /**
     * Inisialisasi kolom TableView dan buat tombol “Lihat” untuk setiap baris.
     */
    private void initializeTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colUserNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colUserNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserStatus.setCellValueFactory(new PropertyValueFactory<>("statusAkun"));
        colUserTrust.setCellValueFactory(new PropertyValueFactory<>("trustScore"));

        colLihatDetail.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Lihat");

            {
                btn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px;");
                btn.setOnAction(evt -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    showUserDetail(selectedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    /**
     * Memuat semua user dari database dan menampilkan di userTable.
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
     * Aksi untuk tombol “Refresh Data”.
     */
    @FXML
    private void refreshUsers() {
        loadAllUsers();
    }

    // Placeholder untuk menu lain
    @FXML private void showManageUsers() { loadAllUsers(); }
    @FXML private void showManageBooks() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Manage Books' sedang dalam pengembangan."); }
    @FXML private void showManageTransactions() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Manage Transactions' sedang dalam pengembangan."); }
    @FXML private void showReports() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Reports' sedang dalam pengembangan."); }

    /**
     * Aksi tombol “Profil” di sidebar: buka halaman view profil Admin fullscreen.
     */
    @FXML
    private void showProfilAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/ProfileView.fxml"));
            Pane root = loader.load();

            ProfileViewController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = new Stage();
            stage.setTitle("Profil Saya - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Profil.");
        }
    }

    /**
     * Aksi tombol “Logout” di sidebar: tutup dashboard dan buka Login fullscreen.
     */
    @FXML
    private void handleLogoutAction() {
        Stage stage = (Stage) menuLogout.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
            Pane root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login - Barter Buku Kuliah");
            loginStage.setScene(new Scene(root));
            loginStage.setMaximized(true);
            loginStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Login.");
        }
    }

    /**
     * Menampilkan detail user lain (ketika Admin klik “Lihat” di tabel).
     */
    private void showUserDetail(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/ProfileView.fxml"));
            Pane root = loader.load();

            ProfileViewController controller = loader.getController();
            controller.setUser(user);

            Stage detailStage = new Stage();
            detailStage.setTitle("Detail Pengguna: " + user.getNama());
            detailStage.setScene(new Scene(root));
            detailStage.setMaximized(true);
            detailStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
