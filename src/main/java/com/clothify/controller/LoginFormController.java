package com.clothify.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.clothify.model.User;
import com.clothify.repository.custom.impl.UserRepositoryImpl;
import com.clothify.service.custom.UserService;
import com.clothify.service.custom.impl.UserServiceImpl;

public class LoginFormController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        try {
            String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
            String password = txtPassword.getText() == null ? "" : txtPassword.getText();

            if (username.isEmpty() || password.isEmpty()) {
                lblMessage.setText("Please enter username and password");
                return;
            }

            User user = userService.authenticate(username, password);
            if (user == null) {
                lblMessage.setText("Invalid username or password");
                return;
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Login successful. Welcome " + user.getFullName() + " (" + user.getRole() + ")").show();
            lblMessage.setText("");
        } catch (Exception e) {
            lblMessage.setText("Login failed");
        }
    }
}

