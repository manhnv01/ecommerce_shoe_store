package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByIdAndProductsIsNotEmpty(Long categoryId);
    Page<Brand> findByNameContaining(String name, Pageable pageable);
    Page<Brand> findByEnabled(boolean enabled, Pageable pageable);
    Page<Brand> findByNameContainingAndEnabled(String name, boolean enabled, Pageable pageable);
    long countByEnabledTrue();
    long countByEnabledFalse();
    List<Brand> findByEnabledIsTrue();
}
