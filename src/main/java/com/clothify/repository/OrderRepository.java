package com.clothify.repository;

import com.clothify.model.Order;
import com.clothify.model.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderRepository {
    int saveOrder(Connection conn, Order order) throws SQLException;

    void saveOrderDetails(Connection conn, List<OrderDetail> details) throws SQLException;

    List<Order> getAll() throws SQLException;

    List<Order> search(String keyword) throws SQLException;

    List<OrderDetail> getOrderDetails(int orderId) throws SQLException;

    int getTodayOrderCount() throws SQLException;

    double getTodaySalesTotal() throws SQLException;

    java.util.Map<String, Double> getSalesLast7Days() throws SQLException;

    java.util.Map<String, Integer> getTopSellingProducts(int limit) throws SQLException;
}
