package com.clothify.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ModulePlaceholderController {

    @FXML
    private Label lblTitle;

    public void setTitle(String title) {
        lblTitle.setText(title);
    }
}
