package com.clothify.service;

import com.clothify.model.CartItem;
import com.clothify.model.Order;
import com.clothify.model.OrderDetail;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    boolean placeOrder(List<CartItem> cartItems, int cashierUserId) throws SQLException;

    int placeOrderAndGetId(List<CartItem> cartItems, int cashierUserId) throws SQLException;

    List<Order> getAllOrders() throws SQLException;

    List<Order> searchOrders(String keyword) throws SQLException;

    List<OrderDetail> getOrderDetails(int orderId) throws SQLException;

    int getTodayOrderCount() throws SQLException;

    double getTodaySalesTotal() throws SQLException;

    Map<String, Double> getSalesLast7Days() throws SQLException;

    Map<String, Integer> getTopSellingProducts(int limit) throws SQLException;
}
