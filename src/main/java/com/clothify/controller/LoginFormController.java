package com.clothify.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import com.clothify.model.User;
import com.clothify.service.custom.UserService;
import com.clothify.service.custom.impl.UserServiceImpl;
import com.clothify.util.SessionManager;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginFormController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox chkRememberMe;

    @FXML
    private Label lblMessage;

    private final UserService userService = new UserServiceImpl();
    private Preferences prefs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prefs = Preferences.userNodeForPackage(LoginFormController.class);
        String savedUsername = prefs.get("username", "");
        String savedPassword = prefs.get("password", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            txtUsername.setText(savedUsername);
            txtPassword.setText(savedPassword);
            chkRememberMe.setSelected(true);
        }
    }

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

            lblMessage.setText("");
            SessionManager.setCurrentUser(user);

            if (chkRememberMe.isSelected()) {
                prefs.put("username", username);
                prefs.put("password", password);
            } else {
                prefs.remove("username");
                prefs.remove("password");
            }

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/main_layout.fxml"))));
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Login failed: " + e.getMessage());
        }
    }
}
