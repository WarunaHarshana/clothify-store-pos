package com.clothify.repository;

import com.clothify.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryRepository {
    List<Category> getAll() throws SQLException;
    List<Category> search(String keyword) throws SQLException;
    boolean save(Category category) throws SQLException;
    boolean update(Category category) throws SQLException;
    boolean delete(int categoryId) throws SQLException;
}
