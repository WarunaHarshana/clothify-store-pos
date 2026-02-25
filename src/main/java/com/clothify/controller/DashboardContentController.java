package com.clothify.controller;

import com.clothify.repository.custom.impl.OrderRepositoryImpl;
import com.clothify.repository.custom.impl.ProductRepositoryImpl;
import com.clothify.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardContentController implements Initializable {

    @FXML private Label lblWelcome;
    @FXML private Label lblDate;
    @FXML private Label lblTodaySales;
    @FXML private Label lblTotalProducts;
    @FXML private Label lblLowStock;
    @FXML private Label lblTodayOrders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Welcome
        if (SessionManager.getCurrentUser() != null) {
            lblWelcome.setText("Welcome back, " + SessionManager.getCurrentUser().getFullName());
        }
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        // Load KPIs
        try {
            ProductRepositoryImpl productRepo = new ProductRepositoryImpl();
            lblTotalProducts.setText(String.valueOf(productRepo.getProductCount()));
            lblLowStock.setText(String.valueOf(productRepo.getLowStockCount()));
        } catch (Exception e) {
            lblTotalProducts.setText("--");
            lblLowStock.setText("--");
        }

        try {
            OrderRepositoryImpl orderRepo = new OrderRepositoryImpl();
            lblTodaySales.setText("Rs. " + String.format("%,.0f", orderRepo.getTodaySalesTotal()));
            lblTodayOrders.setText(String.valueOf(orderRepo.getTodayOrderCount()));
        } catch (Exception e) {
            lblTodaySales.setText("Rs. 0");
            lblTodayOrders.setText("0");
        }
    }
}

