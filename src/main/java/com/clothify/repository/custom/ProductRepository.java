package com.clothify.repository.custom;

import com.clothify.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {
    List<Product> getAll() throws SQLException;
    List<Product> search(String keyword) throws SQLException;
    boolean save(Product product) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean delete(int productId) throws SQLException;
    String generateNextCode() throws SQLException;
}
