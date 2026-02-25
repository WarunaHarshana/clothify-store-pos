package com.clothify.service.custom.impl;

import com.clothify.model.Supplier;
import com.clothify.repository.custom.SupplierRepository;
import com.clothify.repository.custom.impl.SupplierRepositoryImpl;
import com.clothify.service.custom.SupplierService;

import java.sql.SQLException;
import java.util.List;

public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository = new SupplierRepositoryImpl();

    @Override
    public List<Supplier> getAllSuppliers() throws SQLException {
        return supplierRepository.getAll();
    }

    @Override
    public List<Supplier> searchSuppliers(String keyword) throws SQLException {
        return supplierRepository.search(keyword);
    }

    @Override
    public boolean addSupplier(Supplier supplier) throws SQLException {
        return supplierRepository.save(supplier);
    }

    @Override
    public boolean updateSupplier(Supplier supplier) throws SQLException {
        return supplierRepository.update(supplier);
    }

    @Override
    public boolean deleteSupplier(int supplierId) throws SQLException {
        return supplierRepository.delete(supplierId);
    }
}

