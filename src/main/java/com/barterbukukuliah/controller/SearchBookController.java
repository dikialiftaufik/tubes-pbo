package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.dao.TransactionDAO;
import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.Book;
import com.barterbukukuliah.model.User;
import com.barterbukukuliah.service.MatchingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchBookController {

    @FXML private Button btnBack;
    @FXML private TextField searchField;
    @FXML private TextField matkulField;
    @FXML private ComboBox<String> kondisiFilter;
    @FXML private ComboBox<String> sortByCombo;
    @FXML private Button btnSearch;
    @FXML private Button btnReset;

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colJudul;
    @FXML private TableColumn<Book, String> colPemilik;
    @FXML private TableColumn<Book, String> colPenulis;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colMatkul;
    @FXML private TableColumn<Book, String> colKondisi;
    @FXML private TableColumn<Book, Double> colHarga;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colAksi;

    private final BookDAO bookDAO = new BookDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final UserDAO userDAO = new UserDAO();

    private User currentUser;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    /** Dipanggil setelah FXML di-load dan user berhasil login. */
    public void setUser(User user) {
        this.currentUser = user;
        initializeTableColumns();
        initializeFilters();
        loadAllBooks();
    }

    private void initializeTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPemilik.setCellValueFactory(new PropertyValueFactory<>("pemilikName"));
        colPenulis.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colMatkul.setCellValueFactory(new PropertyValueFactory<>("mataKuliah"));
        colKondisi.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("hargaAsli"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKetersediaan"));

        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button detailBtn = new Button("Lihat Detail");
            private final Button barterBtn = new Button("Ajukan Barter");
            private final HBox container = new HBox(10, detailBtn, barterBtn);

            {
                detailBtn.setStyle("-fx-background-color: #0288D1; -fx-text-fill: white; -fx-font-size: 12px;");
                barterBtn.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-font-size: 12px;");
                container.setAlignment(Pos.CENTER);
                container.setPadding(new Insets(5));

                detailBtn.setOnAction(evt -> {
                    Book book = getTableView().getItems().get(getIndex());
                    // Misal panggil method detail buku di controller lain
                    // UserDashboardController userDash = new UserDashboardController();
                    // userDash.showBookDetail(book);
                    showAlert(Alert.AlertType.INFORMATION, "Detail Buku",
                        "Judul: " + book.getJudul() + "\nPemilik: " + book.getPemilikNama());
                });

                barterBtn.setOnAction(evt -> {
                    Book targetBook = getTableView().getItems().get(getIndex());
                    handleBarterRequest(targetBook);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void initializeFilters() {
        kondisiFilter.setItems(FXCollections.observableArrayList(
                "", "Baru", "Bagus", "Cukup", "Rusak Ringan", "Rusak Sedang"
        ));
        sortByCombo.setItems(FXCollections.observableArrayList(
                "Newest", "Oldest", "PriceAsc", "PriceDesc", "Condition"
        ));
        kondisiFilter.setValue("");
        sortByCombo.setValue("Newest");
    }

    private void loadAllBooks() {
        try {
            List<Book> list = bookDAO.searchBooks(
                    currentUser.getIdUser(),
                    "", "", "", "Newest"
            );
            bookList.setAll(list);
            bookTable.setItems(bookList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat daftar buku.");
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        String mataKuliah = matkulField.getText().trim();
        String kondisi = kondisiFilter.getValue();
        String sortBy = sortByCombo.getValue();

        try {
            List<Book> list = bookDAO.searchBooks(
                    currentUser.getIdUser(),
                    keyword, mataKuliah, kondisi, sortBy
            );
            bookList.setAll(list);
            bookTable.setItems(bookList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menjalankan pencarian.");
        }
    }

    @FXML
    private void handleResetFilters() {
        searchField.clear();
        matkulField.clear();
        kondisiFilter.setValue("");
        sortByCombo.setValue("Newest");
        loadAllBooks();
    }

    @FXML
    private void handleBackAction() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    private void handleBarterRequest(Book targetBook) {
        try {
            List<Book> myBooks = bookDAO.findByPemilik(currentUser.getIdUser());
            List<Book> availableMine = new ArrayList<>();
            for (Book b : myBooks) {
                if ("Tersedia".equalsIgnoreCase(b.getStatusKetersediaan())) {
                    availableMine.add(b);
                }
            }
            if (availableMine.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Tidak Ada Buku",
                        "Anda tidak memiliki buku tersedia untuk barter.");
                return;
            }

            ChoiceDialog<Book> dialog = new ChoiceDialog<>(availableMine.get(0), availableMine);
            dialog.setTitle("Pilih Buku untuk Ditawarkan");
            dialog.setHeaderText("Anda akan men-barter buku:\n" + targetBook.getJudul());
            dialog.setContentText("Pilih buku milik Anda:");

            Optional<Book> chosen = dialog.showAndWait();
            if (chosen.isEmpty()) return;
            Book offeredBook = chosen.get();

            TextInputDialog msgDialog = new TextInputDialog();
            msgDialog.setTitle("Pesan untuk Pemilik Buku");
            msgDialog.setHeaderText("Opsional: tambahkan pesan kepada pemilik buku target");
            msgDialog.setContentText("Pesan:");

            Optional<String> pesanOpt = msgDialog.showAndWait();
            String pesanPengaju = pesanOpt.orElse("");

            // Hitung match score dulu
            User pengaju = currentUser;
            User pemberi = userDAO.findById(targetBook.getIdPemilik());

            double matchScore = MatchingService.hitungMatchScore(targetBook, offeredBook, pengaju, pemberi);

            // Simpan transaksi dengan skor matching
            boolean sukses = transactionDAO.createTransaction(
                    targetBook.getIdBuku(),
                    offeredBook.getIdBuku(),
                    pengaju.getIdUser(),
                    pemberi.getIdUser(),
                    pesanPengaju,
                    matchScore
            );

            if (sukses) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses",
                        "Permintaan barter berhasil dikirim ke pemilik buku.\n" +
                                String.format("Skor kecocokan: %.2f%%", matchScore));
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal mengajukan barter.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat mengajukan barter.");
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
