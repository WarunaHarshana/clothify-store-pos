package com.clothify.controller;

import com.clothify.model.User;
import com.clothify.service.custom.UserService;
import com.clothify.service.custom.impl.UserServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtFullName;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableView<User> tblUsers;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colFullName;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, Boolean> colActive;

    private final UserService userService = new UserServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "STAFF"));
        cmbRole.setValue("STAFF");

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("active"));

        loadTable();

        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, user) -> {
            if (user != null) {
                txtUsername.setText(user.getUsername());
                txtFullName.setText(user.getFullName());
                txtPassword.clear(); // do not show password hash
                cmbRole.setValue(user.getRole());
            }
        });
    }

    @FXML
    void btnAddOnAction() {
        try {
            if (txtUsername.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Username and Password are required").show();
                return;
            }

            User user = new User();
            user.setUsername(txtUsername.getText().trim());
            user.setPasswordHash(txtPassword.getText().trim());
            user.setFullName(txtFullName.getText().trim());
            user.setRole(cmbRole.getValue());
            user.setActive(true);

            if (userService.addUser(user)) {
                new Alert(Alert.AlertType.INFORMATION, "User added").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add user").show();
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            User selected = tblUsers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a user first").show();
                return;
            }

            User user = new User(
                    selected.getUserId(),
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(), // Service will hash this, or retain old if empty
                    txtFullName.getText().trim(),
                    cmbRole.getValue(),
                    true);

            if (userService.updateUser(user)) {
                new Alert(Alert.AlertType.INFORMATION, "User updated").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update user").show();
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            User selected = tblUsers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a user first").show();
                return;
            }

            if (userService.deleteUser(selected.getUserId())) {
                new Alert(Alert.AlertType.INFORMATION, "User deleted").show();
                clearFields();
                loadTable();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete user").show();
        }
    }

    @FXML
    void btnReloadOnAction() {
        loadTable();
        clearFields();
    }

    private void loadTable() {
        try {
            tblUsers.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load users").show();
        }
    }

    private void clearFields() {
        txtUsername.clear();
        txtFullName.clear();
        txtPassword.clear();
        cmbRole.setValue("STAFF");
        tblUsers.getSelectionModel().clearSelection();
    }
}
