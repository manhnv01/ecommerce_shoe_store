package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByEnabled(boolean enabled, Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    long countByEnabledTrue();
    long countByEnabledFalse();
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
