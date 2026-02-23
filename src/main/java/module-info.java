module com.clothify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens controller to javafx.fxml;

    exports application;
    exports controller;
    exports db;
    exports model;
    exports repository.custom;
    exports repository.custom.impl;
    exports service.custom;
    exports service.custom.impl;
    exports util;
}
