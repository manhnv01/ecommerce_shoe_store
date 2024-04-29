package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.entity.ReturnProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, Long> {
    boolean existsByOrderIdAndReturnProductDetails_ProductDetailsIdAndReturnProductDetails_ReturnTypeIsTrue(Long orderId, Long productDetailsId);

    Page<ReturnProduct> findByEmployeeNameContainingOrOrder_Customer_NameContaining(String employeeName, String customerName, Pageable pageable);

    Page<ReturnProduct> findByStatus(boolean status, Pageable pageable);
    int countByStatus(boolean status);
}