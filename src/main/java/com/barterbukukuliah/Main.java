package com.barterbukukuliah;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point untuk aplikasi Platform Barter Buku Kuliah.
 * Kali ini akan kita set agar window langsung fullscreen (maximized).
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barterbukukuliah/fxml/Login.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Login - Barter Buku Kuliah");

            // Atur Scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // Maksimalkan jendela agar fullscreen
            primaryStage.setMaximized(true);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}