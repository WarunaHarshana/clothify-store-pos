package com.clothify.service.impl;

import com.clothify.model.Category;
import com.clothify.repository.CategoryRepository;
import com.clothify.repository.impl.CategoryRepositoryImpl;
import com.clothify.service.CategoryService;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    @Override
    public List<Category> getAllCategories() throws SQLException {
        return categoryRepository.getAll();
    }

    @Override
    public List<Category> searchCategories(String keyword) throws SQLException {
        return categoryRepository.search(keyword);
    }

    @Override
    public boolean addCategory(Category category) throws SQLException {
        return categoryRepository.save(category);
    }

    @Override
    public boolean updateCategory(Category category) throws SQLException {
        return categoryRepository.update(category);
    }

    @Override
    public boolean deleteCategory(int categoryId) throws SQLException {
        return categoryRepository.delete(categoryId);
    }
}
