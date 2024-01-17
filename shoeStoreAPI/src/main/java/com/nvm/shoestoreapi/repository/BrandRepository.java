package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    boolean existsByName(String name);
}
