package com.clothify.repository.custom.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.Order;
import com.clothify.model.OrderDetail;
import com.clothify.repository.custom.OrderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public int saveOrder(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_code, user_id, customer_name, subtotal, discount_percent, discount_amount, tax_amount, total_amount, payment_method, status) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String orderCode = "ORD-" + System.currentTimeMillis();
            pstm.setString(1, orderCode);
            pstm.setInt(2, order.getCashierUserId());
            pstm.setString(3, "Walk-in");
            pstm.setDouble(4, order.getTotalAmount());
            pstm.setDouble(5, 0.00);
            pstm.setDouble(6, 0.00);
            pstm.setDouble(7, 0.00);
            pstm.setDouble(8, order.getTotalAmount());
            pstm.setString(9, "CASH");
            pstm.setString(10, order.getStatus());
            pstm.executeUpdate();

            try (ResultSet keys = pstm.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                throw new SQLException("Failed to get generated order ID");
            }
        }
    }

    @Override
    public void saveOrderDetails(Connection conn, List<OrderDetail> details) throws SQLException {
        String sql = "INSERT INTO order_details (order_id, product_id, product_name, quantity, unit_price, line_total) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            for (OrderDetail d : details) {
                pstm.setInt(1, d.getOrderId());
                pstm.setInt(2, d.getProductId());
                pstm.setString(3, d.getProductName());
                pstm.setInt(4, d.getQuantity());
                pstm.setDouble(5, d.getUnitPrice());
                pstm.setDouble(6, d.getLineTotal());
                pstm.addBatch();
            }
            pstm.executeBatch();
        }
    }

    @Override
    public List<Order> getAll() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_id DESC";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                List<Order> list = new ArrayList<>();
                while (rs.next()) list.add(mapOrder(rs));
                return list;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Order> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM orders WHERE CAST(order_id AS CHAR) LIKE ? OR status LIKE ? ORDER BY order_id DESC";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                String text = "%" + (keyword == null ? "" : keyword.trim()) + "%";
                pstm.setString(1, text);
                pstm.setString(2, text);
                try (ResultSet rs = pstm.executeQuery()) {
                    List<Order> list = new ArrayList<>();
                    while (rs.next()) list.add(mapOrder(rs));
                    return list;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) throws SQLException {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, orderId);
                try (ResultSet rs = pstm.executeQuery()) {
                    List<OrderDetail> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new OrderDetail(
                                rs.getInt("order_detail_id"),
                                rs.getInt("order_id"),
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"),
                                rs.getDouble("unit_price"),
                                rs.getDouble("line_total")
                        ));
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getTodayOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(order_date) = CURDATE()";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public double getTodaySalesTotal() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE DATE(order_date) = CURDATE() AND status='COMPLETED'";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getString("order_date"),
                rs.getInt("user_id"),
                rs.getDouble("total_amount"),
                rs.getString("status")
        );
    }
}

