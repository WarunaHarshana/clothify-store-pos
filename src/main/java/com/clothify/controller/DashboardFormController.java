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
        openModuleWindow(event, "/view/product_form.fxml", "Products");
    }

    @FXML
    void btnPosOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Point of Sale");
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Orders");
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Inventory");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Suppliers");
    }

    @FXML
    void btnEmployeesOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Employees");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Reports");
    }

    @FXML
    void btnSettingsOnAction(ActionEvent event) {
        openPlaceholderWindow(event, "Settings");
    }

    private void openPlaceholderWindow(ActionEvent event, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/module_placeholder.fxml"));
            Parent root = loader.load();
            ModulePlaceholderController controller = loader.getController();
            controller.setTitle(title);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage moduleStage = new Stage();
            moduleStage.setTitle(title + " - Clothify Store POS");
            moduleStage.setScene(new Scene(root));
            moduleStage.setMaximized(true);
            moduleStage.show();

            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openModuleWindow(ActionEvent event, String fxml, String title) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));

            Stage moduleStage = new Stage();
            moduleStage.setTitle(title + " - Clothify Store POS");
            moduleStage.setScene(new Scene(root));
            moduleStage.setMaximized(true);
            moduleStage.show();

            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
