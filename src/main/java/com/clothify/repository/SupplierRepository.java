package com.clothify.repository;

import com.clothify.model.Supplier;

import java.sql.SQLException;
import java.util.List;

public interface SupplierRepository {
    List<Supplier> getAll() throws SQLException;
    List<Supplier> search(String keyword) throws SQLException;
    boolean save(Supplier supplier) throws SQLException;
    boolean update(Supplier supplier) throws SQLException;
    boolean delete(int supplierId) throws SQLException;
}

