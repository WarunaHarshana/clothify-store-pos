package com.clothify.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.clothify.util.SessionManager;

public class MainLayoutController {

    @FXML
    private StackPane contentArea;
    @FXML
    private Label lblUserName;

    @FXML
    private Button navDashboard;
    @FXML
    private Button navProducts;
    @FXML
    private Button navPOS;
    @FXML
    private Button navOrders;
    @FXML
    private Button navInventory;
    @FXML
    private Button navSuppliers;
    @FXML
    private Button navEmployees;
    @FXML
    private Button navUsers;
    @FXML
    private Button navReports;

    private Button active;

    @FXML
    public void initialize() {
        if (SessionManager.getCurrentUser() != null) {
            lblUserName.setText(SessionManager.getCurrentUser().getFullName());

            if (!SessionManager.isAdmin()) {
                navProducts.setVisible(false);
                navProducts.setManaged(false);
                navInventory.setVisible(false);
                navInventory.setManaged(false);
                navSuppliers.setVisible(false);
                navSuppliers.setManaged(false);
                navEmployees.setVisible(false);
                navEmployees.setManaged(false);
                navUsers.setVisible(false);
                navUsers.setManaged(false);
                navReports.setVisible(false);
                navReports.setManaged(false);
            }
        }
        loadPage("/view/dashboard_content.fxml", navDashboard);
    }

    @FXML
    void handleNavDashboard() {
        loadPage("/view/dashboard_content.fxml", navDashboard);
    }

    @FXML
    void handleNavProducts() {
        loadPage("/view/product_form.fxml", navProducts);
    }

    @FXML
    void handleNavPOS() {
        loadPage("/view/pos_form.fxml", navPOS);
    }

    @FXML
    void handleNavOrders() {
        loadPage("/view/order_history.fxml", navOrders);
    }

    @FXML
    void handleNavInventory() {
        loadPage("/view/product_form.fxml", navInventory);
    }

    @FXML
    void handleNavSuppliers() {
        loadPage("/view/supplier_form.fxml", navSuppliers);
    }

    @FXML
    void handleNavEmployees() {
        loadPage("/view/employee_form.fxml", navEmployees);
    }

    @FXML
    void handleNavUsers() {
        loadPage("/view/user_form.fxml", navUsers);
    }

    @FXML
    void handleNavReports() {
        loadPage("/view/report_form.fxml", navReports);
    }

    @FXML
    void handleLogout() {
        try {
            SessionManager.clear();
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxml, Button navBtn) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(root);
            setActive(navBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActive(Button btn) {
        if (active != null)
            active.getStyleClass().remove("side-btn-active");
        if (!btn.getStyleClass().contains("side-btn-active"))
            btn.getStyleClass().add("side-btn-active");
        active = btn;
    }
}
