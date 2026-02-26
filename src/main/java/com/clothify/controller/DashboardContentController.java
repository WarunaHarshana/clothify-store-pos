package com.clothify.controller;

import com.clothify.repository.impl.OrderRepositoryImpl;
import com.clothify.repository.impl.ProductRepositoryImpl;
import com.clothify.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Map;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class DashboardContentController implements Initializable {

    @FXML
    private Label lblWelcome;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTodaySales;
    @FXML
    private Label lblTotalProducts;
    @FXML
    private Label lblLowStock;
    @FXML
    private Label lblTodayOrders;

    @FXML
    private LineChart<String, Number> salesChart;

    @FXML
    private PieChart topProductsChart;

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

            // Charts
            loadSalesChart(orderRepo);
            loadTopProductsChart(orderRepo);
        } catch (Exception e) {
            lblTodaySales.setText("Rs. 0");
            lblTodayOrders.setText("0");
        }
    }

    private void loadSalesChart(OrderRepositoryImpl orderRepo) {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Sales (Rs.)");

            Map<String, Double> last7Days = orderRepo.getSalesLast7Days();
            for (Map.Entry<String, Double> entry : last7Days.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            salesChart.getData().clear();
            salesChart.getData().add(series);
        } catch (Exception e) {
            System.err.println("Failed to load sales chart " + e.getMessage());
        }
    }

    private void loadTopProductsChart(OrderRepositoryImpl orderRepo) {
        try {
            Map<String, Integer> topProducts = orderRepo.getTopSellingProducts(5);
            topProductsChart.getData().clear();

            for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")",
                        entry.getValue());
                topProductsChart.getData().add(slice);
            }
        } catch (Exception e) {
            System.err.println("Failed to load top products chart " + e.getMessage());
        }
    }
}
