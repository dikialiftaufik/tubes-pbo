package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.BookDAO;
import com.barterbukukuliah.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class ManageBooksController {

    @FXML
    private TableView<Book> tableBooks;

    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colJudul;
    @FXML private TableColumn<Book, String> colPemilik;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colAksi;

    private final BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {
        // Setup columns
        colId.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPemilik.setCellValueFactory(new PropertyValueFactory<>("pemilikNama"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKetersediaan"));

        // Setup aksi kolom dengan tombol Hapus & Ubah Status
        addActionButtonsToTable();

        // Load data awal
        refreshData();
    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                return new TableCell<>() {
                    private final Button btnDelete = new Button("Hapus");
                    private final Button btnToggleStatus = new Button("Toggle Status");
                    private final HBox pane = new HBox(10, btnDelete, btnToggleStatus);

                    {
                        btnDelete.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
                        btnToggleStatus.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white;");

                        btnDelete.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleDeleteBook(book);
                        });

                        btnToggleStatus.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleToggleStatus(book);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        };

        colAksi.setCellFactory(cellFactory);
    }

    @FXML
    private void refreshData() {
        try {
            List<Book> books = bookDAO.getAllBooksForAdmin();
            ObservableList<Book> bookList = FXCollections.observableArrayList(books);
            tableBooks.setItems(bookList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data buku: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteBook(Book book) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Apakah Anda yakin ingin menghapus buku: " + book.getJudul() + "?");
        if (confirm.showAndWait().filter(response -> response == ButtonType.OK).isPresent()) {
            try {
                bookDAO.forceDeleteBook(book.getIdBuku());
                refreshData();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dihapus.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus buku: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleToggleStatus(Book book) {
        String currentStatus = book.getStatusKetersediaan();
        String newStatus;
switch (currentStatus) {
    case "Tersedia":
        newStatus = "Tidak Tersedia";
        break;
    case "Tidak Tersedia":
        newStatus = "Tersedia";
        break;
    default:
        newStatus = "Tersedia";
}


        try {
            bookDAO.updateBookStatus(book.getIdBuku(), newStatus);
            refreshData();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Status buku diubah menjadi: " + newStatus);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengubah status: " + e.getMessage());
            e.printStackTrace();
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
