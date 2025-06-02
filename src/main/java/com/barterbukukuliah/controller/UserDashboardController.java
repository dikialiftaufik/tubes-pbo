package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;
import com.barterbukukuliah.controller.SearchBookController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller untuk UserDashboard.fxml (SplitPane + sidebar),
 * mengelola “Daftar Buku Saya” beserta aksi Edit/Detail/Hapus/Tambah.
 */
public class UserDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button menuBukuSaya;
    @FXML private Button menuCariBuku;
    @FXML private Button menuTransaksi;
    @FXML private Button menuRating;
    @FXML private Button menuNotifikasi;
    @FXML private Button menuProfile;
    @FXML private Button menuLogout;

    @FXML private TableView<Book> bukuTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colJudul;
    @FXML private TableColumn<Book, String> colPenulis;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colMatkul;
    @FXML private TableColumn<Book, String> colKondisi;
    @FXML private TableColumn<Book, Double> colHarga;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colAksi;
    @FXML private Button addBookButton;
    @FXML private BorderPane mainPane; 

    private final BookDAO bookDAO = new BookDAO();
    private User currentUser;
    private ObservableList<Book> bukuList = FXCollections.observableArrayList();

    private Pane bukuSayaPane; // Simpan layout daftar buku saya

    /**
     * Dipanggil dari AuthController setelah login sukses.
     * Meng‐set user saat ini, inisialisasi kolom, dan muat data.
     */
    public void setUser(User user) {
        this.currentUser = user;
    welcomeLabel.setText("Selamat datang, " + user.getNama() + "!");
    initializeTableColumns();

    // Load layout daftar buku saya sekali dan simpan
    bukuSayaPane = createBukuSayaPane();

    // Tampilkan layout buku saya di mainPane center
    mainPane.setCenter(bukuSayaPane);

    loadBukuSaya();
    }

    private Pane createBukuSayaPane() {
    VBox root = new VBox(10);
    root.setPadding(new Insets(20));

    HBox header = new HBox(10);
    header.setAlignment(Pos.CENTER_LEFT);

    Label title = new Label("Daftar Buku Saya");
    title.setStyle("-fx-font-size: 24px; -fx-text-fill: #212121; -fx-font-weight: bold;");

    Button tambah = new Button("Tambah Buku");
    tambah.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    tambah.setOnAction(e -> handleAddBook());

    HBox spacer = new HBox();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    header.getChildren().addAll(title, spacer, tambah);

    // Pasang tableView yang sudah ada
    bukuTable.setPrefHeight(600);
    VBox.setVgrow(bukuTable, Priority.ALWAYS);

    root.getChildren().addAll(header, bukuTable);

    return root;
}


    /**
     * Inisialisasi kolom TableView, termasuk kolom “Aksi” (Edit, Detail, Hapus).
     */
    private void initializeTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPenulis.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colMatkul.setCellValueFactory(new PropertyValueFactory<>("mataKuliah"));
        colKondisi.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("hargaAsli"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKetersediaan"));

        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn   = new Button("Edit");
            private final Button detailBtn = new Button("Detail");
            private final Button delBtn    = new Button("Hapus");
            private final HBox container   = new HBox(5, editBtn, detailBtn, delBtn);

            {
                editBtn.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 12px;");
                detailBtn.setStyle("-fx-background-color:rgb(229, 210, 40); -fx-text-fill: black; -fx-font-size: 12px;");
                delBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 12px;");

                container.setAlignment(Pos.CENTER);
                container.setPadding(new Insets(2));

                editBtn.setOnAction(evt -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleEditBook(book);
                });

                detailBtn.setOnAction(evt -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showBookDetail(book);
                });

                delBtn.setOnAction(evt -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleDeleteBook(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    /**
     * Public agar AddBookController bisa memanggilnya setelah menutup form Tambah Buku.
     */
    public void loadBukuSaya() {
        try {
            List<Book> list = bookDAO.findByPemilik(currentUser.getIdUser());
            bukuList.setAll(list);
            bukuTable.setItems(bukuList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data 'Buku Saya'.");
        }
    }

    /**
     * Aksi tombol “Tambah Buku”: buka AddBook.fxml fullscreen,
     * menggunakan getClass().getResource(...) dengan leading slash.
     */
    @FXML
    private void handleAddBook() {
        try {
            // Cari resource FXML via getClass().getResource dengan leading slash
            URL fxmlLocation = getClass().getResource("/com/barterbukukuliah/fxml/AddBook.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML AddBook.fxml tidak ditemukan di resources/com/barterbukukuliah/fxml/");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Pane root = loader.load();

            // Inject currentUser ke AddBookController
            AddBookController controller = loader.getController();
            controller.setUser(currentUser);

            // Simpan reference parent controller untuk refresh
            Scene scene = new Scene(root);
            scene.setUserData(this);

            Stage stage = new Stage();
            stage.setTitle("Tambah Buku - Barter Buku Kuliah");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka form Tambah Buku.");
        }
    }

    @FXML
private void showBukuSaya() {
    mainPane.setCenter(bukuSayaPane);  // Ganti konten center dengan layout buku saya
    loadBukuSaya();
}

    
    @FXML
    private void showCariBuku() {
        try {
            URL fxmlLocation = getClass().getResource("/com/barterbukukuliah/fxml/SearchBook.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML SearchBook.fxml tidak ditemukan.");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Pane root = loader.load();
    
            // Inject currentUser
            SearchBookController controller = loader.getController();
            controller.setUser(currentUser);
    
            Stage stage = new Stage();
            stage.setTitle("Cari Buku - Barter Buku Kuliah");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Cari Buku.");
        }
    }    

    @FXML
private void showTransaksi() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/TransaksiMasukView.fxml"));
        Pane transaksiPane = loader.load();

        TransaksiMasukController controller = loader.getController();
        controller.setCurrentUser(currentUser);  // Kirim user yang login ke controller transaksi

        mainPane.setCenter(transaksiPane); // Ganti content center jadi transaksi
    } catch (Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman Transaksi.");
    }
}
@FXML private void showRating()     { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Rating' sedang dalam pengembangan."); }
    @FXML private void showNotifikasi() { showAlert(Alert.AlertType.INFORMATION, "Info", "Fitur 'Notifikasi' sedang dalam pengembangan."); }

    /**
     * Aksi tombol “Profil”: buka ProfileView.fxml fullscreen.
     */
    @FXML
    private void showProfil() {
        try {
            URL fxmlLocation = getClass().getResource("/com/barterbukukuliah/fxml/ProfileView.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML ProfileView.fxml tidak ditemukan.");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
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
     * Aksi tombol “Logout”: tutup dashboard dan kembali ke Login fullscreen.
     */
    @FXML
    private void handleLogoutAction() {
        Stage stage = (Stage) menuLogout.getScene().getWindow();
        stage.close();

        try {
            URL fxmlLocation = getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML Login.fxml tidak ditemukan.");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
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
     * Menampilkan jendela detail buku, berisi:
     * - Slider untuk foto_path_1 sampai foto_path_3 (jika ada)
     * - Deskripsi lengkap di bawahnya
     */
    public void showBookDetail(Book book) {
        // 1. Kumpulkan semua path gambar yang tidak null/blank
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

        // 2. Buat list Image dari path tersebut
        List<Image> images = new ArrayList<>();
        for (String p : paths) {
            try {
                // Jika path sudah berupa URI file:... maka langsung new Image(p)
                Image img = new Image(p);
                images.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 3. Jika tidak ada gambar, tampilkan alert dan keluar
        if (images.isEmpty()) {
            // Hanya menampilkan deskripsi jika memang tidak ada gambar
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detail Buku");
            alert.setHeaderText(book.getJudul());
            String desc = (book.getDeskripsi() == null || book.getDeskripsi().isBlank())
                          ? "Tidak ada deskripsi."
                          : book.getDeskripsi();
            alert.setContentText(desc);
            alert.showAndWait();
            return;
        }

        // 4. Siapkan ImageView untuk slider
        ImageView imageView = new ImageView(images.get(0));
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        // 5. Tombol prev/next untuk slider
        Button prevButton = new Button("<");
        Button nextButton = new Button(">");

        prevButton.setDisable(true);
        if (images.size() == 1) {
            nextButton.setDisable(true);
        }

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

        HBox sliderControls = new HBox(10, prevButton, nextButton);
        sliderControls.setAlignment(Pos.CENTER);

        VBox sliderBox = new VBox(10, imageView, sliderControls);
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.setPadding(new Insets(10));

        // 6. Deskripsi lengkap di bawah slider
        String descText = (book.getDeskripsi() == null || book.getDeskripsi().isBlank())
                          ? "Tidak ada deskripsi."
                          : book.getDeskripsi();
        Label descLabel = new Label("Deskripsi:");
        descLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextArea descArea = new TextArea(descText);
        descArea.setWrapText(true);
        descArea.setEditable(false);
        descArea.setPrefRowCount(5);
        descArea.setStyle("-fx-font-size: 14px;");

        VBox contentBox = new VBox(15, sliderBox, descLabel, descArea);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));

        // 7. Bungkus dengan ScrollPane agar bisa digulir jika tinggi konten melebihi layar
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);

        // 8. Buat stage baru untuk menampilkan detail
        Stage detailStage = new Stage();
        detailStage.setTitle("Detail Buku: " + book.getJudul());
        detailStage.setScene(new Scene(scrollPane));
        detailStage.setWidth(600);
        detailStage.setHeight(700);
        detailStage.show();
    }


    /**
     * Edit buku: sementara hanya menampilkan alert placeholder.
     */
    private void handleEditBook(Book book) {
        try {
            // Pastikan path FXML benar (leading slash)
            URL fxmlLocation = getClass().getResource("/com/barterbukukuliah/fxml/EditBook.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML EditBook.fxml tidak ditemukan.");
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Pane root = loader.load();
    
            EditBookController controller = loader.getController();
            controller.setData(currentUser, book);
    
            // Simpan parent untuk refresh
            Scene scene = new Scene(root);
            scene.setUserData(this);
    
            Stage stage = new Stage();
            stage.setTitle("Edit Buku: " + book.getJudul());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka form Edit Buku.");
        }
    }    

    /**
     * Hapus buku dengan konfirmasi.
     */
    private void handleDeleteBook(Book book) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText("Hapus Buku");
        confirm.setContentText("Anda yakin ingin menghapus buku \"" + book.getJudul() + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                bookDAO.deleteBook(book.getIdBuku());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dihapus.");
                loadBukuSaya();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus buku.");
            }
        }
    }

    /**
     * Utility untuk menampilkan alert sederhana.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}