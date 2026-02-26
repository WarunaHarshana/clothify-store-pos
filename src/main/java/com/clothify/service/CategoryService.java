package com.clothify.service;

import com.clothify.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories() throws SQLException;
    List<Category> searchCategories(String keyword) throws SQLException;
    boolean addCategory(Category category) throws SQLException;
    boolean updateCategory(Category category) throws SQLException;
    boolean deleteCategory(int categoryId) throws SQLException;
}
