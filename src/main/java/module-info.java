module com.clothify {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires jasperreports;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires static lombok;
    requires java.prefs;

    opens com.clothify to javafx.fxml;
    opens com.clothify.controller to javafx.fxml;
    opens com.clothify.model to javafx.base;

    exports com.clothify;
    exports com.clothify.controller;
    exports com.clothify.db;
    exports com.clothify.model;
    exports com.clothify.repository.custom;
    exports com.clothify.repository.custom.impl;
    exports com.clothify.service.custom;
    exports com.clothify.service.custom.impl;
    exports com.clothify.util;
}
