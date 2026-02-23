module com.clothify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.clothify.controller to javafx.fxml;

    exports com.clothify;
    exports com.clothify.controller;
    exports com.clothify.service;
    exports com.clothify.service.impl;
    exports com.clothify.repository;
    exports com.clothify.repository.impl;
    exports com.clothify.model;
    exports com.clothify.util;
}
