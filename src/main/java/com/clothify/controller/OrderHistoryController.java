package com.clothify.controller;

import com.clothify.model.Order;
import com.clothify.model.OrderDetail;
import com.clothify.service.OrderService;
import com.clothify.service.impl.OrderServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {

    @FXML private TextField txtSearch;

    @FXML private TableView<Order> tblOrders;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, String> colDate;
    @FXML private TableColumn<Order, Double> colTotal;
    @FXML private TableColumn<Order, String> colStatus;

    @FXML private TableView<OrderDetail> tblDetails;
    @FXML private TableColumn<OrderDetail, String> colProduct;
    @FXML private TableColumn<OrderDetail, Integer> colQty;
    @FXML private TableColumn<OrderDetail, Double> colPrice;
    @FXML private TableColumn<OrderDetail, Double> colLineTotal;

    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));

        loadOrders();

        tblOrders.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, order) -> {
            if (order != null) loadDetails(order.getOrderId());
        });
    }

    @FXML
    void btnSearchOnAction() {
        try {
            tblOrders.setItems(FXCollections.observableArrayList(
                    orderService.searchOrders(txtSearch.getText())
            ));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Search failed").show();
        }
    }

    @FXML
    void btnReloadOnAction() {
        loadOrders();
        tblDetails.getItems().clear();
    }

    private void loadOrders() {
        try {
            tblOrders.setItems(FXCollections.observableArrayList(orderService.getAllOrders()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load orders").show();
        }
    }

    private void loadDetails(int orderId) {
        try {
            tblDetails.setItems(FXCollections.observableArrayList(
                    orderService.getOrderDetails(orderId)
            ));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load order details").show();
        }
    }
}

