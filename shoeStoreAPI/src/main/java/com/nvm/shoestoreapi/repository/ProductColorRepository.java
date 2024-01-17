package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
}
