package com.clothify.service.custom;

import com.clothify.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts() throws SQLException;
    List<Product> searchProducts(String keyword) throws SQLException;
    boolean addProduct(Product product) throws SQLException;
    boolean updateProduct(Product product) throws SQLException;
    boolean deleteProduct(int productId) throws SQLException;
    boolean addStock(int productId, int qty) throws SQLException;
    boolean reduceStock(int productId, int qty) throws SQLException;
    String generateNextCode() throws SQLException;
}
