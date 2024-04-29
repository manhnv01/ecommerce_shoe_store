package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.ReturnProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, Long> {
    boolean existsByOrderIdAndReturnProductDetails_ProductDetailsIdAndReturnProductDetails_ReturnTypeIsTrue(Long orderId, Long productDetailsId);
}