package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.ProductColor;
import com.nvm.shoestoreapi.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails,Long> {
}
