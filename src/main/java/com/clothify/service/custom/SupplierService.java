package com.clothify.service.custom;

import com.clothify.model.Supplier;

import java.sql.SQLException;
import java.util.List;

public interface SupplierService {
    List<Supplier> getAllSuppliers() throws SQLException;
    List<Supplier> searchSuppliers(String keyword) throws SQLException;
    boolean addSupplier(Supplier supplier) throws SQLException;
    boolean updateSupplier(Supplier supplier) throws SQLException;
    boolean deleteSupplier(int supplierId) throws SQLException;
}

