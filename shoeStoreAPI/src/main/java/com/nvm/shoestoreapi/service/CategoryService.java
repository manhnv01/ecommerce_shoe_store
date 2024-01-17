package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Page<Category> findAll(Pageable pageable);
    Category createCategory (CategoryRequest categoryRequest);
    Category updateCategory (CategoryRequest categoryRequest);
    void deleteCategoryById(Long id);
    void deleteCategoriesByIds(List<Long> categoryIds);
    void updatesStatus(List<Long> categoryIds, boolean enabled);
    Page<Category> findByNameContaining(String name, Pageable pageable);
    Page<Category> findByEnabled(boolean enabled, Pageable pageable);
    long count();
    long countByEnabledTrue();
    long countByEnabledFalse();
    Optional<Category> findById(Long id);
}
