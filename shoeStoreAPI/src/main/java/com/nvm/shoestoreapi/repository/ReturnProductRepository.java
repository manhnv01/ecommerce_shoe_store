package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.entity.ReturnProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, Long> {
    boolean existsByOrderIdAndStatusAndReturnProductDetails_ProductDetailsIdAndReturnProductDetails_ReturnTypeIsTrue(Long orderId, String status, Long productDetailsId);

    Page<ReturnProduct> findByEmployeeNameContainingOrOrder_Customer_NameContaining(String employeeName, String customerName, Pageable pageable);

    Page<ReturnProduct> findByStatus(String status, Pageable pageable);
    int countByStatus(String status);
    boolean existsByOrderIdAndStatus(Long orderId, String status);

    // theo user cụ thể
    // lấy tất cả phiếu đổi trả của 1 customer theo email
    Page<ReturnProduct> findByOrder_Customer_Account_Email(String email, Pageable pageable);

    // đếm theo status và email
    long countByOrder_Customer_Account_EmailAndStatus(String email, String status);
    // đếm theo email
    long countByOrder_Customer_Account_Email(String email);
}