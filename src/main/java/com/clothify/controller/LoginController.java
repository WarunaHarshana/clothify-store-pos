package com.clothify.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter username and password");
            return;
        }

        lblMessage.setText("Login module setup completed");
    }
}
