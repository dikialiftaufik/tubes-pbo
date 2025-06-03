// dikialiftaufik/tubes-pbo/tubes-pbo-114b958adca147bc9ecad8a39ec42eb28db2797e/src/main/java/module-info.java
module barterbukukuliah {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base; // Ditambahkan untuk Callback dan PropertyValueFactory

    requires java.sql;
    requires spring.security.crypto;
    requires commons.logging;
    requires org.slf4j;
    requires ch.qos.logback.classic;

    opens com.barterbukukuliah.controller to javafx.fxml;
    // Modifikasi baris berikut:
    opens com.barterbukukuliah.model to javafx.fxml, javafx.base;
    opens com.barterbukukuliah.dao to javafx.fxml; // Mungkin tidak perlu dibuka ke fxml, tapi tidak masalah
    opens com.barterbukukuliah.util to javafx.fxml; // Mungkin tidak perlu dibuka ke fxml

    exports com.barterbukukuliah;
    exports com.barterbukukuliah.controller;
    exports com.barterbukukuliah.service;
    exports com.barterbukukuliah.dao;
    exports com.barterbukukuliah.util;
    exports com.barterbukukuliah.model;
}