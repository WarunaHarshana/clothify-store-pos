package com.clothify.controller;

import com.clothify.model.CartItem;
import com.clothify.model.Product;
import com.clothify.service.custom.OrderService;
import com.clothify.service.custom.ProductService;
import com.clothify.service.custom.impl.OrderServiceImpl;
import com.clothify.service.custom.impl.ProductServiceImpl;
import com.clothify.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PosFormController implements Initializable {

    @FXML private TextField txtSearch;
    @FXML private FlowPane productFlow;
    @FXML private TableView<CartItem> tblCart;
    @FXML private TableColumn<CartItem, String> colCartName;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, Double> colCartPrice;
    @FXML private TableColumn<CartItem, Double> colCartTotal;
    @FXML private Label lblItemCount;
    @FXML private Label lblTotal;

    private final ProductService productService = new ProductServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final ObservableList<CartItem> cart = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCartName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));
        tblCart.setItems(cart);

        loadProducts(null);
    }

    @FXML
    void btnSearchProducts() {
        String keyword = txtSearch.getText();
        loadProducts(keyword);
    }

    @FXML
    void btnClearSearch() {
        txtSearch.clear();
        loadProducts(null);
    }

    @FXML
    void btnRemoveItem() {
        CartItem selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cart.remove(selected);
            updateTotals();
        }
    }

    @FXML
    void btnClearCart() {
        cart.clear();
        updateTotals();
    }

    @FXML
    void btnPlaceOrder() {
        if (cart.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").show();
            return;
        }

        int cashierId = SessionManager.getCurrentUser() != null
                ? SessionManager.getCurrentUser().getUserId() : 1;

        try {
            if (orderService.placeOrder(cart, cashierId)) {
                new Alert(Alert.AlertType.INFORMATION,
                        "Order placed successfully!\nTotal: Rs. " + String.format("%.2f", getCartTotal())).show();
                cart.clear();
                updateTotals();
                loadProducts(null);
            } else {
                new Alert(Alert.AlertType.ERROR, "Order failed. Check stock availability.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Order failed: " + e.getMessage()).show();
        }
    }

    private void loadProducts(String keyword) {
        try {
            List<Product> products;
            if (keyword == null || keyword.trim().isEmpty()) {
                products = productService.getAllProducts();
            } else {
                products = productService.searchProducts(keyword);
            }
            productFlow.getChildren().clear();
            for (Product p : products) {
                productFlow.getChildren().add(createProductCard(p));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load products").show();
        }
    }

    private VBox createProductCard(Product product) {
        Label name = new Label(product.getName());
        name.getStyleClass().add("top-item-name");

        Label price = new Label("Rs. " + String.format("%.2f", product.getUnitPrice()));
        price.getStyleClass().add("kpi-value");
        price.setStyle("-fx-font-size: 16px;");

        Label stock = new Label("Stock: " + product.getQuantity());
        stock.getStyleClass().add("top-item-qty");

        Button addBtn = new Button("Add to Cart");
        addBtn.getStyleClass().add("pos-add-btn");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> addToCart(product));

        if (product.getQuantity() <= 0) {
            addBtn.setDisable(true);
            addBtn.setText("Out of Stock");
        }

        VBox card = new VBox(6, name, price, stock, addBtn);
        card.getStyleClass().add("pos-product-card");
        card.setPadding(new Insets(12));
        card.setPrefWidth(170);
        return card;
    }

    private void addToCart(Product product) {
        // Check if already in cart
        for (CartItem item : cart) {
            if (item.getProductId() == product.getProductId()) {
                if (item.getQuantity() + 1 > product.getQuantity()) {
                    new Alert(Alert.AlertType.WARNING, "Insufficient stock").show();
                    return;
                }
                item.addQuantity(1);
                tblCart.refresh();
                updateTotals();
                return;
            }
        }
        // New item
        if (product.getQuantity() < 1) {
            new Alert(Alert.AlertType.WARNING, "Out of stock").show();
            return;
        }
        cart.add(new CartItem(product, 1));
        updateTotals();
    }

    private void updateTotals() {
        lblItemCount.setText(String.valueOf(cart.size()));
        lblTotal.setText("Rs. " + String.format("%.2f", getCartTotal()));
    }

    private double getCartTotal() {
        return cart.stream().mapToDouble(CartItem::getLineTotal).sum();
    }
}

