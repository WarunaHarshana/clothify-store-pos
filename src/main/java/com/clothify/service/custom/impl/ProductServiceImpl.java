package com.clothify.service.custom.impl;

import com.clothify.model.Product;
import com.clothify.repository.custom.ProductRepository;
import com.clothify.repository.custom.impl.ProductRepositoryImpl;
import com.clothify.service.custom.ProductService;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();

    @Override
    public List<Product> getAllProducts() throws SQLException {
        return productRepository.getAll();
    }

    @Override
    public List<Product> searchProducts(String keyword) throws SQLException {
        return productRepository.search(keyword);
    }

    @Override
    public boolean addProduct(Product product) throws SQLException {
        return productRepository.save(product);
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        return productRepository.update(product);
    }

    @Override
    public boolean deleteProduct(int productId) throws SQLException {
        return productRepository.delete(productId);
    }

    @Override
    public String generateNextCode() throws SQLException {
        return productRepository.generateNextCode();
    }
}
