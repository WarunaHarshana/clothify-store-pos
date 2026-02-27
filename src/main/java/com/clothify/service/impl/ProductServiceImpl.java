package com.clothify.service.impl;

import com.clothify.model.Product;
import com.clothify.repository.ProductRepository;
import com.clothify.repository.impl.ProductRepositoryImpl;
import com.clothify.service.ProductService;

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
    public boolean addStock(int productId, int qty) throws SQLException {
        Product product = getAllProducts().stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst()
                .orElse(null);
        if (product == null || qty <= 0)
            return false;
        return productRepository.updateQuantity(productId, product.getQuantity() + qty);
    }

    @Override
    public boolean reduceStock(int productId, int qty) throws SQLException {
        Product product = getAllProducts().stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst()
                .orElse(null);
        if (product == null || qty <= 0)
            return false;
        int newQty = product.getQuantity() - qty;
        if (newQty < 0)
            return false;
        return productRepository.updateQuantity(productId, newQty);
    }

    @Override
    public String generateNextCode() throws SQLException {
        return productRepository.generateNextCode();
    }

    @Override
    public int getProductCount() throws SQLException {
        return productRepository.getProductCount();
    }

    @Override
    public int getLowStockCount() throws SQLException {
        return productRepository.getLowStockCount();
    }
}
