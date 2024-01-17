package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> findAll();
    Supplier createSupplier (SupplierRequest supplierRequest);
    Supplier updateSupplier  (Long id, SupplierRequest supplierRequest);
    void deleteSupplierById(Long id);
}
