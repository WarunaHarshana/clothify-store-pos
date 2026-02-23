package com.clothify.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML
    private StackPane contentArea;

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
    private Button navReports;

    private Button active;

    @FXML
    public void initialize() {
        loadPage("/view/dashboard_content.fxml", navDashboard);
    }

    @FXML
    void handleNavDashboard() { loadPage("/view/dashboard_content.fxml", navDashboard); }
    @FXML
    void handleNavProducts() { loadPage("/view/product_form.fxml", navProducts); }
    @FXML
    void handleNavPOS() { loadPage("/view/module_placeholder.fxml", navPOS); }
    @FXML
    void handleNavOrders() { loadPage("/view/module_placeholder.fxml", navOrders); }
    @FXML
    void handleNavInventory() { loadPage("/view/module_placeholder.fxml", navInventory); }
    @FXML
    void handleNavSuppliers() { loadPage("/view/module_placeholder.fxml", navSuppliers); }
    @FXML
    void handleNavEmployees() { loadPage("/view/module_placeholder.fxml", navEmployees); }
    @FXML
    void handleNavReports() { loadPage("/view/module_placeholder.fxml", navReports); }

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
        if (active != null) active.getStyleClass().remove("side-btn-active");
        if (!btn.getStyleClass().contains("side-btn-active")) btn.getStyleClass().add("side-btn-active");
        active = btn;
    }
}
