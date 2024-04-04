package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);

    boolean existsByPhone(String phone);

    Page<Supplier> findByNameContaining(String name, Pageable pageable);
}