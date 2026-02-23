package com.clothify.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardFormController {

    @FXML
    void btnProductsOnAction(ActionEvent event) {
        switchScene(event, "/view/product_form.fxml");
    }

    @FXML
    void btnPosOnAction(ActionEvent event) {
        openPlaceholder(event, "Point of Sale");
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        openPlaceholder(event, "Orders");
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        openPlaceholder(event, "Inventory");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        openPlaceholder(event, "Suppliers");
    }

    @FXML
    void btnEmployeesOnAction(ActionEvent event) {
        openPlaceholder(event, "Employees");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        openPlaceholder(event, "Reports");
    }

    @FXML
    void btnSettingsOnAction(ActionEvent event) {
        openPlaceholder(event, "Settings");
    }

    private void openPlaceholder(ActionEvent event, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/module_placeholder.fxml"));
            Parent root = loader.load();
            ModulePlaceholderController controller = loader.getController();
            controller.setTitle(title);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String fxml) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
