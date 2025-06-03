// dikialiftaufik/tubes-pbo/tubes-pbo-114b958adca147bc9ecad8a39ec42eb28db2797e/src/main/java/com/barterbukukuliah/controller/RatingViewController.java
package com.barterbukukuliah.controller;

import com.barterbukukuliah.dao.TrustScoreHistoryDAO;
import com.barterbukukuliah.dao.UserDAO;
import com.barterbukukuliah.model.User;
import com.barterbukukuliah.model.TrustScoreHistory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell; // Pastikan import ini benar
import javafx.util.Callback; // Import untuk Callback

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RatingViewController {

    @FXML private Label trustScoreLabel;
    @FXML private Separator historySeparator;
    @FXML private Label historyTitleLabel;
    @FXML private TableView<TrustScoreHistory> historyTable;
    @FXML private TableColumn<TrustScoreHistory, LocalDateTime> colTimestamp;
    @FXML private TableColumn<TrustScoreHistory, String> colActionType;
    @FXML private TableColumn<TrustScoreHistory, Double> colScoreChange;
    @FXML private TableColumn<TrustScoreHistory, Double> colNewScore;
    @FXML private TableColumn<TrustScoreHistory, String> colKeterangan;

    private User currentUser;
    private UserDAO userDAO = new UserDAO();
    private TrustScoreHistoryDAO trustScoreHistoryDAO = new TrustScoreHistoryDAO();
    private ObservableList<TrustScoreHistory> historyList = FXCollections.observableArrayList();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public void setUser(User user) {
        this.currentUser = user;
        refreshUserDataAndHistory();
    }

    @FXML
    private void initialize() {
        // Konfigurasi untuk colTimestamp dengan implementasi Callback eksplisit
        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colTimestamp.setCellFactory(new Callback<TableColumn<TrustScoreHistory, LocalDateTime>, TableCell<TrustScoreHistory, LocalDateTime>>() {
            @Override
            public TableCell<TrustScoreHistory, LocalDateTime> call(TableColumn<TrustScoreHistory, LocalDateTime> param) {
                return new TableCell<TrustScoreHistory, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(dateTimeFormatter.format(item));
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        colActionType.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        colScoreChange.setCellValueFactory(new PropertyValueFactory<>("scoreChange"));
        colNewScore.setCellValueFactory(new PropertyValueFactory<>("newScore"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        historyTable.setItems(historyList);
    }

    private void refreshUserDataAndHistory() {
        if (currentUser == null) return;

        try {
            User updatedUser = userDAO.findById(currentUser.getIdUser());
            if (updatedUser != null) {
                this.currentUser = updatedUser;
                trustScoreLabel.setText(String.format("%.2f", currentUser.getTrustScore()));
            } else {
                trustScoreLabel.setText("N/A");
                showAlert(Alert.AlertType.ERROR, "Error Pengguna", "Tidak dapat memuat data pengguna terkini.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            trustScoreLabel.setText("Error");
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat skor kepercayaan: " + e.getMessage());
        }

        try {
            List<TrustScoreHistory> detailedHistory = trustScoreHistoryDAO.getHistoryForUser(currentUser.getIdUser());
            if (detailedHistory != null && !detailedHistory.isEmpty()) {
                historyList.setAll(detailedHistory);
                setHistoryViewVisibility(true);
            } else {
                historyList.clear();
                setHistoryViewVisibility(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setHistoryViewVisibility(false);
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat riwayat skor: " + e.getMessage());
        }
    }

    private void setHistoryViewVisibility(boolean visible) {
        historyTable.setVisible(visible);
        historySeparator.setVisible(visible);
        historyTitleLabel.setVisible(visible);
        if (!visible) {
            historyTable.setPlaceholder(new Label("Anda belum memiliki riwayat perubahan skor kepercayaan."));
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