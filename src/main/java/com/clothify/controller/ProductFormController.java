package com.clothify.controller;

import com.clothify.model.Product;
import com.clothify.service.custom.ProductService;
import com.clothify.service.custom.impl.ProductServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Product> tblProducts;
    @FXML
    private TableColumn<Product, Integer> colId;
    @FXML
    private TableColumn<Product, String> colCode;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colQty;

    private final ProductService productService = new ProductServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadTable();
        setNextCode();

        tblProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, product) -> {
            if (product != null) {
                txtCode.setText(product.getProductCode());
                txtName.setText(product.getName());
                txtPrice.setText(String.valueOf(product.getUnitPrice()));
                txtQty.setText(String.valueOf(product.getQuantity()));
            }
        });
    }

    @FXML
    void btnAddOnAction() {
        try {
            Product product = new Product();
            product.setProductCode(txtCode.getText().trim());
            product.setName(txtName.getText().trim());
            product.setUnitPrice(Double.parseDouble(txtPrice.getText().trim()));
            product.setQuantity(Integer.parseInt(txtQty.getText().trim()));
            product.setActive(true);

            if (productService.addProduct(product)) {
                new Alert(Alert.AlertType.INFORMATION, "Product added").show();
                clearFields();
                loadTable();
                setNextCode();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add product").show();
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            Product selected = tblProducts.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a product first").show();
                return;
            }

            Product product = new Product(
                    selected.getProductId(),
                    txtCode.getText().trim(),
                    txtName.getText().trim(),
                    Double.parseDouble(txtPrice.getText().trim()),
                    Integer.parseInt(txtQty.getText().trim()),
                    true
            );

            if (productService.updateProduct(product)) {
                new Alert(Alert.AlertType.INFORMATION, "Product updated").show();
                clearFields();
                loadTable();
                setNextCode();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update product").show();
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            Product selected = tblProducts.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a product first").show();
                return;
            }

            if (productService.deleteProduct(selected.getProductId())) {
                new Alert(Alert.AlertType.INFORMATION, "Product deleted").show();
                clearFields();
                loadTable();
                setNextCode();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete product").show();
        }
    }

    @FXML
    void btnSearchOnAction() {
        try {
            tblProducts.setItems(FXCollections.observableArrayList(
                    productService.searchProducts(txtSearch.getText())
            ));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Search failed").show();
        }
    }

    @FXML
    void btnReloadOnAction() {
        loadTable();
    }

    @FXML
    void btnStockInOnAction() {
        try {
            Product selected = tblProducts.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a product first").show();
                return;
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Stock In");
            dialog.setHeaderText("Enter quantity to add");
            dialog.setContentText("Quantity:");
            String input = dialog.showAndWait().orElse("").trim();
            if (input.isEmpty()) return;

            int qty = Integer.parseInt(input);
            if (productService.addStock(selected.getProductId(), qty)) {
                new Alert(Alert.AlertType.INFORMATION, "Stock updated").show();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Stock update failed").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Stock update failed").show();
        }
    }

    @FXML
    void btnStockOutOnAction() {
        try {
            Product selected = tblProducts.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a product first").show();
                return;
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Stock Out");
            dialog.setHeaderText("Enter quantity to reduce");
            dialog.setContentText("Quantity:");
            String input = dialog.showAndWait().orElse("").trim();
            if (input.isEmpty()) return;

            int qty = Integer.parseInt(input);
            if (productService.reduceStock(selected.getProductId(), qty)) {
                new Alert(Alert.AlertType.INFORMATION, "Stock updated").show();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid quantity or insufficient stock").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Stock update failed").show();
        }
    }

    private void loadTable() {
        try {
            tblProducts.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load products").show();
        }
    }

    private void setNextCode() {
        try {
            txtCode.setText(productService.generateNextCode());
        } catch (Exception e) {
            txtCode.setText("P001");
        }
    }

    private void clearFields() {
        txtName.clear();
        txtPrice.clear();
        txtQty.clear();
        txtSearch.clear();
        tblProducts.getSelectionModel().clearSelection();
    }
}
