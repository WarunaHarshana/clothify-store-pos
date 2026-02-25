package com.clothify.service.custom.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.*;
import com.clothify.repository.custom.OrderRepository;
import com.clothify.repository.custom.ProductRepository;
import com.clothify.repository.custom.impl.OrderRepositoryImpl;
import com.clothify.repository.custom.impl.ProductRepositoryImpl;
import com.clothify.service.custom.OrderService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository = new OrderRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    @Override
    public boolean placeOrder(List<CartItem> cartItems, int cashierUserId) throws SQLException {
        if (cartItems == null || cartItems.isEmpty()) return false;

        Connection conn;
        try {
            conn = DbConnection.getInstance().getConnection();
        } catch (Exception e) {
            throw new SQLException(e);
        }

        try {
            conn.setAutoCommit(false);

            // 1. Validate stock
            for (CartItem item : cartItems) {
                Product product = productRepository.findById(item.getProductId());
                if (product == null || product.getQuantity() < item.getQuantity()) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Calculate total
            double total = cartItems.stream().mapToDouble(CartItem::getLineTotal).sum();

            // 3. Save order
            Order order = new Order(0, null, cashierUserId, total, "COMPLETED");
            int orderId = orderRepository.saveOrder(conn, order);

            // 4. Save order details
            List<OrderDetail> details = new ArrayList<>();
            for (CartItem item : cartItems) {
                details.add(new OrderDetail(
                        0, orderId,
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()
                ));
            }
            orderRepository.saveOrderDetails(conn, details);

            // 5. Deduct stock
            for (CartItem item : cartItems) {
                Product product = productRepository.findById(item.getProductId());
                int newQty = product.getQuantity() - item.getQuantity();
                productRepository.updateQuantity(item.getProductId(), newQty);
            }

            conn.commit();
            return true;
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
}

