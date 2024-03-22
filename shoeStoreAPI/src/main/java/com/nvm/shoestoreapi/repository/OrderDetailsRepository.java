package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    boolean existsByProductDetailsId(Long productDetailsId);
    long countByProductDetails_ProductColor_Product_Id(Long productId);
}