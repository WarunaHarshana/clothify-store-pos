package com.clothify.service.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.*;
import com.clothify.repository.OrderRepository;
import com.clothify.repository.ProductRepository;
import com.clothify.repository.impl.OrderRepositoryImpl;
import com.clothify.repository.impl.ProductRepositoryImpl;
import com.clothify.service.OrderService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository = new OrderRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    @Override
    public boolean placeOrder(List<CartItem> cartItems, int cashierUserId) throws SQLException {
        return placeOrderAndGetId(cartItems, cashierUserId) > 0;
    }

    @Override
    public int placeOrderAndGetId(List<CartItem> cartItems, int cashierUserId) throws SQLException {
        if (cartItems == null || cartItems.isEmpty())
            return -1;

        Connection conn;
        try {
            conn = DbConnection.getInstance().getConnection();
        } catch (Exception e) {
            throw new SQLException(e);
        }

        try {
            conn.setAutoCommit(false);

            for (CartItem item : cartItems) {
                Product product = productRepository.findById(item.getProductId());
                if (product == null || product.getQuantity() < item.getQuantity()) {
                    conn.rollback();
                    return -1;
                }
            }

            double total = cartItems.stream().mapToDouble(CartItem::getLineTotal).sum();

            Order order = new Order(0, null, cashierUserId, total, "COMPLETED");
            int orderId = orderRepository.saveOrder(conn, order);

            List<OrderDetail> details = new ArrayList<>();
            for (CartItem item : cartItems) {
                details.add(new OrderDetail(
                        0, orderId,
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()));
            }
            orderRepository.saveOrderDetails(conn, details);

            for (CartItem item : cartItems) {
                Product product = productRepository.findById(item.getProductId());
                int newQty = product.getQuantity() - item.getQuantity();
                productRepository.updateQuantity(item.getProductId(), newQty);
            }

            conn.commit();
            return orderId;
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException(e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        return orderRepository.getAll();
    }

    @Override
    public List<Order> searchOrders(String keyword) throws SQLException {
        return orderRepository.search(keyword);
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) throws SQLException {
        return orderRepository.getOrderDetails(orderId);
    }

    @Override
    public int getTodayOrderCount() throws SQLException {
        return orderRepository.getTodayOrderCount();
    }

    @Override
    public double getTodaySalesTotal() throws SQLException {
        return orderRepository.getTodaySalesTotal();
    }

    @Override
    public Map<String, Double> getSalesLast7Days() throws SQLException {
        return orderRepository.getSalesLast7Days();
    }

    @Override
    public Map<String, Integer> getTopSellingProducts(int limit) throws SQLException {
        return orderRepository.getTopSellingProducts(limit);
    }
}
