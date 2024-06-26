package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.ReturnProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnProductDetailsRepository extends JpaRepository<ReturnProductDetails, Long> {
    List<ReturnProductDetails> findByReturnProduct_OrderIdAndReturnProduct_StatusAndProductDetailsIdAndReturnTypeIsTrue(Long orderId, String status, Long productDetailsId);
    List<ReturnProductDetails> findByReturnProduct_OrderIdAndReturnTypeIsTrue(Long orderId);
}