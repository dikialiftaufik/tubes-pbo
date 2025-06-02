package com.barterbukukuliah.controller;

import com.barterbukukuliah.model.TransaksiBarter;
import com.barterbukukuliah.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.List;

public class TransaksiMasukController {

    @FXML private TableView<TransaksiBarter> tableTransaksi;
    @FXML private TableColumn<TransaksiBarter, Integer> colId;
    @FXML private TableColumn<TransaksiBarter, String> colBukuDiminta;
    @FXML private TableColumn<TransaksiBarter, String> colBukuDitawarkan;
    @FXML private TableColumn<TransaksiBarter, String> colPengaju;
    @FXML private TableColumn<TransaksiBarter, String> colPesan;
    @FXML private TableColumn<TransaksiBarter, Double> colMatchScore;
    @FXML private TableColumn<TransaksiBarter, Void> colAksi;

    private TransactionService transactionService = new TransactionService();
    private ObservableList<TransaksiBarter> transaksiList = FXCollections.observableArrayList();

    private int currentUserId;
    
    public void setCurrentUser(com.barterbukukuliah.model.User user) {
        this.currentUserId = user.getIdUser();
        loadPendingTransactions();
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colBukuDiminta.setCellValueFactory(new PropertyValueFactory<>("judulBukuDiminta")); // sesuaikan getter di model
        colBukuDitawarkan.setCellValueFactory(new PropertyValueFactory<>("judulBukuDitawarkan"));
        colPengaju.setCellValueFactory(new PropertyValueFactory<>("namaPengaju"));
        colPesan.setCellValueFactory(new PropertyValueFactory<>("pesanPengaju"));
        colMatchScore.setCellValueFactory(new PropertyValueFactory<>("matchScore"));

        // Setup kolom aksi tombol terima/tolak
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnTerima = new Button("Terima");
            private final Button btnTolak = new Button("Tolak");
            private final HBox box = new HBox(5, btnTerima, btnTolak);

            {
                btnTerima.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnTolak.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                btnTerima.setOnAction(event -> {
                    TransaksiBarter trx = getTableView().getItems().get(getIndex());
                    prosesRespon(trx, "Diterima");
                });

                btnTolak.setOnAction(event -> {
                    TransaksiBarter trx = getTableView().getItems().get(getIndex());
                    prosesRespon(trx, "Ditolak");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void loadPendingTransactions() {
        try {
            List<TransaksiBarter> list = transactionService.getPendingRequests(currentUserId);
            transaksiList.setAll(list);
            tableTransaksi.setItems(transaksiList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal memuat data transaksi.");
        }
    }

    private void prosesRespon(TransaksiBarter trx, String status) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(status + " Permintaan Barter");
        dialog.setHeaderText("Masukkan pesan balasan (opsional):");
        dialog.setContentText("Pesan:");

        dialog.showAndWait().ifPresent(pesanBalasan -> {
            try {
                boolean berhasil = transactionService.respondToBarter(trx.getIdTransaksi(), status, pesanBalasan, trx.getIdUserPengaju());
                if (berhasil) {
                    showAlert(Alert.AlertType.INFORMATION, "Status transaksi berhasil diubah.");
                    loadPendingTransactions();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal mengubah status transaksi.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Terjadi kesalahan saat memproses transaksi.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
