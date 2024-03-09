package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
    List<ProductColor> findByProductId(Long id);
}
