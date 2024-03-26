package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByIdAndProductsIsNotEmpty(Long categoryId);
    Page<Category> findByNameContaining(String name, Pageable pageable);
    Page<Category> findByEnabled(boolean enabled, Pageable pageable);
    Page<Category> findByNameContainingAndEnabled(String name, boolean enabled, Pageable pageable);
    long countByEnabledTrue();
    long countByEnabledFalse();
    List<Category> findByEnabledIsTrue();
}
