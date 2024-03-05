package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    List<Supplier> findAll();
    Page<Supplier> findAll(Pageable pageable);
    Supplier create (SupplierRequest request);
    Supplier update (SupplierRequest request);
    void deleteById(Long id);
    Page<Supplier> findByNameContaining(String name, Pageable pageable);
    long count();
    Optional<Supplier> findById(Long id);
}
