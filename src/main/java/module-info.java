module barterbukukuliah {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires spring.security.crypto;
    requires commons.logging;
    requires org.slf4j;
    requires ch.qos.logback.classic;

    opens com.barterbukukuliah.controller to javafx.fxml;
    opens com.barterbukukuliah.model to javafx.fxml;
    opens com.barterbukukuliah.dao to javafx.fxml;
    opens com.barterbukukuliah.util to javafx.fxml;

    exports com.barterbukukuliah;
    exports com.barterbukukuliah.controller;
    exports com.barterbukukuliah.service;
    exports com.barterbukukuliah.dao;
    exports com.barterbukukuliah.util;
    exports com.barterbukukuliah.model;
}
