package com.clothify.controller;

import com.clothify.service.OrderService;
import com.clothify.service.ProductService;
import com.clothify.service.impl.OrderServiceImpl;
import com.clothify.service.impl.ProductServiceImpl;
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

    private final ProductService productService = new ProductServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Welcome
        if (SessionManager.getCurrentUser() != null) {
            lblWelcome.setText("Welcome back, " + SessionManager.getCurrentUser().getFullName());
        }
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        // Load KPIs
        try {
            lblTotalProducts.setText(String.valueOf(productService.getProductCount()));
            lblLowStock.setText(String.valueOf(productService.getLowStockCount()));
        } catch (Exception e) {
            lblTotalProducts.setText("--");
            lblLowStock.setText("--");
        }

        try {
            lblTodaySales.setText("Rs. " + String.format("%,.0f", orderService.getTodaySalesTotal()));
            lblTodayOrders.setText(String.valueOf(orderService.getTodayOrderCount()));

            // Charts
            loadSalesChart();
            loadTopProductsChart();
        } catch (Exception e) {
            lblTodaySales.setText("Rs. 0");
            lblTodayOrders.setText("0");
        }
    }

    private void loadSalesChart() {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Sales (Rs.)");

            Map<String, Double> last7Days = orderService.getSalesLast7Days();
            for (Map.Entry<String, Double> entry : last7Days.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            salesChart.getData().clear();
            salesChart.getData().add(series);
        } catch (Exception e) {
            System.err.println("Failed to load sales chart " + e.getMessage());
        }
    }

    private void loadTopProductsChart() {
        try {
            Map<String, Integer> topProducts = orderService.getTopSellingProducts(5);
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
