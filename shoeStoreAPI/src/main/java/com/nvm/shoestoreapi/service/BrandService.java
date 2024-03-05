package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAll();
    Page<Brand> findAll(Pageable pageable);
    Brand create (BrandRequest brandRequest);
    Brand update (BrandRequest brandRequest);
    void deleteById(Long id);
    void updatesStatus(List<Long> ids, boolean enabled);
    Page<Brand> findByNameContaining(String name, Pageable pageable);
    Page<Brand> findByEnabled(boolean enabled, Pageable pageable);
    long count();
    long countByEnabledTrue();
    long countByEnabledFalse();
    void changeSwitch (Long id);
    Optional<Brand> findById(Long id);
}
