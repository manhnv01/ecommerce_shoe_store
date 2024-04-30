package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.dto.response.ReturnProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReturnProductService {
    Page<ReturnProductResponse> findAll(Pageable pageable);

    ReturnProductResponse create(ReturnProductRequest ReturnProductRequest);

    ReturnProductResponse update(Long id, String status, String reason);

    long count();

    Optional<ReturnProductResponse> findById(Long id);

    int countByStatus(String status);

    Page<ReturnProductResponse> findByStatus(String status, Pageable pageable);

    Page<ReturnProductResponse> findByEmployeeNameOrCustomerNameContaining(String employeeName, String customerName, Pageable pageable);


    // theo user cụ thể
    // lấy tất cả đơn hàng của 1 customer theo email
    Page<ReturnProductResponse> findByCustomerAccountEmail(String email, Pageable pageable);
    // đếm theo orderStatus
    long countByCustomerAccountEmailAndStatus(String email, String status);
    // đếm theo email
    long countByCustomerAccountEmail(String email);
}
