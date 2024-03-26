package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findByEnabledIsTrue();
    Page<Category> findAll(Pageable pageable);
    Category createCategory (CategoryRequest categoryRequest);
    Category updateCategory (CategoryRequest categoryRequest);
    void deleteCategoryById(Long id);
    void updatesStatus(List<Long> categoryIds, boolean enabled);
    Page<Category> findByNameContaining(String name, Pageable pageable);
    Page<Category> searchByNameAndStatus(String name, boolean enabled, Pageable pageable);
    Page<Category> findByEnabled(boolean enabled, Pageable pageable);
    long count();
    long countByEnabledTrue();
    long countByEnabledFalse();
    Optional<Category> findById(Long id);
}
